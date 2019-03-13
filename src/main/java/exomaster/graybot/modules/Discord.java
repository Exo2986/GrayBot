package exomaster.graybot.modules;

import exomaster.graybot.GrayBot;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

import static exomaster.grayfw.Util.consoleLog;

public class Discord {
    public static Discord INSTANCE;
    private DiscordApi api;

    public DiscordApi getApi() {
        return api;
    }

    public void setApi(DiscordApi api) {
        this.api = api;
    }

    public Discord() {
        INSTANCE = this;
        new DiscordApiBuilder().setToken(GrayBot.INSTANCE.getConfig().getEntryAsString("DISCORD_TOKEN")).login().thenAccept(a -> {
            this.api = a;
            this.api.updateActivity(ActivityType.LISTENING, "Boots (" + GrayBot.INSTANCE.getConfig().getEntryAsString("COMMAND_PREFIX") + "help)");
            this.api.setMessageCacheSize(10, this.api.getDefaultMessageCacheStorageTimeInSeconds());

            consoleLog("Connected.");

            GrayBot.INSTANCE.postDiscordApiAuth();
        }).join();
    }
}
