package exomaster.graybot.modules.spotify.webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class SpotifyServer extends Server {
    public SpotifyServer(int port) {
        super(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler
                .NO_SESSIONS);
        context.addServlet(new ServletHolder(new SpotifyPost()), "/post/*");
        this.setHandler(context);
        this.setStopAtShutdown(true);
    }
}
