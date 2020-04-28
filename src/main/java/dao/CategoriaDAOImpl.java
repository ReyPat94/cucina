package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.Categoria;
import entity.Utente;
import exceptions.ConnessioneException;

public class CategoriaDAOImpl implements CategoriaDAO {

	private Connection conn;

	public CategoriaDAOImpl() throws ConnessioneException {
		conn = SingletonConnection.getInstance();
	}

	/*
	 * inserimento di una nuova categoria
	 * 
	 */
	@Override
	public void insert(String descrizione) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO categoria (descrizione) values (?)");
		stmt.setString(1, descrizione);
		stmt.executeUpdate();
	}

	/*
	 * modifica del nome di una categoria. la categoria viene individuata in base al
	 * idCategoria se la categoria non esiste si solleva una eccezione
	 */
	@Override
	public void update(Categoria c) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("UPDATE categoria SET descrizione = ? WHERE id_categoria = ?");
		stmt.setString(1, c.getDescrizione());
		stmt.setInt(2, c.getIdCategoria());
		int result = stmt.executeUpdate();
		if (result == 0) {
			throw new SQLException("categoria " + c.getIdCategoria() + "non presente, UPDATE non eseguibile");
		}
	}

	/*
	 * cancellazione di una singola categoria una categoria si pu� cancellare solo
	 * se non ci sono dati correlati se la categoria non esiste o non � cancellabile
	 * si solleva una eccezione
	 */
	@Override
	public void delete(int idCategoria) throws SQLException {

		PreparedStatement stmt = conn.prepareStatement("DELETE FROM categoria WHERE id_categoria = ?");
		stmt.setInt(1, idCategoria);
		int result = stmt.executeUpdate();
		if (result == 0) {
			throw new SQLException("la categoria " + idCategoria + " non è cancellabile, o non esiste o sono presenti dati correlati");
		}
	}

	/*
	 * lettura di una singola categoria in base al suo id se la categoria non esiste
	 * si solleva una eccezione
	 */
	@Override
	public Categoria select(int idCategoria) throws SQLException {

		PreparedStatement stmt = conn.prepareStatement("SELECT id_categoria, descrizione FROM categoria WHERE id_categoria = ?");
		stmt.setInt(1, idCategoria);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			int id = rs.getInt(1);
			String descr = rs.getString(2);
			return new Categoria(id, descr);
		} else {
			throw new SQLException("Cateogoria " + idCategoria + " non presente.");
		}
	}

	/*
	 * lettura di tutte le categorie se non vi sono categoria il metodo ritorna una
	 * lista vuota
	 */
	@Override
	public ArrayList<Categoria> select() throws SQLException {
		
		ArrayList<Categoria> categorie = new ArrayList<Categoria>();
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM categorie");
		
		while (rs.next()) {
			int id = rs.getInt(1);
			String descr = rs.getString(2);
			categorie.add(new Categoria(id, descr));
		}
		
		return categorie;
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
