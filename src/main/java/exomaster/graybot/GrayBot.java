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

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
    private Properties properties;

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String val) {
        properties.setProperty(key, val);
        this.writePropertiesToFile();
    }

    public boolean tryCreateProperty(String key, String val) {
        if (!properties.containsKey(key)) {
            properties.setProperty(key, val);
            this.writePropertiesToFile();
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        new GrayBot();
    }

    private GrayBot() throws Exception {
        INSTANCE = this;
        Util.setProgramID("GRAYBOT");

        logging = new Logging();
        logging.run();

        config = new Config("config/graybot.cfg");

        //Add config entries
        config.addConfigEntry(new ConfigEntry<>("DISCORD_TOKEN", "token"));
        config.addConfigEntry(new ConfigEntry<>("COMMAND_PREFIX", "."));
        config.addConfigEntry(new ConfigEntry<>("SPOTIFY_CLIENT_ID", "id"));
        config.addConfigEntry(new ConfigEntry<>("SPOTIFY_CLIENT_SECRET", "token"));

        config.init();

        File propertiesFile = new File("data/graybot.properties");

        File filePath = new File(propertiesFile.getParent());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        if (!propertiesFile.exists()) {
            try {
                propertiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        readPropertiesFromFile();

        if (this.config.getHasUpdatedConfigFile()) {
            consoleLog("The config has been created or updated with missing values. Please input the proper values of each new entry before running this bot again.");
            System.exit(1);
        }

        consoleLog("Establishing connection with Discord...");
        new Discord();
    }

    public void readPropertiesFromFile() {
        properties = new Properties();
        InputStream stream;

        try {
            File file = new File("data/graybot.properties");
            stream = new FileInputStream(file);

            properties.load(stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writePropertiesToFile() {
        try {
            File file = new File("data/graybot.properties");
            OutputStream out = new FileOutputStream(file);
            properties.store(out, "GrayBot Properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
