package edu.co.icesi;

import edu.co.icesi.model.Artist;
import edu.co.icesi.model.Track;
import edu.co.icesi.service.ArtistService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/searchArtist")
public class ServletSearchArtist extends HttpServlet {

    private ArtistService artistService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        artistService = Application.getContext()
                .getBean("artistService", ArtistService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        Artist artist = artistService.getArtistByName(name);

        resp.setContentType("text/html");
        if (artist == null) {
            resp.getWriter().println("<h1>Artista no encontrado</h1>");
            return;
        }

        resp.getWriter().println("<h1>Artista encontrado</h1>");
        resp.getWriter().println("<p>" + artist + "</p>");
        resp.getWriter().println("<h2>Tracks</h2>");
        resp.getWriter().println("<ul>");
        for (Track track : artistService.getTracksByArtistName(name)) {
            resp.getWriter().println("<li>" + track + "</li>");
        }
        resp.getWriter().println("</ul>");
    }
}
