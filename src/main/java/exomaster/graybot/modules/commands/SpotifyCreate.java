package exomaster.graybot.modules.commands;

import com.wrapper.spotify.model_objects.specification.Playlist;
import exomaster.graybot.modules.spotify.Spotify;
import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;

public class SpotifyCreate extends Command {
    public SpotifyCreate() {
        this.callback = "spotifycreate";
        this.name = "Spotify Create";
        this.description = "Creates a spotify playlist.";
        this.adminOnly = true;
    }

    @Override
    public void run(Message message) {
        message.getServer().ifPresent(server -> {
            Spotify.INSTANCE.createServerPlaylist(server);
        });

    }
}
