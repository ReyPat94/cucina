package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CategoriaDAOImpl;
import exceptions.ConnessioneException;
import service.CorsoService;
import service.CorsoServiceImpl;

public class servletCreaCategoria {

	@WebServlet("/CreaCategoria")
	public class AccessoPagina extends HttpServlet {
		private static final long serialVersionUID = 1L;

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			String messaggio = "";
			
			try {
			String descrizione = request.getParameter("descr");
			new CorsoServiceImpl().creaNuovaCategoria(descrizione);
			
			messaggio = "Nuova categoria [" + descrizione + "] creata";
			
			} catch (Exception e) {
			 messaggio = e.getMessage();
			 
			}finally {
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

}
