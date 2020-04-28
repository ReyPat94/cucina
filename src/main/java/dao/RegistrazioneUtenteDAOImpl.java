package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import entity.Utente;
import exceptions.ConnessioneException;

public class RegistrazioneUtenteDAOImpl implements RegistrazioneUtenteDAO {
	private static final String SET_USER = "INSERT INTO registrati (id_utente,password, nome, cognome, dataNascita,email,telefono)"
			+ " values(?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_USER = "UPDATE registrati SET id_utente = ?, password = ?, nome = ?, cognome = ?,"
			+ "dataNascita = ?, email = ?, telefono = ? " + "WHERE id_utente= ?";
	private static final String DELETE_USER = "DELETE FROM registrati" + "WHERE id_utente= ?";
	private static final String GET_ALL = "SELECT id_utente, password, nome, cognome, dataNascita, "
			+ "email,telefono FROM registrati";

	private static final String GET_USER = "SELECT id_utente, password, nome, cognome, "
			+ "dataNascita,email,telefono FROM registrati " + "WHERE id_utente = ?";
	private Connection conn;

	public RegistrazioneUtenteDAOImpl() throws ConnessioneException {
		conn = SingletonConnection.getInstance();
	}

	
	/*
	 * registrazione di un nuovo utente alla scuola di formazione se l'utente gi�
	 * esiste si solleva una eccezione
	 */
	@Override
	public void insert(Utente u) throws SQLException {
		try (PreparedStatement prepStmt = conn.prepareStatement(SET_USER)) {
			prepStmt.setString(1, u.getIdUtente());
			prepStmt.setString(2, u.getPassword());
			prepStmt.setString(3, u.getNome());
			prepStmt.setString(4, u.getCognome());
			Date date = new java.sql.Date(u.getDataNascita().getTime());
			prepStmt.setDate(5, date);
			prepStmt.setString(6, u.getEmail());
			prepStmt.setString(7, u.getTelefono());

			prepStmt.executeUpdate();
			int result = prepStmt.executeUpdate();
			if (result == 0) {
				throw new SQLException("Utente già registrato");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return;
	}

	/*
	 * modifica di tutti i dati di un utente l'utente viene individuato dal suo
	 * idUtente se l'utente non esiste si solleva una exception
	 */
	@Override
	public void update(Utente u) throws SQLException {

		try (PreparedStatement prepStmt = conn.prepareStatement(UPDATE_USER)) {
			prepStmt.setString(1, u.getIdUtente());
			prepStmt.setString(2, u.getPassword());
			prepStmt.setString(3, u.getNome());
			prepStmt.setString(4, u.getCognome());
			Date date = new java.sql.Date(u.getDataNascita().getTime());
			prepStmt.setDate(5, date);
			prepStmt.setString(6, u.getEmail());
			prepStmt.setString(7, u.getTelefono());
			prepStmt.setString(8, u.getIdUtente());

			int result = prepStmt.executeUpdate();
			if (result == 0) {
				throw new SQLException("Utente non esiste");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return;
	}

	/*
	 * cancellazione di un singolo utente l'utente si pu� cancellare solo se non �
	 * correlato ad altri dati se l'utente non esiste o non � cancellabile si
	 * solleva una eccezione
	 */
	@Override
	public void delete(Utente u) throws SQLException {
		try (PreparedStatement prepStmt = conn.prepareStatement(DELETE_USER)) {
			prepStmt.setString(1, u.getIdUtente());
			int result = prepStmt.executeUpdate();
			if (result == 0) {
				throw new SQLException("Utente non cancellabile");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	/*
	 * lettura di tutti gli utenti registrati se non ci sono utenti registrati il
	 * metodo ritorna una lista vuota
	 */
	@Override
	public ArrayList<Utente> select() throws SQLException {
		ArrayList<Utente> results = new ArrayList<>();

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(GET_ALL)) {
			while (rs.next()) {
				Date dataNascita = new java.sql.Date(rs.getDate(5).getTime());
				Utente utente = new Utente(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						dataNascita, rs.getString(6), rs.getString(7), false);
				results.add(utente);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return results;
	}

	/*
	 * lettura dei dati di un singolo utente se l'utente non esiste si solleva una
	 * eccezione
	 */
	@Override
	public Utente select(Utente u) throws SQLException {
		Utente results = new Utente();
		try (PreparedStatement prepStmt = conn.prepareStatement(GET_USER)) {
			prepStmt.setString(1, u.getIdUtente());
			try (ResultSet rs = prepStmt.executeQuery()) {

				if (rs.next()) {
					Date dataNascita = new java.sql.Date(rs.getDate(5).getTime());
					results = new Utente(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							dataNascita, rs.getString(6), rs.getString(7), false);

				} else {
					throw new SQLException("L'utente non esiste");
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}

		return results;

	}

	@Override
	public void close() throws IOException {
		try {
			conn.close();
		} catch (SQLException se) {
			throw new IllegalArgumentException("Failed close connection" + se.getMessage());
		}

	}

}
