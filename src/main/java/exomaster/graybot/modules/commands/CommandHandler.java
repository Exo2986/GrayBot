package exomaster.graybot.modules.commands;

import exomaster.graybot.GrayBot;
import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.List;

public class CommandHandler implements MessageCreateListener {
    protected List<Command> commands;
    protected String commandPrefix;

    public CommandHandler(List<Command> commands) {
        this.commands = commands;
        commandPrefix = GrayBot.INSTANCE.getConfig().getEntryAsString("COMMAND_PREFIX");
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Message message = event.getMessage();
        String msgC = message.getContent();
        if (msgC.startsWith(commandPrefix)) {
            for (Command c : commands) {
                if (msgC.startsWith(commandPrefix + c.callback)) {
                    c.run(message);
                }
            }
        }
    }
}
