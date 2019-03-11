package exomaster.graybot.modules.commands;

import exomaster.graybot.modules.Discord;
import exomaster.graybot.structures.Command;
import exomaster.grayfw.modules.Module;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static exomaster.grayfw.Util.consoleLog;

public class Commands extends Module {
    public static List<Command> commands;

    public void run() {
        consoleLog("Loading commands...");
        commands = new ArrayList<>();
        Reflections ref = new Reflections();
        Set<Class<? extends Command>> classSet = ref.getSubTypesOf(Command.class);
        for (Class<? extends Command> c : classSet) {
            try {
                consoleLog("Loading Command " + c.getSimpleName() + "...");
                Command cmd = (Command) Command.class.getClassLoader().loadClass(c.getCanonicalName()).newInstance();
                this.commands.add(cmd);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        consoleLog("Commands successfully loaded.");
        Discord.INSTANCE.getApi().addMessageCreateListener(new CommandHandler(commands));
    }
}
