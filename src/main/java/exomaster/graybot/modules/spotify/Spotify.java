package exomaster.graybot.modules.spotify;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.Track;
import exomaster.graybot.GrayBot;
import exomaster.graybot.modules.Discord;
import exomaster.graybot.modules.spotify.webserver.SpotifyServer;
import exomaster.grayfw.config.Config;
import exomaster.grayfw.modules.Module;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;

import java.awt.*;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static exomaster.grayfw.Util.consoleLog;

public class Spotify extends Module {
    public static Spotify INSTANCE;
    private SpotifyApi spotify;
    private List<Server> spotifyEnabledServers;
    private Map<Server, TextChannel> spotifyChannels;
    private Map<Server, String> serverPlaylists;

    public SpotifyApi getSpotify() {
        return spotify;
    }

    public void updateTokens(String accessToken, String refreshToken, int refreshTime) {
        spotify.setAccessToken(accessToken);
        spotify.setRefreshToken(refreshToken);
        Timer timer = new Timer();
        timer.schedule(new RefreshTimer(), (refreshTime-60)*1000);
    }

    public String createDescription(String name) {
        return "Server playlist for " + name + ", created by GrayBot";
    }

    public boolean addSpotifyEnabledServer(Server server, TextChannel channel) {
        if (!spotifyEnabledServers.contains(server)) {
            spotifyEnabledServers.add(server);
            spotifyChannels.put(server, channel);
            String serverName = server.getName();
            Playlist playlist = null;
            try {
                playlist = spotify.createPlaylist("1ub4ta4ec8o4jeh8gzf1wt3o2",serverName)
                        .collaborative(false)
                        .public_(true)
                        .description(createDescription(serverName))
                        .build()
                        .execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            serverPlaylists.put(server, playlist.getId());
            updateSpotifyEnabledServers();
            return true;
        } else {
            return false;
        }
    }

    private void updateSpotifyEnabledServers() {
        GrayBot grayBot = GrayBot.INSTANCE;
        String storedIDs = null;
        for (Server s : spotifyEnabledServers) {
            if (storedIDs == null)
                storedIDs = s.getIdAsString();
            else
                storedIDs += "," + s.getIdAsString();

            grayBot.setProperty("spotify_channel_" + s.getIdAsString(), spotifyChannels.get(s).getIdAsString());
            grayBot.setProperty("spotify_playlist_" + s.getIdAsString(), serverPlaylists.get(s));
        }
        grayBot.setProperty("spotify_enabled_servers", storedIDs);
    }

    public void createServerPlaylists() {
        for (Server server : spotifyEnabledServers) {
            createServerPlaylist(server);
        }
    }

    public Playlist createServerPlaylist(Server server) {
        if (!spotifyEnabledServers.contains(server)) return null;
        consoleLog("Creating server playlist for " + server.getName());
        TextChannel channel = spotifyChannels.get(server);
        List<String> songs = new ArrayList<>();
        channel.getMessagesAsStream().forEach(message -> {
            String msg = message.getContent();
            if (msg.contains("https://open.spotify.com/track/")) {
                String songID = msg.replaceAll("(https://open.spotify.com/track/)|([?].*)", "");
                try {
                    Track track = spotify.getTrack(songID).build().execute();
                    if (track != null) {
                        songs.add("spotify:track:" + track.getId());
                    }
                } catch (Exception e) {}
            }
        });
        consoleLog("Found " + songs.size() + " songs on " + server.getName() + ".");
        try {
            ArrayList<String> songsArr = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 30; i++) {
                String s = songs.get(random.nextInt(songs.size()));
                if (!songsArr.contains(s)) {
                    songsArr.add(i, s);
                } else {
                    i--;
                }
            }
            spotify.replacePlaylistsTracks(serverPlaylists.get(server), songsArr.toArray(new String[30]))
                    .build()
                    .execute();

            Playlist playlist = spotify.getPlaylist(serverPlaylists.get(server)).build().execute();

            consoleLog("Completed.");

            channel.sendMessage("Updated: " + playlist.getExternalUrls().get("spotify"));

            return playlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        consoleLog("Failed.");
        return null;
    }

    @Override
    public void run() {
        INSTANCE = this;
        GrayBot grayBot = GrayBot.INSTANCE;
        Config config = grayBot.getConfig();
        spotify = SpotifyApi.builder()
                .setClientId(config.getEntryAsString("SPOTIFY_CLIENT_ID"))
                .setClientSecret(config.getEntryAsString("SPOTIFY_CLIENT_SECRET"))
                .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8080/post/"))
                .build();

        SpotifyServer httpServer = new SpotifyServer(8080);
        try {
            httpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        URI uri = spotify.authorizationCodeUri()
                .scope("playlist-modify-public,playlist-modify-private").build().execute();
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishRun(String code, String state) {
        try {
            AuthorizationCodeCredentials auth = spotify.authorizationCode(code).build().execute();
            updateTokens(auth.getAccessToken(), auth.getRefreshToken(), auth.getExpiresIn());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        GrayBot grayBot = GrayBot.INSTANCE;
        boolean previouslyExists = !grayBot.tryCreateProperty("spotify_enabled_servers", "");

        spotifyEnabledServers = new ArrayList<>();
        spotifyChannels = new HashMap<>();
        serverPlaylists = new HashMap<>();
        if (previouslyExists) {
            String[] serverIDs = grayBot.getProperty("spotify_enabled_servers").split(",");
            DiscordApi api = Discord.INSTANCE.getApi();
            for (String id : serverIDs) {
                api.getServerById(id).ifPresent(server -> spotifyEnabledServers.add(server));
            }

            for (Server s : spotifyEnabledServers) {
                String channelID = grayBot.getProperty("spotify_channel_" + s.getIdAsString());
                spotifyChannels.put(s, s.getChannelById(channelID).get().asServerTextChannel().get());
                String playlistID = grayBot.getProperty("spotify_playlist_" + s.getIdAsString());
                serverPlaylists.put(s, playlistID);
            }
        }

        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("America/New_York");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNext2 ;
        zonedNext2 = zonedNow.withHour(18).withMinute(0).withSecond(0);
        if(zonedNow.compareTo(zonedNext2) > 0)
            zonedNext2 = zonedNext2.plusDays(1);

        Duration duration = Duration.between(zonedNow, zonedNext2);
        long initalDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new UpdatePlaylist(), initalDelay,
                24*60*60, TimeUnit.SECONDS);
    }
}
