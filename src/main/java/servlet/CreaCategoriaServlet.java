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

	@WebServlet("/CreaCategoria")
	public class CreaCategoriaServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			String messaggio = "";
			HttpSession session = request.getSession();
			
			try {
			String descrizione = request.getParameter("descr");
			new CorsoServiceImpl().creaNuovaCategoria(descrizione);
			
			ArrayList<ArrayList<Corso>> corsi = new ArrayList<ArrayList<Corso>>();

			ArrayList<Categoria> categorie = new CorsoServiceImpl().visualizzaCategorie();
			for (int i = 0; i < categorie.size(); i++) {
				int idCat = categorie.get(i).getIdCategoria();
				ArrayList<Corso> corsCat = new CorsoServiceImpl().visualizzaCorsiPerCategoria(idCat);
				corsi.add(corsCat);
			}

			session.setAttribute("categorie", categorie);
			session.setAttribute("corsi", corsi);
			
			messaggio = "Nuova categoria [" + descrizione + "] creata";
			
			} catch (Exception e) {
			 messaggio = e.getMessage();
			 
			}finally {
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
