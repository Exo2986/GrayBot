package exomaster.graybot.modules.markov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class MarkovGenerator {
    public static Map<String, HashMap<String, Integer>> generateFromFile(File file) {
        Map<String, HashMap<String, Integer>> chain = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line;
            String lastWord = null;
            while ((line = reader.readLine()) != null) {
                if (lastWord != null)
                    line = lastWord + " " + line;

                String[] splt = line.split("\\s+");
                for (int i = 0; i < splt.length; i++) {
                    String s = splt[i];
                    HashMap<String, Integer> occ;
                    if (chain.containsKey(s)) {
                        occ = chain.get(s);
                    } else {
                        occ = new HashMap<>();
                    }

                    if (i+1 < splt.length) {
                        occ.put(splt[i+1], occ.getOrDefault(s, 0) + 1);
                    } else {
                        lastWord = s;
                    }
                    chain.put(s, occ);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain;
    }
}
