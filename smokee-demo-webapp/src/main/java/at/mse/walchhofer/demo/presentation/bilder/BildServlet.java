package at.mse.walchhofer.demo.presentation.bilder;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.mse.walchhofer.demo.business.bilder.boundary.BildService;
import at.mse.walchhofer.demo.business.bilder.entity.Bild;

@WebServlet("/images")
public class BildServlet extends HttpServlet {

    private static final long serialVersionUID = -6483707047856765329L;

    @EJB
    BildService bildService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imageId = request.getParameter("id");
        Bild b = bildService.getById(Long.parseLong(imageId));
        response.setContentLengthLong(b.getSize());
        response.setContentType(b.getMimeType());
        try (ServletOutputStream out  = response.getOutputStream()) {
            out.write(b.getFile());    
        }
    }

}
