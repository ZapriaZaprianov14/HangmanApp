import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/indexRedirect")
public class IndexServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}
}
