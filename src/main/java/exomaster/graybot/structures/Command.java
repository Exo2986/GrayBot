package exomaster.graybot.structures;

import org.javacord.api.entity.message.Message;

public abstract class Command {
    public String callback;
    public String name;
    public String description;

    public Command() {
        callback = "test";
        name = "test";
        description = "this is a description";
    }

    public void run (Message message) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
