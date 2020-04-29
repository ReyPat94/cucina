package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Utente;
import service.UtenteService;
import service.UtenteServiceImpl;

@WebServlet("/DeleteUser")
public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	try {
	UtenteService uService = new UtenteServiceImpl();
	HttpSession session = request.getSession();
	Utente utente = (Utente) session.getAttribute("myUser");
	String idUtente = utente.getIdUtente();
	uService.cancellaRegistrazioneUtente(idUtente);
	session.invalidate();
	RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
	rd.forward(request, response);
	} catch (Exception e) {
		request.setAttribute("error", e.getMessage());
		RequestDispatcher rd = request.getRequestDispatcher("userpage.jsp");
		rd.forward(request, response);
	}
}
}