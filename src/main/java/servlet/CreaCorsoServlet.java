package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Categoria;
import entity.Corso;
import service.CorsoServiceImpl;

@WebServlet("/CreaCorso")
public class CreaCorsoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String messaggio = "";
		HttpSession session = request.getSession();

		try {
			String titolo = request.getParameter("nome");
			int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
			int maxPartecipanti = Integer.parseInt(request.getParameter("maxP"));
			double costo = Double.parseDouble(request.getParameter("costo"));
			String descrizione = request.getParameter("descr");
			Corso corso = new Corso(titolo, idCategoria, maxPartecipanti, costo, descrizione);
			new CorsoServiceImpl().inserisciCorso(corso);

			ArrayList<ArrayList<Corso>> corsi = new ArrayList<ArrayList<Corso>>();

			ArrayList<Categoria> categorie = new CorsoServiceImpl().visualizzaCategorie();
			for (int i = 0; i < categorie.size(); i++) {
				int idCat = categorie.get(i).getIdCategoria();
				ArrayList<Corso> corsCat = new CorsoServiceImpl().visualizzaCorsiPerCategoria(idCat);
				corsi.add(corsCat);
			}

			session.setAttribute("categorie", categorie);
			session.setAttribute("corsi", corsi);
			messaggio = "Nuovo corso [" + titolo + "] creato";

		} catch (Exception e) {
			messaggio = e.getMessage();

		} finally {
			request.setAttribute("messaggio", messaggio);
			RequestDispatcher rdright = request.getRequestDispatcher("/WEB-INF/jsp/adminpage.jsp");
			rdright.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
