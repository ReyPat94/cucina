package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AmministratoreDAO;
import dao.AmministratoreDAOImpl;
import dao.IscrizioneUtenteDAO;
import dao.IscrizioneUtenteDAOImpl;
import entity.Categoria;
import entity.Corso;
import entity.Edizione;
import entity.Utente;
import exceptions.ConnessioneException;
import exceptions.DAOException;
import service.CorsoService;
import service.CorsoServiceImpl;
import service.UtenteServiceImpl;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("usrn");
		String password = request.getParameter("pswd");
		String accesstype = request.getParameter("accesstype");
		HttpSession session = request.getSession();

		if (accesstype.equals("admin")) {
			
				try (AmministratoreDAO daoA = new AmministratoreDAOImpl()) {
					CorsoService cs = new CorsoServiceImpl();
					Utente admin = daoA.select(username);
								
					ArrayList<ArrayList<Corso>> corsi = new ArrayList<ArrayList<Corso>>();
					try {
						ArrayList<Categoria> categorie = cs.visualizzaCategorie();
						for (int i = 0; i < categorie.size(); i++) {
							int idCategoria = categorie.get(i).getIdCategoria();
							ArrayList<Corso> corsCat =	cs.visualizzaCorsiPerCategoria(idCategoria);
							corsi.add(corsCat);					
						}
						
						session.setAttribute("categorie", categorie);
						session.setAttribute("corsi", corsi);
					} catch (Exception e) {
						request.setAttribute("errore", e.getMessage());
						RequestDispatcher rdwrong = request.getRequestDispatcher("WEB-INF/jsp/login.jsp");
						rdwrong.forward(request, response);
						return;
					}


					if (admin.getPassword().equals(password)) {
						session.setAttribute("admin", admin);

						RequestDispatcher rdright = request.getRequestDispatcher("WEB-INF/jsp/adminpage.jsp");
						rdright.forward(request, response);
						return;
					} else {
						String errore = "Password non corretta";
						request.setAttribute("errore", errore);

						RequestDispatcher rdwrong = request.getRequestDispatcher("WEB-INF/jsp/login.jsp");
						rdwrong.forward(request, response);
						return;
					}
				
			} catch (Exception e) {
				request.setAttribute("errore", e.getMessage());
				RequestDispatcher rdwrong = request.getRequestDispatcher("WEB-INF/jsp/login.jsp");
				rdwrong.forward(request, response);
				return;
			}

		}

		try {
			Utente matchingUtente = new UtenteServiceImpl().checkCredenziali(username, password);
			session.setAttribute("utente", matchingUtente);
			
			IscrizioneUtenteDAO daou = new IscrizioneUtenteDAOImpl();
			ArrayList<Edizione> edizioni = daou.selectIscrizioniUtente(matchingUtente.getIdUtente());
			session.setAttribute("leMieEdizioni", edizioni);

//			try (CatalogoDAO daoc = new CatalogoDAOImpl()) {
//				ArrayList<Corso> catalogo = daoc.select();
//				session.setAttribute("catalogo", catalogo);

				RequestDispatcher rdright = request.getRequestDispatcher("WEB-INF/jsp/userpage.jsp");
				rdright.forward(request, response);
				return;
//			}
		} catch (Exception e) {
				request.setAttribute("errore", e.getMessage());
			
				RequestDispatcher rdwrong = request.getRequestDispatcher("WEB-INF/jsp/login.jsp");
				rdwrong.forward(request, response);
				return;
			}
		}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
