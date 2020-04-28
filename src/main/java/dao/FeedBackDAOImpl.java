package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Categoria;
import entity.Feedback;
import exceptions.ConnessioneException;

public class FeedBackDAOImpl implements FeedbackDAO {

	private Connection conn;

	public FeedBackDAOImpl() throws ConnessioneException {
		conn = SingletonConnection.getInstance();
	}

	/*
	 * inserimento di un singolo feedbak relativo ad una edizione di un corso da
	 * parte di un utente se un utente ha gi� inserito un feedback per una certa
	 * edizione si solleva una eccezione
	 */
	@Override
	public void insert(Feedback feedback) throws SQLException {

		PreparedStatement stmt0 = conn
				.prepareStatement("SELECT * FROM feedback WHERE id_edizione = ? and id_utente = ?");
		stmt0.setInt(1, feedback.getIdEdizione());
		stmt0.setString(2, feedback.getIdUtente());

		ResultSet rs = stmt0.executeQuery();

		if (rs.next()) {
			throw new SQLException(
					feedback.getIdUtente() + " hai già inserito un feedback per " + feedback.getIdEdizione());
		} else {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO feedback (id_edizione, id_utente, descrizione, voto) values (?, ?, ?, ?)");
			stmt.setInt(1, feedback.getIdEdizione());
			stmt.setString(2, feedback.getIdUtente());
			stmt.setString(3, feedback.getDescrizione());
			stmt.setInt(4, feedback.getVoto());
			stmt.executeUpdate();
		}

	}

	/*
	 * modifica di tutti i dati di un singolo feedback un feedback viene individuato
	 * attraverso l'idFeedback se il feedback non esiste si solleva una eccezione
	 */
	@Override
	public void update(Feedback feedback) throws SQLException {

		PreparedStatement stmt = conn
				.prepareStatement("UPDATE feedback SET id_edizione = ?, id_utente = ?, descrizione = ?, voto = ? "
						+ "WHERE id_feedback = ?");
		stmt.setInt(1, feedback.getIdEdizione());
		stmt.setString(2, feedback.getIdUtente());
		stmt.setString(3, feedback.getDescrizione());
		stmt.setInt(4, feedback.getVoto());

		int result = stmt.executeUpdate();
		if (result == 0) {
			throw new SQLException("feedback " + feedback.getIdFeedback() + "non presente, UPDATE non eseguibile");
		}

	}

	/*
	 * cancellazione di un feedback se il feedback non esiste si solleva una
	 * eccezione
	 */
	@Override
	public void delete(int idFeedback) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("DELETE FROM feedback WHERE id_feedback = ?");
		stmt.setInt(1, idFeedback);
		int result = stmt.executeUpdate();
		if (result == 0) {
			throw new SQLException("Il feedback " + idFeedback + " non esiste");
		}

	}

	/*
	 * lettura di un singolo feedback scritto da un utente per una certa edizione se
	 * il feedback non esiste si solleva una eccezione
	 */
	@Override
	public Feedback selectSingoloFeedback(String idUtente, int idEdizione) throws SQLException {

		PreparedStatement stmt = conn
				.prepareStatement("SELECT * FROM feedback WHERE id_edizione = ? and id_utente = ?");
		stmt.setInt(1, idEdizione);
		stmt.setString(2, idUtente);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			String descr = rs.getString("descrizione");
			int voto = rs.getInt("voto");
			return new Feedback(idEdizione, idUtente, descr, voto);
		} else {
			throw new SQLException(idUtente + " non ha scritto feedback per l'edizione " + idEdizione);
		}
	}

	/*
	 * lettura di tutti i feedback di una certa edizione se non ci sono feedback o
	 * l'edizione non esiste si torna una lista vuota
	 */
	@Override
	public ArrayList<Feedback> selectPerEdizione(int idEdizione) throws SQLException {

		ArrayList<Feedback> feedbacks = new ArrayList<Feedback>();

		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM feedback WHERE id_edizione = ?");
		stmt.setInt(1, idEdizione);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			String idUtente = rs.getString("id_utente");
			String descr = rs.getString("descrizione");
			int voto = rs.getInt("voto");
			feedbacks.add(new Feedback(idEdizione, idUtente, descr, voto));
		}

		return feedbacks;
	}
	
	@Override
	public Feedback getFeedbackByID(int feedbackID) throws SQLException {

		Feedback feedback = new Feedback();

		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM feedback WHERE id_feedback = ?");
		stmt.setInt(1, feedbackID);
		ResultSet rs = stmt.executeQuery();

		feedback = new Feedback(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5));

		return feedback;
		
	}

	/*
	 * lettura di tutti i feedback scritti da un certo utente se non ci sono
	 * feedback o l'utente non esiste si torna una lista vuota
	 */
	@Override
	public ArrayList<Feedback> selectPerUtente(String idUtente) throws SQLException {

		ArrayList<Feedback> feedbacks = new ArrayList<Feedback>();

		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM feedback WHERE id_utente = ?");
		stmt.setString(1, idUtente);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			int idEdizione = rs.getInt("id_edizione");
			String descr = rs.getString("descrizione");
			int voto = rs.getInt("voto");
			feedbacks.add(new Feedback(idEdizione, idUtente, descr, voto));
		}

		return feedbacks;
	}

	/*
	 * lettura di tutti i feedback scritti per un certo corso (nota: non edizione ma
	 * corso) se non ci sono feedback o il corso non esiste si torna una lista vuota
	 */
	@Override
	public ArrayList<Feedback> selectFeedbackPerCorso(int idCorso) throws SQLException {
		ArrayList<Feedback> feedbacks = new ArrayList<Feedback>();

		PreparedStatement stmt = conn.prepareStatement(	"SELECT f.id_feedback, f.id_edizione, f.id_utente, f.descrizione, f.voto "
				+ "FROM feedback f JOIN calendario c USING (id_edizione) WHERE c.id_corso = ?");
		stmt.setInt(1, idCorso);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			int idEdizione = rs.getInt("id_edizione");
			String idUtente = rs.getString("id_utente");
			String descr = rs.getString("descrizione");
			int voto = rs.getInt("voto");
			feedbacks.add(new Feedback(idEdizione, idUtente, descr, voto));
		}

		return feedbacks;

	}

	@Override
	public void close() throws IOException {
		try {
			conn.close();
		} catch (SQLException se) {
			throw new IllegalStateException("Failed to close connection" + se.getMessage());
		}
	}

}
