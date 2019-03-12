package exomaster.graybot.modules.commands;

import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;

public class Echo extends Command {
    public Echo() {
        this.callback = "echo";
        this.name = "Echo";
        this.description = "Repeats what you say.";
        this.usage = " <message>";
    }

    @Override
    public void run(Message message) {
        message.getChannel().sendMessage(message.getContent().replaceFirst(".echo",""));
    }
}
