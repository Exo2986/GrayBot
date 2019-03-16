package exomaster.graybot.modules.spotify;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Playlist;
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
import java.util.*;
import java.util.List;

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
        return "Server playlist for " + name + ", created by GrayBot (https://github.com/ExoMaster/GrayBot)";
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
                    songs.add("spotify:track:" + spotify.getTrack(songID).market(CountryCode.US).build().execute().getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            spotify.replacePlaylistsTracks(serverPlaylists.get(server), songs.toArray(new String[songs.size()]))
                    .build()
                    .execute();

            return spotify.getPlaylist(serverPlaylists.get(server)).build().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                .scope("playlist-modify-public").build().execute();
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
    }
}
