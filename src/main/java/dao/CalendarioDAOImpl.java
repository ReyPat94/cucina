package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Edizione;
import exceptions.ConnessioneException;



public class CalendarioDAOImpl implements CalendarioDAO {

	private Connection conn;
	private static final String GET_EDIZIONI_CORSO = "select * from calendario, catalogo where calendario.id_corso = catalogo.id_corso and id_corso=?";

	public CalendarioDAOImpl() throws ConnessioneException{
		conn = SingletonConnection.getInstance();
	}

	/*
	 * registrazione di una nuova edizione nel calendario dei corsi
	 */
	@Override
	public void insert(Edizione ed) throws SQLException{

			PreparedStatement ps=conn.prepareStatement("insert into calendario(id_corso,dataInizio,durata,aula,docente) values (?,?,?,?,?)");

			ps.setInt(1, ed.getIdCorso());
			ps.setDate(2, new java.sql.Date(ed.getDataInizio().getTime()));
			ps.setInt(3, ed.getDurata());
			ps.setString(4, ed.getAula());
			ps.setString(5, ed.getDocente());
			ps.executeUpdate();

	}


	/*
	 * cancellazione di una edizione presente nel calendario dei corsi
	 * per cancellare una edizione � necessario prima cancellare le eventuali iscrizioni degli utenti e i feedbacks  
	 * l'edizione viene individuata in base a idEdizione
	 * se l'edizione non � presente si solleva una eccezione
	 */
	@Override
	public void delete(int idEdizione) throws SQLException{
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM iscritti WHERE id_edizione = ?");
		pstmt.setInt(1, idEdizione);
		pstmt.executeUpdate();
		PreparedStatement pstmt1 = conn.prepareStatement("DELETE FROM feedback WHERE id_edizione = ?");
		pstmt1.setInt(1, idEdizione);
		pstmt1.executeUpdate();
		PreparedStatement pstmt2 = conn.prepareStatement("DELETE FROM calendario WHERE id_edizione = ?");
		pstmt2.setInt(1, idEdizione);
		int rs = pstmt2.executeUpdate();
		if(rs==0) throw new SQLException("Selected edition to delete DOESN'T EXIST");
		
	}


	/*
	 * modifica di tutti i dati di una edizione presente nel calendario dei corsi
	 * l'edizione viene individuata in base al idEdizione
	 * se l'edizione non � presente si solleva una eccezione
	 */
	@Override
	public void update(Edizione ed) throws SQLException{
			PreparedStatement ps=conn.prepareStatement("update calendario set dataInizio=?, durata=?, aula=?, docente=? where id_edizione= ?");
			ps.setDate(1,new java.sql.Date(ed.getDataInizio().getTime()));
			ps.setInt(2,ed.getDurata());
			ps.setString(3,ed.getAula());
			ps.setString(4,ed.getDocente());
			ps.setInt(5, ed.getCodice());
			int n = ps.executeUpdate();
			if(n==0) throw new SQLException("edizione " + ed.getCodice() + " non presente");
	}


	/*
	 * lettura di tutte le edizioni di una certa categoria, presenti nel calendario dei corsi
	 * le edizioni vengono individuate in base al idCategoria
	 * se non vi sono edizioni per quella categoria o la categoria non esiste viene ritornata una lista vuota
	 */
	@Override
	public ArrayList<Edizione> select(int idCaregotia) throws SQLException{
			ArrayList<Edizione> edizioni=new ArrayList<Edizione>(); 
			PreparedStatement ps=conn.prepareStatement("select * from calendario, catalogo where calendario.id_corso = catalogo.id_corso and id_categoria=?");
														//questa è una join scritta brutta, c'era già
			ps.setInt(1, idCaregotia);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				int idEdizione=rs.getInt("id_Edizione");
				int idCorso=rs.getInt("id_corso");
				Date dataInizio=rs.getDate("dataInizio");
				int durata=rs.getInt("durata");
				String aula=rs.getString("aula");
				String docente=rs.getString("docente");

				Edizione e=new Edizione(idCorso,dataInizio,durata,aula,docente);
				e.setCodice(idEdizione);

				long dataM = dataInizio.getTime();				  //brutto
				long durataM= durata*86400000L;
				Date dataFine = new Date(dataM+durataM);
			

				if (dataFine.before(new java.util.Date()))        //controlla se l'edizione è conclusa
					e.setTerminata(true);	

				edizioni.add(e);

			}

