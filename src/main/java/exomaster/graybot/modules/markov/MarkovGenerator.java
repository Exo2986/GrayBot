package exomaster.graybot.modules.markov;

import exomaster.graybot.structures.MarkovStructure;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MarkovGenerator {
    public static MarkovStructure generateFromFile(InputStream stream) {
        Map<String, HashMap<String, Integer>> chain = new HashMap<>();
        HashMap<String, String> lowerToCap = new HashMap<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            String lastWord = null;
            while ((line = reader.readLine()) != null) {
                if (lastWord != null)
                    line = lastWord + " " + line;

                String[] splt = line.split("\\s+");
                for (String s : splt) {
                    lowerToCap.put(s.toLowerCase(), s);
                }
                line = line.toLowerCase();
                splt = line.split("\\s+");
                for (int i = 0; i < splt.length; i++) {
                    String s = splt[i];
                    HashMap<String, Integer> occ;
                    if (chain.containsKey(s)) {
                        occ = chain.get(s);
                    } else {
                        occ = new HashMap<>();
                    }

                    if (i+1 < splt.length) {
                        occ.put(splt[i+1], occ.getOrDefault(splt[i+1], 0) + 1);
                    } else {
                        lastWord = s;
                    }
                    chain.put(s, occ);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MarkovStructure(chain, lowerToCap);
    }
}
