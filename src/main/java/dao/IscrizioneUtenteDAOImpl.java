package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Edizione;
import entity.Utente;
import exceptions.ConnessioneException;

public class IscrizioneUtenteDAOImpl implements IscrizioneUtenteDAO {
	private static final String SET_ISCRIZIONE = "INSERT INTO iscritti (id_edizione, id_utente)"
			+ " values(?, ?)";
	private static final String DELETE_ISCRIZIONE = "DELETE FROM iscritti" + "WHERE id_edizione = ? and id_utente= ?";
	private static final String GET_UTENTE_EDIZIONI = "SELECT id_edizione,  id_corso, dataInizio, durata"
			+ "aula, docente FROM iscritti "
			+ "JOIN calendario USING(id_edizione)"
			+ " WHERE id_utente= ?";
	private static final String GET_EDIZIONE_UTENTI = "SELECT id_utente,  password, nome, cognome"
			+ "dataNascita, email, telefono FROM registrati "
			+ "JOIN calendario USING(id_edizione)"
			+ " WHERE id_edizione = ?";
	private static final String COUNT_UTENTI_EDIZIONE = "SELECT COUNT(id_utente) FROM iscritti "
			+ "GROUP BY id_edizione WHERE id_edizione = ?";
	
	
	private Connection conn;

	public IscrizioneUtenteDAOImpl() throws ConnessioneException{
		conn = SingletonConnection.getInstance();
	}
	
	/*
	 * iscrizione di un certo utente ad una certa edizione di un corso.
	 * sia l'utente che l'edizione devono gi� essere stati registrati in precedenza
	 * se l'utente e/o l'edizione non esistono (WAT) o l'utente � gi� iscritto a quella edizione si solleva una eccezione
	 */
	@Override
	public void iscriviUtente(int idEdizione, String idUtente) throws SQLException {

		try (PreparedStatement prepStmt = conn.prepareStatement(SET_ISCRIZIONE)) {
			prepStmt.setInt(1, idEdizione);
			prepStmt.setString(2, idUtente);

			prepStmt.executeUpdate();
			int result = prepStmt.executeUpdate();
			if (result == 0) {
				throw new SQLException("Utente già iscritto");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return;

	}

	/*
	 * cancellazione di una iscrizione ad una edizione
	 * nota: quando si cancella l'iscrizione, sia l'utente che l'edizione non devono essere cancellati
	 * se l'utente e/o l'edizione non esistono si solleva una eccezione
	 */
	@Override
	public void cancellaIscrizioneUtente(int idEdizione, String idUtente) throws SQLException {
		try (PreparedStatement prepStmt = conn.prepareStatement(DELETE_ISCRIZIONE)) {
			prepStmt.setInt(1, idEdizione);
			prepStmt.setString(2, idUtente);
			
			int result = prepStmt.executeUpdate();
			if (result == 0) {
				throw new SQLException("Iscrizione non esistente");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}

	}

	/*
	 * lettura di tutte le edizioni a cui � iscritto un utente
	 * se l'utente non esiste o non � iscritto a nessuna edizione si torna una lista vuota
	 */
	@Override
	public ArrayList<Edizione> selectIscrizioniUtente(String idUtente) throws SQLException {
		ArrayList<Edizione> results = new ArrayList<>();

		try (PreparedStatement prepStmt = conn.prepareStatement(GET_UTENTE_EDIZIONI)) {
			prepStmt.setString(1, idUtente);
			try (ResultSet rs = prepStmt.executeQuery()) {
				while (rs.next()) {
					Edizione edizione = new Edizione(rs.getInt(1), rs.getInt(2), rs.getDate(3), 
							rs.getInt(4), rs.getString(5), rs.getString(6));
					results.add(edizione);
				}
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
		}

		return results;
	}
	
	/*
	 * lettura di tutti gli utenti iscritti ad una certa edizione
	 * se l'edizione non esiste o non vi sono utenti iscritti si torna una lista vuota
	 */
	@Override
	public ArrayList<Utente> selectUtentiPerEdizione(int idEdizione) throws SQLException {
		ArrayList<Utente> results = new ArrayList<>();

		try (PreparedStatement prepStmt = conn.prepareStatement(GET_EDIZIONE_UTENTI)) {
			prepStmt.setInt(1, idEdizione);
			try (ResultSet rs = prepStmt.executeQuery()) {
				while (rs.next()) {
					java.util.Date dataNascita = new java.util.Date(rs.getDate(5).getTime());
					Utente utente = new Utente(rs.getString(1), rs.getString(2), rs.getString(3), 
							rs.getString(4), dataNascita, rs.getString(6), rs.getString(7), false);
					results.add(utente);
				}
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
		}

		return results;
	}

	/*
	 * ritorna il numero di utenti iscritti ad una certa edizione
	 */
	@Override
	public int getNumeroIscritti(int idEdizione) throws SQLException {
		int count = 0;
		
		try (PreparedStatement prepStmt = conn.prepareStatement(COUNT_UTENTI_EDIZIONE)) {
			prepStmt.setInt(1, idEdizione);
			try (ResultSet rs = prepStmt.executeQuery()) {
				if (rs.next()) {
					count = rs.getInt(1);
				}
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
		}

		return count;
	}

}
