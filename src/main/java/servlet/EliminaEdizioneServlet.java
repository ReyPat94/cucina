package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.FeedBackDAOImpl;
import dao.FeedbackDAO;
import dao.IscrizioneUtenteDAO;
import dao.IscrizioneUtenteDAOImpl;
import service.EdizioneService;
import service.EdizioneServiceImpl;

@WebServlet("/EliminaEdizione")
public class EliminaEdizioneServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String message = "";

		try (IscrizioneUtenteDAO daoI = new IscrizioneUtenteDAOImpl();
			FeedbackDAO daoF = new FeedBackDAOImpl()){
			EdizioneService edS = new EdizioneServiceImpl();
			int idEdizione = Integer.parseInt(request.getParameter("edizione"));
			daoF.deleteFeedbacksEdizione(idEdizione);
			daoI.cancellaIscrizioniEdizione(idEdizione);
			edS.cancellaEdizione(idEdizione);
			request.setAttribute("message", "Edizione correttamente eliminata");
		} catch (Exception e) {
			message = e.getMessage();
		}
		request.setAttribute("message", message);
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/adminpage.jsp");
		rd.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
