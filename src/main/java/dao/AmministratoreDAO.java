package dao;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;
import entity.Utente;

public interface AmministratoreDAO extends Closeable{
	void insert(Utente amministratore) throws SQLException;
	void update(Utente amministratore) throws SQLException;
	void delete(String idAmministratore) throws SQLException;
	List<Utente> select() throws SQLException;
	Utente select(String idAmministratore) throws SQLException;
}