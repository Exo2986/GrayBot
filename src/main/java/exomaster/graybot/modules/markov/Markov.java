package exomaster.graybot.modules.markov;

import exomaster.grayfw.modules.Module;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static exomaster.grayfw.Util.consoleLog;

public class Markov extends Module {
    public static Markov INSTANCE;

    public Map<String, HashMap<String, Integer>> chain;

    @Override
    public void run() {
        INSTANCE = this;
        ClassLoader loader = getClass().getClassLoader();
        chain = MarkovGenerator.generateFromFile(loader.getResourceAsStream("markov/markov.txt"));
    }
}
