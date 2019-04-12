package exomaster.graybot.modules.commands;

import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;

public class Boots extends Command {
    public Boots() {
        this.callback = "boots";
        this.name = "Boots";
        this.description = "WHEEZE";
    }

    @Override
    public void run(Message message) {
        exomaster.graybot.modules.Boots.startStreak(message);
    }
}
