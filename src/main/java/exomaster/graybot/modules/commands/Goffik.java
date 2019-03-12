package exomaster.graybot.modules.commands;

import exomaster.graybot.modules.markov.Markov;
import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Goffik extends MarkovCommand {
    public Goffik() {
        this.callback = "goffik";
        this.name = "Goffik";
        this.description = "Immediately destroys all posr preps.";
    }

    @Override
    public void run(Message message) {
        createAndSendChain(message, Markov.INSTANCE.immortal);
    }
}
