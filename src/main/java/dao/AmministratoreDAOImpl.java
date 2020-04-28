package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Utente;
import exceptions.ConnessioneException;

public class AmministratoreDAOImpl implements AmministratoreDAO {

	private Connection conn;

	public AmministratoreDAOImpl() throws ConnessioneException{
		conn = SingletonConnection.getInstance();
	}


	/*
	 * registrazione di un nuovo amministratore.
	 * Se gi� presente si solleva una eccezione
	 */
	@Override
	public void insert(Utente amministratore) throws SQLException{

		PreparedStatement ps=conn.prepareStatement("INSERT INTO amministratori(id_amministratore,password,nome,cognome,dataNascita,email,telefono) VALUES (?,?,?,?,?,?,?)");
		ps.setString(1, amministratore.getIdUtente());
		ps.setString(2, amministratore.getPassword());
		ps.setString(3, amministratore.getNome());
		ps.setString(4, amministratore.getCognome());
		ps.setDate(5, new java.sql.Date(amministratore.getDataNascita().getTime()));
		ps.setString(6, amministratore.getEmail());
		ps.setString(7, amministratore.getTelefono());
		ps.executeUpdate();
		
		// handle exc
	}


	/*
	 * modifica di tutti i dati di un amministratore.
 	 * L'aministratore viene individuato in base al idAmministratore
 	 * Se non esiste viene sollevata una eccezione
	 */
	@Override
	public void update(Utente amministratore) throws SQLException{

		PreparedStatement ps=conn.prepareStatement("UPDATE amministratori SET password=?, nome=?, cognome=?, dataNascita=?, email=?, telefono=? where id_amministratore=?");
		ps.setString(1, amministratore.getPassword());
		ps.setString(2, amministratore.getNome());
		ps.setString(3, amministratore.getCognome());
		ps.setDate(4, new java.sql.Date(amministratore.getDataNascita().getTime()));
		ps.setString(5, amministratore.getEmail());
		ps.setString(6, amministratore.getTelefono());
		ps.setString(7, amministratore.getIdUtente());
		int n = ps.executeUpdate();
		if(n==0)
			throw new SQLException("utente: " + amministratore.getIdUtente() + " non presente");

	}


	/*
	 * cancellazione di un amministratore individuato attraverso il suo idAmministratore.
	 * l'amministratore potr� essere cancellato solo se non legato a nessun altro dato presente sul DB
	 * Se non esiste viene sollevata una eccezione
	 * Se non � cancellabile si solleva una eccezione
	 */
	@Override
	public void delete(String idAmministratore) throws SQLException{

		PreparedStatement ps = conn.prepareStatement("DELETE FROM amministratori WHERE id_amministratore=?");
		ps.setString(1, idAmministratore);
		int n = ps.executeUpdate();
		if(n==0)
			throw new SQLException("utente " + idAmministratore + " non presente o collegato ad altri dati");
	}

	/*
	 * lettura di tutti gli amministratori registrati
	 */
	@Override
	public List<Utente> select() throws SQLException{

		ArrayList<Utente> amministratori = new ArrayList<Utente>(); 

		PreparedStatement ps=conn.prepareStatement("SELECT * FROM amministratori");

		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			String idUtente = rs.getString("id_amministratore");
			String password= rs.getString("password");
			String nome= rs.getString("nome");
			String cognome= rs.getString("cognome");
			Date dataNascita = rs.getDate("dataNascita");
			String email= rs.getString("email");
			String telefono= rs.getString("telefono");

			Utente amministratore = new Utente(idUtente,password,nome,cognome,dataNascita,email,telefono, true);
			amministratori.add(amministratore);
		}

		return amministratori;
		// handle exc
	}


	/*
	 * lettura dei dati di un singolo amministratore in base al suo idAmministratore 
	 * Se non presente si solleva una eccezione
	 */
	@Override
	public Utente select(String idAmministratore) throws SQLException {

		PreparedStatement ps=conn.prepareStatement("SELECT * FROM amministratori where id_amministratore =?");

		ps.setString(1, idAmministratore);

		ResultSet rs = ps.executeQuery();
		Utente amministratore =null;
		if(rs.next()){
			String idUtente = rs.getString("id_amministratore");
			String password= rs.getString("password");
			String nome= rs.getString("nome");
			String cognome= rs.getString("cognome");
			Date dataNascita = rs.getDate("dataNascita");
			String email= rs.getString("email");
			String telefono= rs.getString("telefono");

			amministratore = new Utente(idUtente,password,nome,cognome,dataNascita,email,telefono, true);
			return amministratore;
		}
		else
			throw new SQLException("amministratore: " + idAmministratore + " non presente");
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
