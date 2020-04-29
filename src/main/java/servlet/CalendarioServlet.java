package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.EdizioneDTO;
import service.EdizioneServiceImpl;

@WebServlet("/Calendario")
public class CalendarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int corso = Integer.parseInt(request.getParameter("corso"));
		ArrayList<EdizioneDTO> edizioni;

		try {
			edizioni = new EdizioneServiceImpl().visualizzaEdizioniPerCorso(corso);
			request.setAttribute("calendario", edizioni);
		} catch (Exception e) {
			String messaggio = e.getMessage();
			request.setAttribute("Messaggio", messaggio);
		} finally {
			RequestDispatcher rdright = request.getRequestDispatcher("/WEB-INF/jsp/adminpage.jsp");
			rdright.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
