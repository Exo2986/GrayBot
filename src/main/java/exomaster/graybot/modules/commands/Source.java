package exomaster.graybot.modules.commands;

import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;

public class Source extends Command {
    public Source() {
        this.callback = "source";
        this.name = "Source";
        this.description = "Sends the github repository for this bot.";
    }

    @Override
    public void run(Message message) {
        message.getChannel().sendMessage("https://github.com/ExoMaster/GrayBot");
    }
}
