package exomaster.graybot.structures;

import org.javacord.api.entity.message.Message;

public abstract class Command {
    public String callback;
    public String name;
    public String description;
    public String usage;
    public boolean hidden = false;

    public Command() {
        callback = "test";
        name = "test";
        description = "this is a description";
        usage = "";
    }

    public void run (Message message) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
