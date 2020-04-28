package validator;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

public class Validatore {

	private static ResourceBundle bundle = ResourceBundle.getBundle("risorse/info");

	/*
	 * regole di validazione per la registrazione dell'utente
	 */
	public static List<ErroreValidazione> validazioneUtente(HttpServletRequest request) {
		List<ErroreValidazione> lista = new ArrayList<>();

		String idUtente = request.getParameter("idUtente");
		if (idUtente == null || idUtente.length() == 0)
			lista.add(new ErroreValidazione("idUtente", "idUtente " + bundle.getString("error.required")));

		String password = request.getParameter("password");
		if (password == null || password.length() == 0)
			lista.add(new ErroreValidazione("password", "password " + bundle.getString("error.required")));
		else if (password.length() < 8)
			lista.add(new ErroreValidazione("password", bundle.getString("error.minlength") + " 8"));
		else if (password.length() > 20)
			lista.add(new ErroreValidazione("password", bundle.getString("error.maxlength") + " 20"));

		String email = request.getParameter("email");
		if (email == null || email.length() == 0)
			lista.add(new ErroreValidazione("email", "email " + bundle.getString("error.required")));
		else if (email.length() < 8)
			lista.add(new ErroreValidazione("email", bundle.getString("error.minlength") + " 8"));
		else if (email.length() > 40)
			lista.add(new ErroreValidazione("email", bundle.getString("error.maxlength") + " 40"));
		else if (!email.contains("@"))
			lista.add(new ErroreValidazione("email", "email: " + bundle.getString("error.formato.errato")));
		return lista;
	}

}
