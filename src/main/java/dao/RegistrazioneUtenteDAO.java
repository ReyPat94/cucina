package dao;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Utente;

public interface RegistrazioneUtenteDAO extends Closeable {

	void insert(Utente u) throws SQLException;
	void update(Utente u) throws SQLException;
	void delete(Utente u) throws SQLException;
	ArrayList<Utente> select() throws SQLException;
	Utente select(Utente u) throws SQLException;
}