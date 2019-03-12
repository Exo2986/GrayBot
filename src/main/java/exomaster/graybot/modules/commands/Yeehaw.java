package exomaster.graybot.modules.commands;

import exomaster.graybot.structures.Command;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static exomaster.grayfw.Util.consoleLog;

public class Yeehaw extends Command {
    private List<File> images = null;
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
            ClassLoader loader = getClass().getClassLoader();
            images = new ArrayList<>();
            for (String s : resourceList) {
                images.add(new File(URLDecoder.decode(loader.getResource(s).getFile())));
            }
        }
        new MessageBuilder()
                .append("Yeehaw!")
                .addAttachment(images.get(random.nextInt(images.size())))
                .send(message.getChannel());
    }
}
