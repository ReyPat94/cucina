package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;

import dao.CalendarioDAO;
import dao.CalendarioDAOImpl;
import dao.FeedBackDAOImpl;
import dao.FeedbackDAO;
import dao.IscrizioneUtenteDAO;
import dao.IscrizioneUtenteDAOImpl;
import dto.EdizioneDTO;
import entity.Edizione;
import entity.Feedback;
import entity.Utente;
import exceptions.ConnessioneException;
import exceptions.DAOException;

public class EdizioneServiceImpl implements EdizioneService {

	// dichiarare qui tutti i dao di cui si ha bisogno
	private CalendarioDAO daoC;
	private IscrizioneUtenteDAO daoIU;
	private FeedbackDAO daoF;

	// costruire qui tutti i dao di cui si ha bisogno
	public EdizioneServiceImpl() throws ConnessioneException {
		daoC = new CalendarioDAOImpl();
		daoIU = new IscrizioneUtenteDAOImpl();
		daoF = new FeedBackDAOImpl();
	}

	/*
	 * inserisce una nuova edizione
	 */
	@Override
	public void inserisciEdizione(Edizione e) throws DAOException {
		try {
			daoC.insert(e);
		} catch (SQLException se) {
			throw new DAOException("Inserimento non riuscito", se);
		}
	}

	/*
	 * modifica tutti i dati di una edizione esistente
	 */
	@Override
	public void modificaEdizione(Edizione e) throws DAOException {
		try {
			daoC.update(e);
		} catch (SQLException se) {
			throw new DAOException("Modifica non riuscita", se);
		}
	}

	/*
	 * cancella una edizione esistente. E' possibile cancellare una edizione
	 * soltanto se la data di inizio � antecedente a quella odierna Nel caso
	 * l'edizione sia cancellabile, � necessario cancellare l'iscrizione a tutti gli
	 * utenti iscritti
	 */
	@Override
	public void cancellaEdizione(int idEdizione) throws DAOException {
		try {
			Edizione edizione = daoC.selectEdizione(idEdizione);
			Date dataInizio = edizione.getDataInizio();
			Date today = new Date();
			if (dataInizio.before(today)) {
				daoC.delete(idEdizione);
			} else {
				throw new DAOException("Edizione non cancellabile, data di inizio successiva alla data odierna");
			}
		} catch (SQLException se) {
			throw new DAOException("Cancellazione non riuscita", se);
		}
	}

	/*
	 * iscrive un utente ad una edizione un utente si pu� iscrivere solo se ci sono
	 * ancora posti disponibili considerato che ogni corso ha un numero massimo di
	 * partecipanti
	 */
	@Override
	public void iscriviUtente(int idEdizione, String idUtente) throws DAOException {

		try {
			int iscritti = daoIU.getNumeroIscritti(idEdizione);
			int maxIscritti = daoC.maxPartecipanti(idEdizione);

			if (iscritti < maxIscritti) {
				daoIU.iscriviUtente(idEdizione, idUtente);
			} else {
				throw new DAOException("L'edizione ha già raggiunto il numero massimo di iscritti");
			}

		} catch (SQLException se) {
			throw new DAOException("Iscrizione non riuscita", se);
		}

	}

	/*
	 * cancella l'iscrizione ad un utente
	 */
	@Override
	public void cancellaIscrizioneUtente(int idEdizione, String idUtente) throws DAOException {
		try {
			daoIU.cancellaIscrizioneUtente(idEdizione, idUtente);
		} catch (SQLException se) {
			throw new DAOException("Cancellazione non riuscita", se);
		}

	}

