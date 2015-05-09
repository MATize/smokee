package at.mse.walchhofer.smokee.client;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.mse.walchhofer.smokee.api.security.Constants;

@WebServlet("/smokee-client")
public class ClientServlet extends HttpServlet {

	private static final long serialVersionUID = 7674442208759302569L;
	private static final String BASE_DIR = "/smokee-client";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String apiKey = request.getHeader(Constants.HTTP_HEADER_API_KEY);
		if(apiKey == null || "".equals(apiKey)) {
			apiKey = request.getParameter("apiKey");
		}
		request.setAttribute("servletContext", BASE_DIR);
		request.setAttribute("apiKeyHeaderName", Constants.HTTP_HEADER_API_KEY);
		request.setAttribute("authTokenHeaderName", Constants.HTTP_HEADER_AUTH_TOKEN);
		request.setAttribute("smokerUrl", request.getServletContext().getContextPath()+"/smoker");
		request.setAttribute("apikey", apiKey);
		RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(BASE_DIR+"/jsp/client.jsp");
		requestDispatcher.forward(request, response);
	}
	
}
