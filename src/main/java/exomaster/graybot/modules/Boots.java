package exomaster.graybot.modules;

import exomaster.graybot.modules.Discord;
import exomaster.grayfw.modules.Module;
import org.javacord.api.entity.message.Message;

import java.util.HashMap;
import java.util.Map;

import static exomaster.grayfw.Util.consoleLog;

public class Boots extends Module {
    private Map<String, Integer> streaks = new HashMap<>();

    public void run() {
        Discord.INSTANCE.getApi().addMessageCreateListener(event -> {
            Message message = event.getMessage();
            String key = message.getServer().get().getIdAsString() + "_" + message.getChannel().getIdAsString();
            if (message.getContent().toLowerCase().contains("boots")) {
                if (streaks.containsKey(key))
                    streaks.put(key,streaks.get(key)+1);
                else
                    streaks.put(key, 1);

                message.getChannel().sendMessage("***WHEEZE*** (\uD83D\uDD25" + streaks.get(key) + ")");
            } else if (streaks.containsKey(key)) {
                message.getUserAuthor().ifPresent(user -> {
                    if (!user.isYourself() && !user.isBot()){
                        message.getChannel().sendMessage("Streak ended! Score: " + streaks.get(key));
                        streaks.remove(key);
                    }
                });
            }
        });
    }
}
