package exomaster.graybot.modules.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.util.TimerTask;

import static exomaster.grayfw.Util.consoleLog;
public class RefreshTimer extends TimerTask {

    @Override
    public void run() {
        consoleLog("Refreshing Spotify tokens...");
        SpotifyApi api = Spotify.INSTANCE.getSpotify();
        try {
            AuthorizationCodeCredentials auth = api.authorizationCodeRefresh().build().execute();
            String refreshToken = auth.getRefreshToken();
            if (refreshToken == null) refreshToken = api.getRefreshToken();
            Spotify.INSTANCE.updateTokens(auth.getAccessToken(), refreshToken, auth.getExpiresIn());
            consoleLog("Spotify tokens successfully refreshed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
