package exomaster.graybot.structures;

import java.util.HashMap;
import java.util.Map;

public class MarkovStructure {
    public Map<String, HashMap<String, Integer>> chain;
    public HashMap<String,String> lowerToCap;

    public MarkovStructure(Map<String, HashMap<String, Integer>> chain, HashMap<String,String> lowerToCap) {
        this.chain = chain;
        this.lowerToCap = lowerToCap;
    }

    public String getProperCapitalization(String word) {
        return lowerToCap.get(word);
    }
}
