package exomaster.graybot;

import exomaster.graybot.modules.Discord;
import exomaster.grayfw.config.Config;
import exomaster.grayfw.config.ConfigEntry;
import exomaster.grayfw.logging.Logging;
import exomaster.grayfw.modules.Module;
import exomaster.grayfw.Util;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static exomaster.grayfw.Util.consoleLog;
public class GrayBot {
    public static GrayBot INSTANCE;

    private Logging logging;
    private Config config;
    public Config getConfig() {
        return INSTANCE.config;
    }
    private List<Module> modules;

    public static void main(String[] args) throws Exception {
        new GrayBot();
    }

    private GrayBot() throws Exception {
        INSTANCE = this;
        Util.setProgramID("GRAYBOT");

        logging = new Logging();
        logging.run();

        config = new Config("Config/GrayBot.cfg");

        //Add config entries
        config.addConfigEntry(new ConfigEntry<>("DISCORD_TOKEN", "token"));
        config.addConfigEntry(new ConfigEntry<>("COMMAND_PREFIX", "."));

        config.init();

        if (this.config.getHasUpdatedConfigFile()) {
            consoleLog("Config.txt has been created or updated with missing values. Please input the proper values of each new entry before running this bot again.");
            System.exit(1);
        }

        consoleLog("Establishing connection with Discord...");
        new Discord();
    }

    public void postDiscordApiAuth() {
        consoleLog("Loading modules...");
        modules = new ArrayList<>();
        Reflections ref = new Reflections();
        Set<Class<? extends Module>> classSet = ref.getSubTypesOf(Module.class);
        for (Class<? extends Module> c : classSet) {
            try {
                consoleLog("Loading Module " + c.getSimpleName() + "...");
                Module md = (Module) Module.class.getClassLoader().loadClass(c.getCanonicalName()).newInstance();
                this.modules.add(md);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        consoleLog("Modules successfully loaded.");
    }
}
