package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Corso;
import service.CorsoServiceImpl;

@WebServlet("/ModificaCorso")
public class ModificaCorsoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String messaggio = "";
		Corso corso = new Corso();

		try {
			if (request.getParameter("set")!= null && request.getParameter("set").equals("first")) {
				int codiceCorso = Integer.parseInt(request.getParameter("corsoCodice"));
				corso = new CorsoServiceImpl().visualizzaCorso(codiceCorso);
				request.setAttribute("corso", corso);
				RequestDispatcher rdright = request.getRequestDispatcher("/WEB-INF/jsp/gestioneCorso.jsp");
				rdright.forward(request, response);
			} else {
				int codice = Integer.parseInt(request.getParameter("corsoCodice"));
				String titolo = request.getParameter("nome");
				int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
				int maxPartecipanti = Integer.parseInt(request.getParameter("maxP"));
				double costo = Double.parseDouble(request.getParameter("costo"));
				String descrizione = request.getParameter("descr");
				corso = new Corso(codice, titolo, idCategoria, maxPartecipanti, costo, descrizione);
				new CorsoServiceImpl().modificaDatiCorso(corso);
				messaggio = "Corso [" + codice + " - " + titolo + "] modificato con successo";
				request.setAttribute("corso", corso);
				request.setAttribute("messaggio", messaggio);
				RequestDispatcher rdright = request.getRequestDispatcher("/WEB-INF/jsp/adminpage.jsp");
				rdright.forward(request, response);
			}			

		} catch (Exception e) {			
			int codice = Integer.parseInt(request.getParameter("corsoCodice"));
			String titolo = request.getParameter("nome");
			int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
			int maxPartecipanti = Integer.parseInt(request.getParameter("maxP"));
			double costo = Double.parseDouble(request.getParameter("costo"));
			String descrizione = request.getParameter("descr");
			corso = new Corso(codice, titolo, idCategoria, maxPartecipanti, costo, descrizione);
			messaggio = "Errore: " + e.getMessage();
			request.setAttribute("corso", corso);
			request.setAttribute("messaggio", messaggio);
			RequestDispatcher rdright = request.getRequestDispatcher("/WEB-INF/jsp/gestioneCorso.jsp");
			rdright.forward(request, response);
			
		} 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
