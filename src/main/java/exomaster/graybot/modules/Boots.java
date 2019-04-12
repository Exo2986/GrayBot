package exomaster.graybot.modules;

import exomaster.graybot.GrayBot;
import exomaster.graybot.modules.Discord;
import exomaster.grayfw.modules.Module;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;

import java.util.HashMap;
import java.util.Map;

import static exomaster.grayfw.Util.consoleLog;

public class Boots extends Module {
    private static Boots INSTANCE;
    public Map<String, Integer> streaks = new HashMap<>();

    public static void startStreak(Message message) {
        String key = message.getServer().get().getIdAsString() + "_" + message.getChannel().getIdAsString();
        if (INSTANCE.streaks.containsKey(key)) return;
        INSTANCE.streaks.put(key, 1);
        message.getChannel().sendMessage("***WHEEZE*** (\uD83D\uDD25" + "1" + ")");
    }

    public void run() {
        INSTANCE = this;
        String commandPrefix = GrayBot.INSTANCE.getConfig().getEntryAsString("COMMAND_PREFIX");
        Discord.INSTANCE.getApi().addMessageCreateListener(event -> {
            Message message = event.getMessage();
            message.getUserAuthor().ifPresent(user -> {
                if (!user.isYourself() && !user.isBot()) {
                    String key = message.getServer().get().getIdAsString() + "_" + message.getChannel().getIdAsString();
                    if (message.getContent().toLowerCase().contains("boots")) {
                        if (streaks.containsKey(key) && !message.getContent().toLowerCase().contains(commandPrefix + "boots")) {
                            streaks.put(key, streaks.get(key) + 1);
                            message.getChannel().sendMessage("***WHEEZE*** (\uD83D\uDD25" + streaks.get(key) + ")");
                        }
                    } else if (streaks.containsKey(key)) {
                        String propKey;
                        if (message.isServerMessage())
                            propKey = "BootsHiScore_" + message.getServer().get().getIdAsString();
                        else
                            propKey = "BootsHiScore_" + message.getChannel().getIdAsString();

                        int score = streaks.get(key);
                        String hiscore = GrayBot.INSTANCE.getProperty(propKey);
                        String hiscoreMsg;
                        if (hiscore == null || score > Integer.parseInt(hiscore)) {
                            GrayBot.INSTANCE.setProperty(propKey, streaks.get(key).toString());
                            hiscoreMsg = "New Server High Score!";
                        } else {
                            hiscoreMsg = "Server High Score: " + hiscore;
                        }

                        message.getChannel().sendMessage("Streak ended! Score: " + score + ", " + hiscoreMsg);
                        streaks.remove(key);
                    }
                }
            });
        });
    }
}
