package exomaster.graybot.modules.commands;

import exomaster.graybot.modules.markov.Markov;
import exomaster.graybot.structures.Command;
import exomaster.graybot.structures.MarkovStructure;
import org.javacord.api.entity.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MarkovCommand extends Command {
    public MarkovCommand() {
        hidden = true;
    }

    protected void createAndSendChain(Message message, MarkovStructure struct) {
        Map<String, HashMap<String, Integer>> chain = struct.chain;
        int chars = 0;
        StringBuilder msg = new StringBuilder();
        Random random = new Random();
        String word = new ArrayList<>(chain.keySet()).get(random.nextInt(chain.keySet().size()));
        while (chars+word.length()+1 < 2000) {
            msg.append(struct.getProperCapitalization(word) + " ");
            HashMap<String,Integer> occ = chain.get(word);
            ArrayList<String> follows = new ArrayList<>(occ.keySet());
            ArrayList<String> followsWeighted = new ArrayList<>();
            for (String s : follows) {
                for (int i = 0; i < occ.get(s); i++) {
                    followsWeighted.add(s);
                }
            }

            if (followsWeighted.size() == 0)
                break;

            word = followsWeighted.get(random.nextInt(followsWeighted.size()));
            chars+=word.length()+1;
        }
        String strMsg = msg.toString();
        int[] punks = new int[3];
        punks[0] = strMsg.lastIndexOf('.');
        punks[1] = strMsg.lastIndexOf('?');
        punks[2] = strMsg.lastIndexOf('!');
        int max = 0;
        for (int i : punks) {
            if (i > max)
                max = i;
        }
        if (max+1 != strMsg.length())
            strMsg = strMsg.substring(0, max+1);

        message.getChannel().sendMessage(strMsg);
    }
}