			return edizioni;

	}
	
	public ArrayList<Edizione> selectByCorso(int idCorso) throws SQLException {
		ArrayList<Edizione> edizioni=new ArrayList<Edizione>(); 
		PreparedStatement ps=conn.prepareStatement(GET_EDIZIONI_CORSO);
													//questa è una join scritta brutta, c'era già
		ps.setInt(1, idCorso);
		ResultSet rs=ps.executeQuery();
		while(rs.next()){
			int idEdizione=rs.getInt("id_Edizione");
			Date dataInizio=rs.getDate("dataInizio");
			int durata=rs.getInt("durata");
			String aula=rs.getString("aula");
			String docente=rs.getString("docente");

			Edizione e=new Edizione(idCorso,dataInizio,durata,aula,docente);
			e.setCodice(idEdizione);

			long dataM = dataInizio.getTime();				  //brutto
			long durataM= durata*86400000L;
			Date dataFine = new Date(dataM+durataM);
		

			if (dataFine.before(new java.util.Date()))        //controlla se l'edizione è conclusa
				e.setTerminata(true);	

			edizioni.add(e);

		}

		return edizioni;

}	


	/*
	 * lettura dei dati di una edizione presente nel calendario dei corsi
	 * l'edizione viene individuata in base al idEdizione
	 * se l'edizione non � presente si solleva una eccezione
	 */
	@Override
	public Edizione selectEdizione(int idEdizione) throws SQLException{
	  
		PreparedStatement ps=conn.prepareStatement("select * from calendario where id_edizione = ?");
		ps.setInt(1, idEdizione);
		ResultSet rs=ps.executeQuery();
		
		if(rs.next()){
			int idCorso=rs.getInt("id_corso");
			Date dataInizio=rs.getDate("dataInizio");
			int durata=rs.getInt("durata");
			String aula=rs.getString("aula");
			String docente=rs.getString("docente");

			Edizione ed = new Edizione(idCorso,dataInizio,durata,aula,docente);
			ed.setCodice(idEdizione);

			long dataM = dataInizio.getTime();
			long durataM= durata*86400000L;
			Date dataFine = new Date(dataM+durataM);

			if (dataFine.before(new java.util.Date()))
				ed.setTerminata(true);
			return ed;
		} else 
			throw new SQLException("edizione " + idEdizione + " non presente");
	  
	  
	}

	/*
	 * lettura di tutte le edizioni presenti nel calendario dei corsi
	 * se non vi sono edizioni registrate viene ritornata una lista vuota
	 */
	@Override
	public ArrayList<Edizione> select()throws SQLException{
	  
		ArrayList<Edizione> edizioni=new ArrayList<Edizione>();
		PreparedStatement ps=conn.prepareStatement("select * from calendario");
		ResultSet rs=ps.executeQuery();
		
		while(rs.next()){
			int idEdizione=rs.getInt("id_Edizione");
			int idCorso=rs.getInt("id_corso");
			Date dataInizio=rs.getDate("dataInizio");
			int durata=rs.getInt("durata");
			String aula=rs.getString("aula");
			String docente=rs.getString("docente");

			Edizione ed = new Edizione(idCorso,dataInizio,durata,aula,docente);
			ed.setCodice(idEdizione);

			long dataM = dataInizio.getTime();
			long durataM= durata*86400000L;
			Date dataFine = new Date(dataM+durataM);

			if (dataFine.before(new java.util.Date()))
				ed.setTerminata(true);
			
			edizioni.add(ed);
		}
		return edizioni;
	  

		
	}

	
	/*
	 * lettura di tutte le edizioni a cui un certo utente � iscritto o � stato iscritto in passato (vale a dire tutte), presenti nel calendario dei corsi
	 * le edizioni vengono individuate in base al idUtente dell'utente
	 * se non vi sono edizioni per quell'utente o l'utente non esiste viene ritornata una lista vuota
	 */
	@Override
	public ArrayList<Edizione> select(String idUtente) throws SQLException{

			ArrayList<Edizione> edizioni=new ArrayList<Edizione>();
			PreparedStatement ps=conn.prepareStatement("select calendario.id_edizione,id_corso,id_utente,dataInizio,durata,aula,docente from calendario,iscritti where calendario.id_edizione=iscritti.id_edizione and iscritti.id_utente=?");

			ps.setString(1, idUtente);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				int idEdizione=rs.getInt("id_Edizione");
				int idCorso=rs.getInt("id_corso");
				Date dataInizio=rs.getDate("dataInizio");
				int durata=rs.getInt("durata");
				String aula=rs.getString("aula");
				String docente=rs.getString("docente");

				Edizione e=new Edizione(idCorso,dataInizio,durata,aula,docente);
				e.setCodice(idEdizione);

				long dataM = dataInizio.getTime();
				long durataM= durata*86400000L;
				Date dataFine = new Date(dataM+durataM);
				java.util.Date d =  new java.util.Date();

				if (dataFine.before(d) )
					e.setTerminata(true);	
				
				edizioni.add(e);
			}

			return edizioni;
	}
	
	/*
	 * leggere tutte le edizioni presenti nel calendario nel range delle date da, a (inclusi)
	 * se non vi sono edizioni in quel range di date viene ritornata una lista vuota
	 */
	@Override
	public ArrayList<Edizione> select(java.sql.Date da, java.sql.Date a) throws SQLException {
		ArrayList<Edizione> result = new ArrayList<>();
		
		PreparedStatement pstmt = conn.prepareStatement("Select * FROM calendiario WHERE dataInizio betweeen ? and ?");
		pstmt.setDate(1, da);
		pstmt.setDate(1, a);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			result.add(new Edizione(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getInt(4), rs.getString(5), rs.getString(6)));
		}
		return result;
	}

	/*
	 * lettura di tutte le edizioni di una certa categoria, presenti nel calendario dei corsi
	 * se future = true, le edizioni lette devono essere solo quelle a partire dalla data in odierna e dell'anno corrente 
	 * se future = false devono essere lette tutte le edizioni
	 * le edizioni vengono individuate in base al idCategoria
	 * se non vi sono edizioni per quella categoria o la categoria non esiste viene ritornata una lista vuota
	 */
	@Override
	public ArrayList<Edizione> select(int idCaregotia, boolean future) throws SQLException {
		ArrayList<Edizione> result = new ArrayList<>();
		PreparedStatement pstmt;
		if(future) {
			pstmt = conn.prepareStatement("SELECT * from calendario JOIN catalogo ON(id_corso) WHERE id_categoria = ? and dataInizio > ?");
			java.util.Date today = new java.util.Date();
			Date todaySQL = new Date(today.getTime());
			pstmt.setDate(2, todaySQL);
		}else {
			pstmt = conn.prepareStatement("SELECT * from calendario JOIN catalogo ON(id_corso) WHERE id_categoria = ?");	
		}
		pstmt.setInt(1, idCaregotia);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
		result.add(new Edizione(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getInt(4), rs.getString(5), rs.getString(6)));
		}
		return result;
	}

	/*
	 * lettura di tutte le edizioni presenti nel calendario dei corsi
	 * se future = true, le edizioni lette devono essere solo quelle a partire dalla data in odierna e dell'anno corrente 
	 * se future = false devono essere lette tutte le edizioni
	 * se non vi sono edizioni viene ritornata una lista vuota
	 */
	@Override
	public ArrayList<Edizione> select(boolean future) throws SQLException {
		ArrayList<Edizione> result = new ArrayList<>();
		PreparedStatement pstmt;
		if(future) {
			pstmt = conn.prepareStatement("SELECT * from calendario WHERE dataInizio > ?");
			java.util.Date today = new java.util.Date();
			Date todaySQL = new Date(today.getTime());
			pstmt.setDate(1, todaySQL);
		}else {
			pstmt = conn.prepareStatement("SELECT * from calendario");	
		}
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			result.add(new Edizione(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getInt(4), rs.getString(5), rs.getString(6)));
			}
			return result;
	}

	/*
	 * lettura di tutte le edizioni a cui � iscritto una certo utente, presenti nel calendario dei corsi
	 * se future = true, le edizioni lette devono essere solo quelle a partire dalla data in odierna e dell'anno corrente 
	 * se future = false devono essere lette tutte le edizioni
	 * le edizioni vengono individuate in base al idUtente
	 * se non vi sono edizioni per quella categoria o la categoria non esiste viene ritornata una lista vuota
	 */
	@Override
	public ArrayList<Edizione> select(String idUtente, boolean future) throws SQLException {
		ArrayList<Edizione> result = new ArrayList<>();
		PreparedStatement pstmt;
		if(future) {
			pstmt = conn.prepareStatement("SELECT * from calendario JOIN iscritti ON(id_edizione) WHERE user_id = ? AND dataInizio > ? ");
			java.util.Date today = new java.util.Date();
			Date todaySQL = new Date(today.getTime());
			pstmt.setDate(2, todaySQL);
		}else{
			pstmt = conn.prepareStatement("SELECT * from calendario JOIN iscritti ON(id_edizione) WHERE user_id = ?");	
		}
		pstmt.setString(1, idUtente);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			result.add(new Edizione(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getInt(4), rs.getString(5), rs.getString(6)));
			}
		return result;
	}

	/*
	 * Numero massimo di partecipanti a un'edizione in base al corso a cui l'edizione appartiene
	 * se la query non viene eseguita ritorna 0	
	 */
	@Override
	public int maxPartecipanti(int idEdizione) throws SQLException {
		
		int max = 0;
		
		PreparedStatement stmt = conn.prepareStatement("SELECT numeroMaxPartecipanti FROM catalogo "
				+ "JOIN calendario USING (id_corso) WHERE id_edizione = ?");
		stmt.setInt(1, idEdizione);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			max = rs.getInt(1);
		}
		
		return max;
	}

	
}
