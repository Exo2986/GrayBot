package exomaster.graybot.modules.commands;

import exomaster.graybot.GrayBot;
import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;

public class Help extends Command {
    protected String helpMsg = "undefined";

    public Help() {
        this.callback = "help";
        this.name = "Help";
        this.description = "Displays a list of commands.";
    }

    @Override
    public void run(Message message) {
        if (helpMsg.equals("undefined")) {
            StringBuilder helpBuilder = new StringBuilder();
            String commandPrefix = GrayBot.INSTANCE.getConfig().getEntryAsString("COMMAND_PREFIX");
            for (Command c : Commands.commands) {
                helpBuilder.append("**__");
                helpBuilder.append(c.name);
                helpBuilder.append("__**\r\n`");
                helpBuilder.append(commandPrefix);
                helpBuilder.append(c.callback);
                helpBuilder.append(c.usage);
                helpBuilder.append(": ");
                helpBuilder.append(c.description);
                helpBuilder.append("`\r\n");
            }
            helpMsg = helpBuilder.toString();
        }
        message.getChannel().sendMessage(helpMsg);
    }
}
