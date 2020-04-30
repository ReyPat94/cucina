package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dto.CorsoDTO;
import dto.EdizioneDTO;
import entity.Corso;
import service.CorsoService;
import service.CorsoServiceImpl;
import service.EdizioneService;
import service.EdizioneServiceImpl;

@WebServlet("/JSONedizioniAnni")
public class JSONedizioniAnniServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try  {
			EdizioneService es = new EdizioneServiceImpl();
			CorsoService cs = new CorsoServiceImpl();
			int anno = Integer.parseInt(request.getParameter("anno"));
			ArrayList<EdizioneDTO> edizioniDTO = es.visualizzaEdizioniPerAnno(anno);
			for (int i = 0; i <edizioniDTO.size(); i++) {
				Corso corso = cs.visualizzaCorso(edizioniDTO.get(i).getEdizione().getIdCorso());
				edizioniDTO.get(i).getEdizione().setCorso(corso);
			}
			
			String JsonYears = new Gson().toJson(edizioniDTO);

			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(JsonYears);
			out.flush();
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
