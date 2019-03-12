package exomaster.graybot.modules.commands;

import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static exomaster.grayfw.Util.consoleLog;

public class Yeehaw extends Command {
    private List<String> images = null;
    private Random random = new Random();
    public Yeehaw() {
        this.callback = "yeehaw";
        this.name = "Yeehaw";
        this.description = "Yeehaw, pardner.";
    }

    @Override
    public void run(Message message) {
        if (images == null) {
            Reflections reflections = new Reflections("cowboys", new ResourcesScanner());
            Set<String> resourceList = reflections.getResources(x -> x.contains("cowboy"));
            images = new ArrayList<>();
            for (String s : resourceList) {
                images.add(s);
            }
        }
        ClassLoader loader = getClass().getClassLoader();
        new MessageBuilder()
                .append("Yeehaw!")
                .addAttachment(loader.getResourceAsStream(images.get(random.nextInt(images.size()))), "yeehaw.png")
                .send(message.getChannel());
    }
}
