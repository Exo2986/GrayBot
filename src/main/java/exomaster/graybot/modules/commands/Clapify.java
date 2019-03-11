package exomaster.graybot.modules.commands;

import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;

public class Clapify extends Command {
    public Clapify() {
        this.callback = "clapify";
        this.name = "Clapify";
        this.description = "Makes every message 100% more important.";
        this.usage = " <message>";
    }

    @Override
    public void run(Message message) {
        message.getChannel().sendMessage(message.getContent().toUpperCase().replaceAll("(.CLAPIFY )|\\s|$","\uD83D\uDC4F"));
    }
}
