package service;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.CalendarioDAO;
import dao.CalendarioDAOImpl;
import dao.CatalogoDAO;
import dao.CatalogoDAOImpl;
import dao.CategoriaDAO;
import dao.CategoriaDAOImpl;
import dao.FeedBackDAOImpl;
import dao.FeedbackDAO;
import dao.IscrizioneUtenteDAO;
import dao.IscrizioneUtenteDAOImpl;
import dto.CorsoDTO;
import dto.EdizioneDTO;
import entity.Categoria;
import entity.Corso;
import entity.Edizione;
import entity.Feedback;
import entity.Utente;
import exceptions.ConnessioneException;
import exceptions.DAOException;

public class CorsoServiceImpl implements CorsoService {

	// dichiarare qui tutti i dao di cui si ha bisogno
	private CatalogoDAO daoC;
	private CategoriaDAO daoCat;
	private CalendarioDAO daoCal;	
	private FeedbackDAO daoF;
	private IscrizioneUtenteDAO daoI;

	// ... dichiarazione di altri DAO

	// costruire qui tutti i dao di cui si ha bisogno
	public CorsoServiceImpl() throws ConnessioneException {
		daoC = new CatalogoDAOImpl();
		daoF = new FeedBackDAOImpl();
		daoCat = new CategoriaDAOImpl();
		daoCal = new CalendarioDAOImpl();
		daoI = new IscrizioneUtenteDAOImpl();
		
		// ... costruzione dei altri dao
	}

	/*
	 * il metodo mostra tutti i corsi offerti dalla scuola se il metodi del/dei DAO
	 * invocati sollevano una eccezione, il metodo deve tornare una DAOException con
	 * all'interno l'exception originale
	 */
	@Override
	public ArrayList<Corso> visualizzaCatalogoCorsi() throws DAOException {
		try {
			return daoC.select();
		} catch (SQLException e) {
			throw new DAOException("Errore nel recuperare o leggere i dati", e);

		}
	}

	/*
	 * il metodo mostra l'elenco dei corsi di una certa categoria
	 * se il metodi del/dei DAO invocati sollevano una eccezione, il metodo deve tornare una DAOException con all'interno l'exception originale 
	 */
	@Override
	public ArrayList<Corso> visualizzaCorsiPerCategoria(int idCategoria) throws DAOException {
		ArrayList<Corso> corsiCategoria;
		try {
			corsiCategoria = daoC.getCorsiCategoria(idCategoria);
		} catch (SQLException e) {
			throw new DAOException("Errore nel recuperare o leggere i dati", e);
		}

		return corsiCategoria;
	}

	/*
	 * lettura di tutte le categorie se il metodi del/dei DAO invocati sollevano una
	 * eccezione, il metodo deve tornare una DAOException con all'interno
	 * l'exception originale
	 */
	@Override
	public ArrayList<Categoria> visualizzaCategorie() throws DAOException {
		ArrayList<Categoria> categorie;
		try {
			categorie = daoCat.select();
		} catch (SQLException e) {
			throw new DAOException("Errore nel recuperare o leggere i dati", e);
		}
		return categorie;
	}

	/*
	 * il metodo (invocabile solo da un amministratore) crea una nuova categoria se
	 * il metodi del/dei DAO invocati sollevano una eccezione, il metodo deve
	 * tornare una DAOException con all'interno l'exception originale
	 */
	@Override
	public void creaNuovaCategoria(String descrizione) throws DAOException {
		try {
			daoCat.insert(descrizione);
		} catch (SQLException e) {
			throw new DAOException("Errore nell'inserire i dati", e);
		}
		return;

	}
	
