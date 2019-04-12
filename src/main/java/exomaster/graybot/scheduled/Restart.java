package exomaster.graybot.scheduled;

import exomaster.graybot.GrayBot;
import exomaster.grayfw.Util;

import java.util.TimerTask;

public class Restart extends TimerTask {
    @Override
    public void run() {
        Util.consoleLog("Restarting...");
        System.exit(0);
    }
}
