package exomaster.graybot.modules.markov;

import exomaster.graybot.structures.MarkovStructure;
import exomaster.grayfw.modules.Module;

import static exomaster.grayfw.Util.consoleLog;

public class Markov extends Module {
    public static Markov INSTANCE;

    public MarkovStructure chain;
    public MarkovStructure immortal;

    @Override
    public void run() {
        INSTANCE = this;
        ClassLoader loader = getClass().getClassLoader();
        chain = MarkovGenerator.generateFromFile(loader.getResourceAsStream("markov/markov.txt"));
        immortal = MarkovGenerator.generateFromFile(loader.getResourceAsStream("markov/myimmortal.txt"));
    }
}
