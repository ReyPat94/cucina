package servlet;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Edizione;
import service.EdizioneServiceImpl;

@WebServlet("/AggiungiEdizione")
public class AggiungiEdizioneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		String messaggio = "";
		int idCorso = Integer.parseInt(request.getParameter("idCorso"));
		Date dataDiInizio = Date.valueOf(request.getParameter("dataDiInizio"));
		int durata = Integer.parseInt(request.getParameter("durata"));
		String aula = request.getParameter("aula");
		String docente = request.getParameter("docente");
		Edizione ed = new Edizione(idCorso, dataDiInizio, durata, aula, docente);
		messaggio = "Edizione aggiunta con successo";
		try {
			new EdizioneServiceImpl().inserisciEdizione(ed);
		} catch (Exception e) {
			messaggio = e.getMessage();
		} finally {
			request.setAttribute("Messaggio", messaggio);
			RequestDispatcher rdright = request.getRequestDispatcher("/WEB-INF/jsp/adminpage.jsp");
			rdright.forward(request, response);
		}

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}