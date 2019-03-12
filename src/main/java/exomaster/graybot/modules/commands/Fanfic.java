package exomaster.graybot.modules.commands;

import exomaster.graybot.modules.markov.Markov;
import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static exomaster.grayfw.Util.consoleLog;

public class Fanfic extends MarkovCommand {
    public Fanfic() {
        this.callback = "fanfic";
        this.name = "Fanfic";
        this.description = "Generates an Imagine Dragons fanfic.";
        this.hidden = false;
    }

    @Override
    public void run(Message message) {
        this.createAndSendChain(message, Markov.INSTANCE.chain);
    }
}
