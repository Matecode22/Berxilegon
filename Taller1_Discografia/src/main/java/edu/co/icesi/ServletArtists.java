package edu.co.icesi;

import edu.co.icesi.model.Artist;
import edu.co.icesi.service.ArtistService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/artists")
public class ServletArtists extends HttpServlet {

    private ArtistService artistService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        artistService = Application.getContext()
                .getBean("artistService", ArtistService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<h1>Artistas registrados<h1>");
        resp.getWriter().println("<ul>");
        for (Artist artist : artistService.getArtists()) {
            resp.getWriter().println("<li>" + artist + "</li>");
        }
        resp.getWriter().println("</ul>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String nationality = req.getParameter("nationality");

        Artist artist = new Artist(id, name, nationality);
        artistService.addArtist(artist);
        resp.getWriter().println("Artista registrado: " + artist);
    }
}
