package exomaster.graybot.modules.spotify.webserver;

import exomaster.graybot.modules.spotify.Spotify;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class SpotifyPost extends HttpServlet {
    public SpotifyPost() {}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            final long timeStamp = System.currentTimeMillis();
            final String code = request.getParameter("code");
            final String state = request.getParameter("state");
            Spotify.INSTANCE.finishRun(code, state);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("");
            response.getWriter().close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            final long timeStamp = System.currentTimeMillis();
            final String code = request.getParameter("code");
            final String state = request.getParameter("state");
            Spotify.INSTANCE.finishRun(code, state);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("");
            response.getWriter().close();
        }
    }
}