	/*
	 * ritorna un oggetto CorsoDTO con tutti i dati di un singolo corso 
	 * tutte le edizioni di quel corso con relativi feedbacks (classe EdizioneDTO)
	 * il corso � individuato tramite idCorso
	 * se il metodi del/dei DAO invocati sollevano una eccezione, il metodo deve tornare una DAOException con all'interno l'exception originale
	 */
	@Override
	public CorsoDTO visualizzaSchedaCorso(int idCorso) throws DAOException {
		ArrayList<Feedback> feedbacks;
		ArrayList<Utente> utentiEdizione;
		ArrayList<EdizioneDTO> edizioniDTO = new ArrayList<>();
		Corso corso = new Corso();
		CorsoDTO corsoDTO;
		try {
			corso = daoC.select(idCorso);
			ArrayList<Edizione> edizioni = daoCal.selectByCorso(idCorso);
			
			for (int i=0; i < edizioni.size(); i++) {
				feedbacks = daoF.selectPerEdizione(edizioni.get(i).getCodice());
				utentiEdizione = daoI.selectUtentiPerEdizione(edizioni.get(i).getCodice());
				edizioniDTO.add(new EdizioneDTO(edizioni.get(i), feedbacks, utentiEdizione));
			}
			corsoDTO = new CorsoDTO(corso, edizioniDTO);

		} catch (SQLException e) {
			throw new DAOException("Errore nell'inserire i dati", e);
		}
		return corsoDTO;
	}

	/*
	 * ritorna una lista con tutti i feedbacks relativi ad un corso il corso �
	 * individuato tramite idCorso se il metodi del/dei DAO invocati sollevano una
	 * eccezione, il metodo deve tornare una DAOException con all'interno
	 * l'exception originale
	 */
	@Override
	public ArrayList<Feedback> visualizzaFeedbackCorso(int idCorso) throws DAOException {
		ArrayList<Feedback> feedbacks = new ArrayList<>();
		
		try {
			feedbacks = daoF.selectFeedbackPerCorso(idCorso);
		} catch (SQLException e) {
			throw new DAOException("Errore nel recuperare o leggere i dati", e);
		}
		return feedbacks;
	}

	/*
	 * modifica tutti i dati di un corso se il metodi del/dei DAO invocati sollevano
	 * una eccezione, il metodo deve tornare una DAOException con all'interno
	 * l'exception originale
	 */
	@Override
	public void modificaDatiCorso(Corso corso) throws DAOException {
		try {
			daoC.update(corso);
		} catch (SQLException e) {
			throw new DAOException("Errore nell'aggiornare i dati", e);
		}
		return;
	}

	/*
	 * inserisce un nuovo corso a catalogo (invocabile solo dall'amministratore) se
	 * il metodi del/dei DAO invocati sollevano una eccezione, il metodo deve
	 * tornare una DAOException con all'interno l'exception originale
	 */
	@Override
	public void inserisciCorso(Corso corso) throws DAOException {
		try {
			daoC.insert(corso);
		} catch (SQLException e) {
			throw new DAOException("Errore nell'inserire dati", e);
		}
		return;
	}
	

	/*
	 * cancella un singolo corso dal catalogo dei corsi se il metodi del/dei DAO
	 * invocati sollevano una eccezione, il metodo deve tornare una DAOException con
	 * all'interno l'exception originale
	 */
	@Override
	public void cancellaCorso(int codiceCorso) throws DAOException {
		try {
			daoC.delete(codiceCorso);
		} catch (Exception e) {
			throw new DAOException("Errore nell'inserire i dati nel database", e);
		}
		return;
	}

	/*
	 * legge i dati di un singolo corso (senza edizioni) se il metodi del/dei DAO
	 * invocati sollevano una eccezione, il metodo deve tornare una DAOException con
	 * all'interno l'exception originale
	 */
	@Override
	public Corso visualizzaCorso(int codiceCorso) throws DAOException {
		Corso corso;
		try {
			corso = daoC.select(codiceCorso);
		} catch (Exception e) {
			throw new DAOException("Errore nell'inserire i dati nel database", e);
		}
		return corso;
	}

}
