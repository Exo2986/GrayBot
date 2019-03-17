package exomaster.graybot.modules.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.util.TimerTask;

import static exomaster.grayfw.Util.consoleLog;

public class UpdatePlaylist extends TimerTask {
    @Override
    public void run() {
        Spotify spotify = Spotify.INSTANCE;
        spotify.createServerPlaylists();
    }
}
