package exomaster.graybot.modules.commands;

import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;

public class Test extends Command {
    public Test() {
        this.callback = "test";
        this.name = "Test";
        this.description = "Test command.";
    }

    public void run(Message message) {
        message.getChannel().sendMessage("gabby gay");
    }
}
