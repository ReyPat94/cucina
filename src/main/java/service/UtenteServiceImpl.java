package service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.CalendarioDAO;
import dao.CalendarioDAOImpl;
import dao.CatalogoDAO;
import dao.CatalogoDAOImpl;
import dao.FeedBackDAOImpl;
import dao.FeedbackDAO;
import dao.IscrizioneUtenteDAO;
import dao.IscrizioneUtenteDAOImpl;
import dao.RegistrazioneUtenteDAO;
import dao.RegistrazioneUtenteDAOImpl;
import entity.Corso;
import entity.Edizione;
import entity.Feedback;
import entity.Utente;
import exceptions.ConnessioneException;
import exceptions.DAOException;

public class UtenteServiceImpl implements UtenteService {

	// dichiarare qui tutti i dao di cui si ha bisogno
	private RegistrazioneUtenteDAO daoU;
	private FeedbackDAO daoF;
	private CalendarioDAO daoC;
	private IscrizioneUtenteDAO daoI;
	private CatalogoDAO daoCat;
	// ... dichiarazione di altri eventuali DAO

	// costruire qui tutti i dao di cui si ha bisogno
	public UtenteServiceImpl() throws ConnessioneException {
		daoU = new RegistrazioneUtenteDAOImpl();
		daoF = new FeedBackDAOImpl();
		daoC = new CalendarioDAOImpl();
		daoI = new IscrizioneUtenteDAOImpl();
		daoCat = new CatalogoDAOImpl();

	}

	/*
	 * registrazione nel sistema di un nuovo utente Se l'utente � gi� presente si
	 * solleva una eccezione
	 */
	@Override
	public void registrazioneUtente(Utente u) throws DAOException {
		try {
			daoU.insert(u);
		} catch (SQLException e) {
			throw new DAOException("impossibile inserire l'utente", e);
		}

	}

	/*
	 * controllo della presenza di un utente in base a idUtente e password Se
	 * l'utente � presente viene recuperato e ritornato Se l'utente non � presente
	 * si solleva una eccezione
	 */
	@Override
	public Utente checkCredenziali(String idUtente, String psw) throws DAOException {
		Utente u = new Utente(idUtente, psw);
		Utente result;
		try {
			result = daoU.select(u);
			if (result.getPassword().equals(u.getPassword())) {
				return result;
			}
		} catch (SQLException e) {
			throw new DAOException("User doesn't exit", e);
		}
		return result; // useless
	}

	/*
	 * cancellazione di un utente dal sistema l'utente � cancellabile solo se non vi
	 * sono dati correlati. (ad esempio, non � (o � stato) iscritto a nessuna
	 * edizione) se l'utente non � cancellabile si solleva una eccezione
	 * 
	 */
	@Override
	public void cancellaRegistrazioneUtente(String idUtente) throws DAOException {
		Utente u = new Utente(idUtente);
		try {
			daoU.delete(u);
		} catch (SQLException se) {
			throw new DAOException("Utente non cancellabile", se);
		}

	}

	/*
	 * modifica tutti i dati di un utente l'utente viene individuato in base a
	 * idUtente se l'utente non � presente si solleva una eccezione
	 */
	@Override
	public void modificaDatiUtente(Utente u) throws DAOException {
		try {
			daoU.update(u);
		} catch (SQLException e) {
			throw new DAOException("Can't update user because user doesn't exist", e);
		}
	}

	/*
	 * legge tutti gli utenti registrati sul sistema se non vi sono utenti il metodo
	 * ritorna una lista vuota
	 */
	@Override
	public ArrayList<Utente> visualizzaUtentiRegistrati() throws DAOException {
		ArrayList<Utente> result;
		try {
			result = daoU.select();
		} catch (SQLException e) {
			throw new DAOException("Can't get users", e);
		}
		return result;
	}

	/*
	 * inserisce un feedback per una certa edizione Un utente pu� inserire un
	 * feedback solo per i corsi gi� frequentati (e terminati) e solo se non lo ha
	 * gi� fatto in precedenza (un solo feedback ad utente per edizione) se l'utente
	 * non pu� insierire un feedback si solleva una eccezione
	 */
	@Override
	public void inserisciFeedback(Feedback f) throws DAOException {
		String utenteFeedback = f.getIdUtente();
		int edizioneFeedback = f.getIdEdizione();
		try {
			ArrayList<Edizione> edizioniUtente = daoI.selectIscrizioniUtente(utenteFeedback);
			for (Edizione edizione : edizioniUtente) {
				if (edizione.getCodice() == edizioneFeedback) {
					daoF.insert(f);
				}
			}
		} catch (SQLException e) {
			throw new DAOException(
					"Utente ha già lasciato il feedback, o l'utente non esiste, o non è iscritto a nessuna edizione",
					e);
		}

	}

	/*
	 * modifica della descrizione e/o del voto di un feedback il feedback �
	 * modificabile solo da parte dell'utente che lo ha inserito e solo entro un
	 * mese dal termine della edizione del corso se l'utente non pu� modificare un
	 * feedback si solleva una eccezione
	 */

//Francesca COMMENTO: per il momento questo metodo fa l'update del feedback per la descrizione E il voto. 
//                    dato che voto non può essere null, non so come gestire la cosa. Forse userò -1. 
	@Override
	public void modificaFeedback(Feedback feedback) throws DAOException {
		String utente = feedback.getIdUtente();
		int edizioneid = feedback.getIdEdizione();
		try {
			Feedback oldFeedback = daoF.selectSingoloFeedback(utente, edizioneid);
			Edizione edizione = daoC.selectEdizione(edizioneid);
			// trovando la data limite per modifica feedback
			long dataM = edizione.getDataInizio().getTime();
			long durataM = edizione.getDurata() * 86400000L;
			long mese = 2628000000L;
			Date dataLimite = new Date(dataM + durataM + mese); // aggiunge il mese
			// data di oggi (giorno in cui viene modificato il feedback)
			java.util.Date d = new java.util.Date();
			if (dataLimite.after(d)){
				Feedback newFeedback = new Feedback(oldFeedback.getIdFeedback(), feedback.getDescrizione(),
						oldFeedback.getVoto(), oldFeedback.getIdEdizione(), utente);
				daoF.update(newFeedback);
			}
		} catch (SQLException e) {
			throw new DAOException("FeedbackId non esiste o edizione non esiste", e);
		}
	}

	/*
	 * eliminazione di un feedback il feedback � cancellabile solo da parte
	 * dell'utente che lo ha inserito e solo entro un mese dal termine della
	 * edizione del corso se l'utente non pu� cancellare un feedback si solleva una
	 * eccezione
	 */
	@Override
	public void cancellaFeedback(int idFeedback, int idUtente) throws DAOException {
		try {
			Feedback feedback = daoF.getFeedbackByID(idFeedback);
			Edizione edizione = daoC.selectEdizione(feedback.getIdEdizione());
			// trovando la data limite per modifica feedback
			long dataM = edizione.getDataInizio().getTime();
			long durataM = edizione.getDurata() * 86400000L;
			long mese = 2628000000L;
			Date dataLimite = new Date(dataM + durataM + mese); // aggiunge il mese
			// data di oggi (giorno in cui viene modificato il feedback)
			java.util.Date d = new java.util.Date();
			if (dataLimite.after(d)) {
				daoF.delete(idFeedback);
			}
		}catch (SQLException e) {
			throw new DAOException("FeedbackId non esiste o edizione non esiste o feedback non può essere eliminato", e);
		}
	
		
		
		
		
		

	}

}
