package exomaster.graybot.modules.commands;

import exomaster.graybot.GrayBot;
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
        this.hidden = false;
    }

    @Override
    public void run(Message message) {
        String chapter = GrayBot.INSTANCE.getProperty("MyImmortalChapter");
        if (chapter == null) {
            chapter = "45";
        } else {
            chapter = Integer.toString(Integer.parseInt(chapter) + 1);
        }
        GrayBot.INSTANCE.setProperty("MyImmortalChapter", chapter);

        createAndSendChain(message, "Chapter " + chapter + ":\r\n\r\n", Markov.INSTANCE.immortal);
    }
}
