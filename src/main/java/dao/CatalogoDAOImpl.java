package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Categoria;
import entity.Corso;
import entity.Edizione;
import entity.Feedback;
import exceptions.ConnessioneException;

public class CatalogoDAOImpl implements CatalogoDAO {

	private static final String GET_CORSI_CATEGORIA = "SELECT id_corso, titolo, id_categoria, numeroMaxPartecipanti, costo, descrizione "
			+ "FROM catalogo JOIN categoria USING(id_categoria) WHERE id_corso = ?";

	private Connection conn;

	public CatalogoDAOImpl() throws ConnessioneException {
		conn = SingletonConnection.getInstance();
	}

	/*
	 * registrazione di un nuovo corso nel catalogo dei corsi
	 */
	@Override
	public void insert(Corso corso) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"INSERT into catalogo(id_corso, titolo, id_categoria, numeroMaxPartecipanti, costo, descrizione) values( ?, ?, ?, ?, ?, ?)");
		stmt.setInt(1, corso.getCodice());
		stmt.setString(2, corso.getTitolo());
		stmt.setInt(3, corso.getIdCategoria());
		stmt.setInt(4, corso.getMaxPartecipanti());
		stmt.setDouble(5, corso.getCosto());
		stmt.setString(6, corso.getDescrizione());
		stmt.executeUpdate();
	}

	/*
	 * modifica di tutti i dati di un corso nel catalogo dei corsi il corso viene
	 * individuato in base al idCorso se il corso non esiste si solleva una
	 * eccezione
	 */
	@Override
	public void update(Corso corso) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"UPDATE catalogo SET titolo = ?, id_categoria = ?,  numeroMaxPartecipanti = ?,  costo = ?,  descrizione = ? WHERE id_corso = ?");
		ArrayList<Corso> allcourses = select();
		if (allcourses.contains(corso)) {
			stmt.setInt(6, corso.getCodice());
			stmt.setString(1, corso.getTitolo());
			stmt.setInt(2, corso.getIdCategoria());
			stmt.setInt(3, corso.getMaxPartecipanti());
			stmt.setDouble(4, corso.getCosto());
			stmt.setString(5, corso.getDescrizione());
		} else {
			throw new SQLException("Il corso che vuoi UPDATE non esiste.");
		}
	}

	/*
	 * cancellazione di un nuovo corso nel catalogo dei corsi questo potr� essere
	 * cancellato solo se non vi sono edizioni di quel corso o qualsiasi altro
	 * legame con gli altri dati. Se il corso non esiste si solleva una eccezione Se
	 * non � cancellabile si solleva una eccezione
	 */
	@Override
	public void delete(int idCorso) throws SQLException, Exception {
		try {
			Corso corsoCheck = select(idCorso);
		} catch (SQLException se) {
			throw new SQLException("Cannot delete course that doesn't exist." + se.getMessage());
		}
		PreparedStatement stmt_select = conn.prepareStatement("SELECT id_corso FROM calendario WHERE id_corso = ?");
		stmt_select.setInt(1, idCorso);
		ResultSet rs = stmt_select.executeQuery();
		if (rs.next()) {
			throw new Exception("DELETE operation NOT PERMITTED: past editions exist.");
		} else {
			PreparedStatement stmt_delete = conn.prepareStatement("DELETE FROM catalogo WHERE id_corso = ?");
			stmt_delete.setInt(1, idCorso);
			stmt_delete.executeUpdate();
		}
	}

	/*
	 * lettura di tutti i corsi dal catalogo se non ci sono corsi nel catalogo il
	 * metodo torna una lista vuota
	 */
	@Override
	public ArrayList<Corso> select() throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM catalogo");
		ResultSet rs = pstmt.executeQuery();
		ArrayList<Corso> result = new ArrayList<>();
		while (rs.next()) {
			Corso corso = new Corso(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5),
					rs.getString(6));
			result.add(corso);
		}
		return result;
	}

	/*
	 * lettura di un singolo corso dal catalogo dei corsi se il corso non � presente
	 * si solleva una eccezione
	 */
	@Override
	public Corso select(int idCorso) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM catalogo WHERE id_corso = ?");
		pstmt.setInt(1, idCorso);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return new Corso(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5),
					rs.getString(6));
		} else {
			throw new SQLException("The course with the ID provided doesn't exist");
		}
	}

	@Override
	public ArrayList<Corso> getCorsiCategoria(int idCategoria) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(GET_CORSI_CATEGORIA);
		pstmt.setInt(1, idCategoria);
		ResultSet rs = pstmt.executeQuery();
		ArrayList<Corso> result = new ArrayList<>();
		while (rs.next()) {
			Corso corso = new Corso(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5),
					rs.getString(6));
			result.add(corso);
		}
		return result;
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
