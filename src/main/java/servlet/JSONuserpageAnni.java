package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

import dao.CalendarioDAO;
import dao.CalendarioDAOImpl;
import entity.Edizione;
import exceptions.ConnessioneException;

@WebServlet("/JSONuserpageAnni")
public class JSONuserpageAnni {
	private static final long serialVersionUID = 1L;

	@Resource(name = "jdbc/cucina")
	private DataSource ds;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ArrayList<String> anni = new ArrayList<>(); //set

		try (CalendarioDAO daoC = new CalendarioDAOImpl()) {
			ArrayList<Edizione> edizioni = daoC.select();
			for (Edizione edit : edizioni) {
				String anno = Integer.toString(edit.getDataInizio().getYear());
				anni.add(anno);
			}
			String JsonYears = new Gson().toJson(anni);

			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(JsonYears);
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
