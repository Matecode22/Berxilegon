package edu.co.icesi;

import edu.co.icesi.service.TrackService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteTrack")
public class ServletDeleteTrack extends HttpServlet {

    private TrackService trackService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        trackService = Application.getContext()
                .getBean("trackService", TrackService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        trackService.deleteTrack(id);
        resp.getWriter().println("Operacion de eliminar track ejecutada");
    }
}
