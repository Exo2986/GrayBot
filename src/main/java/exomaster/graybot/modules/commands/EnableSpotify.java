package exomaster.graybot.modules.commands;

import exomaster.graybot.modules.spotify.Spotify;
import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;

public class EnableSpotify extends Command {
    public EnableSpotify() {
        this.callback = "enablespotify";
        this.name = "Enable Spotify";
        this.description = "Enables spotify integration features, for me to grab songs from a set channel to add to a playlist every day!";
        this.usage = " <#channel>";
        this.adminOnly = true;
    }

    @Override
    public void run(Message message) {
        message.getServer().ifPresent(server -> {
            boolean success = Spotify.INSTANCE.addSpotifyEnabledServer(server, message.getChannel());
            if (success)
                message.getChannel().sendMessage("Successfully enabled Spotify integration for this server.");
            else
                message.getChannel().sendMessage("Spotify integration is already enabled on this server!");
        });
    }
}
