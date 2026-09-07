package edu.co.icesi;

import edu.co.icesi.model.Artist;
import edu.co.icesi.model.Track;
import edu.co.icesi.service.TrackService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/tracks")
public class ServletTracks extends HttpServlet {

    private TrackService trackService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        trackService = Application.getContext()
                .getBean("trackService", TrackService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<h1>Tracks registrados<h1>");
        resp.getWriter().println("<ul>");
        for (Track track : trackService.getTracks()) {
            resp.getWriter().println("<li>" + track + "<ul>");
            for (Artist artist : trackService.getArtistsByTrack(track.getId())) {
                resp.getWriter().println("<li>Artista: " + artist + "</li>");
            }
            resp.getWriter().println("</ul></li>");
        }
        resp.getWriter().println("</ul>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String title = req.getParameter("title");
        String genre = req.getParameter("genre");
        int duration = Integer.parseInt(req.getParameter("duration"));
        String albumTitle = req.getParameter("albumTitle");

        Track track = new Track(id, title, genre, duration, albumTitle);

        for (int i = 1; i <= 10; i++) {
            String artistId = req.getParameter("artistId" + i);
            if (artistId != null && !artistId.equals("")) {
                track.getArtistIds().add(Integer.parseInt(artistId));
            }
        }

        trackService.addTrack(track);
        resp.getWriter().println("Operacion de registrar track ejecutada");
    }
}
