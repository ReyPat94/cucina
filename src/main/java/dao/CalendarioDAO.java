package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import entity.Edizione;
import exceptions.ConnessioneException;


public interface CalendarioDAO {

	void insert(Edizione e) throws SQLException;
	void delete(int idEdizione) throws SQLException;
	void update(Edizione e) throws SQLException;
	Edizione selectEdizione(int idEdizione) throws SQLException;
	ArrayList<Edizione> select(int idCaregotia) throws SQLException;
	ArrayList<Edizione> select(int idCaregotia, boolean future) throws SQLException;
	ArrayList<Edizione> select() throws SQLException;
	ArrayList<Edizione> select(boolean future) throws SQLException;
	ArrayList<Edizione> select(java.sql.Date da, java.sql.Date a) throws SQLException;
	ArrayList<Edizione> select(String idUtente) throws SQLException;
	ArrayList<Edizione> select(String idUtente, boolean future) throws SQLException;
	int maxPartecipanti(int idEdizione) throws SQLException;
	ArrayList<Edizione> selectByCorso(int idCorso) throws SQLException;	
}