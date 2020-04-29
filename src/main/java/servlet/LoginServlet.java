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
import dao.CatalogoDAO;
import dao.CatalogoDAOImpl;
import dao.IscrizioneUtenteDAO;
import dao.IscrizioneUtenteDAOImpl;
import entity.Corso;
import entity.Edizione;
import entity.Utente;
import exceptions.ConnessioneException;
import exceptions.DAOException;
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
					Utente admin = daoA.select(username);

					if (admin.getPassword().equals(password)) {
						session.setAttribute("admin", admin);

						RequestDispatcher rdright = request.getRequestDispatcher("adminpage.jsp");
						rdright.forward(request, response);
					} else {
						String errore = "Password non corretta";
						request.setAttribute("errore", errore);

						RequestDispatcher rdwrong = request.getRequestDispatcher("login.jsp");
						rdwrong.forward(request, response);
					}
				
			} catch (ConnessioneException | SQLException e) {
				request.setAttribute("errore", e.getMessage());
				RequestDispatcher rdwrong = request.getRequestDispatcher("login.jsp");
				rdwrong.forward(request, response);
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

				RequestDispatcher rdright = request.getRequestDispatcher("userpage.jsp");
				rdright.forward(request, response);
//			}
		} catch (Exception e) {
				request.setAttribute("errore", e.getMessage());
			
				RequestDispatcher rdwrong = request.getRequestDispatcher("login.jsp");
				rdwrong.forward(request, response);
			}
		}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