	/*
	 * il metodo ritorna tutte le edizioni con relativi utenti e feedback dei corsi
	 * in calendario nel mese indicato dell'anno corrente se il metodi del/dei DAO
	 * invocati sollevano una eccezione, il metodo deve tornare una DAOException con
	 * all'interno l'exception originale
	 */
	@Override
	public ArrayList<EdizioneDTO> visualizzaEdizioniPerMese(int mese) throws DAOException {

		try {

			ArrayList<EdizioneDTO> result = new ArrayList<EdizioneDTO>();
			LocalDate today = LocalDate.now();
			LocalDate from = today.withMonth(mese).withDayOfMonth(1);
			LocalDate to = from.with(TemporalAdjusters.lastDayOfMonth());
			java.sql.Date da = java.sql.Date.valueOf(from);
			java.sql.Date a = java.sql.Date.valueOf(to);

			ArrayList<Edizione> edizioni = daoC.select(da, a);

			for (Edizione edizione : edizioni) {
				ArrayList<Feedback> feedbacks = daoF.selectPerEdizione(edizione.getCodice());
				ArrayList<Utente> utenti = daoIU.selectUtentiPerEdizione(edizione.getCodice());
				result.add(new EdizioneDTO(edizione, feedbacks, utenti));
			}

			return result;

		} catch (SQLException se) {
			throw new DAOException("Metodo fallito", se);
		}
	}

	/*
	 * il metodo ritorna tutte le edizioni con relativi utenti e feedback del corso
	 * specificato presenti in calendario nell'anno corrente a partire dalla data
	 * odierna se il metodi del/dei DAO invocati sollevano una eccezione, il metodo
	 * deve tornare una DAOException con all'interno l'exception originale
	 */
	@Override
	public ArrayList<EdizioneDTO> visualizzaEdizioniPerAnno(int anno) throws DAOException {

		try {

			ArrayList<EdizioneDTO> result = new ArrayList<EdizioneDTO>();
			LocalDate from = LocalDate.now();
			LocalDate to = LocalDate.of(anno, 12, 31);
			java.sql.Date da = java.sql.Date.valueOf(from);
			java.sql.Date a = java.sql.Date.valueOf(to);

			ArrayList<Edizione> edizioni = daoC.select(da, a);

			for (Edizione edizione : edizioni) {
				ArrayList<Feedback> feedbacks = daoF.selectPerEdizione(edizione.getCodice());
				ArrayList<Utente> utenti = daoIU.selectUtentiPerEdizione(edizione.getCodice());
				result.add(new EdizioneDTO(edizione, feedbacks, utenti));
			}

			return result;

		} catch (SQLException se) {
			throw new DAOException("Metodo fallito", se);
		}
	}

	/*
	 * il metodo ritorna tutte le edizioni con relativi utenti e feedback del corso
	 * specificato presenti in calendario nell'anno corrente a partire dalla data
	 * odierna se il metodi del/dei DAO invocati sollevano una eccezione, il metodo
	 * deve tornare una DAOException con all'interno l'exception originale
	 */
	@Override
	public ArrayList<EdizioneDTO> visualizzaEdizioniPerCorso(int idCorso) throws DAOException {
		
		try {

			ArrayList<EdizioneDTO> result = new ArrayList<EdizioneDTO>();

			ArrayList<Edizione> edizioni = daoC.selectByCorso(idCorso);

			for (Edizione edizione : edizioni) {
				ArrayList<Feedback> feedbacks = daoF.selectPerEdizione(edizione.getCodice());
				ArrayList<Utente> utenti = daoIU.selectUtentiPerEdizione(edizione.getCodice());
				result.add(new EdizioneDTO(edizione, feedbacks, utenti));
			}

			return result;

		} catch (SQLException se) {
			throw new DAOException("Metodo fallito", se);
		}
	}

	/*
	 * il metodo ritorna tutte le edizioni dei corsi e relativi utenti e feedbacks
	 * in calendario dell'anno corrente a partire dalla data odierna se il metodi
	 * del/dei DAO invocati sollevano una eccezione, il metodo deve tornare una
	 * DAOException con all'interno l'exception originale
	 */
	@Override
	public EdizioneDTO visualizzaEdizione(int idEdizione) throws DAOException {
		
		try {
			
		Edizione edizione = daoC.selectEdizione(idEdizione);
		ArrayList<Feedback> feedbacks = daoF.selectPerEdizione(idEdizione);
		ArrayList<Utente> utenti = daoIU.selectUtentiPerEdizione(idEdizione);
		
		return new EdizioneDTO(edizione, feedbacks, utenti);
		
		} catch (SQLException se) {
			throw new DAOException("Metodo fallito", se);
		}

	}

}
