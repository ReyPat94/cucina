package entity;

import java.util.ArrayList;

import dto.EdizioneDTO;
import exceptions.ConnessioneException;
import exceptions.DAOException;
import service.CorsoService;
import service.CorsoServiceImpl;
import service.EdizioneService;
import service.EdizioneServiceImpl;

/*
 * rappresenta il carrello di acquisto da parte di un utente 
 * L'utente pu� acquistare una o pi� partecipazioni ad una edizione di un corso
 */
public class Carrello {
	private EdizioneService es;
	private CorsoService cs;

	public Carrello() {
		try {
			EdizioneService es = new EdizioneServiceImpl();
			CorsoService cs = new CorsoServiceImpl();
			this.cs = cs;
			this.es = es;
		} catch (ConnessioneException ex) {
			ex.printStackTrace();
		}
	}

	private ArrayList<Edizione> edizioniAcquistate = new ArrayList<Edizione>();
	private ArrayList<Edizione> edizioniCarrello = new ArrayList<Edizione>();

	/*
	 * aggiunge una edizione nel carrello se l'edizione gi� � presente nel carrello
	 * questa non va aggiunta
	 */
	public void aggiungiEdizione(Edizione e) {
		if (!edizioniCarrello.contains(e))
			edizioniCarrello.add(e);
	}

	/*
	 * elimina una edizione nel carrello se l'edizione non � presente nel carrello
	 * NON si sollava una eccezione
	 */
	public void rimuoviEdizione(Edizione e) {
		if (edizioniCarrello.contains(e))
			edizioniCarrello.remove(e);
	}

	/*
	 * legge una edizione presente nel carrello in base ad idEdizione se l'edizione
	 * non � presente nel carrello il metodo torna null
	 */
	public Edizione getEdizione(int idEdizione) throws DAOException {

		try {
			EdizioneDTO edDTO = es.visualizzaEdizione(idEdizione);
			return edDTO.getEdizione();
		} catch (DAOException de) {
			return null;
		}
	}

	/*
	 * recupera tutte le edizioni presente nel carrello
	 */
	public ArrayList<Edizione> getEdizioniCarrello() {
		return edizioniCarrello;
	}

	/*
	 * recupera il numero di edizioni presenti nel carrello
	 */
	public int getSize() {
		return edizioniCarrello.size();
	}

	/*
	 * il metodo ritorna il costo totale delle edizioni presenti nel carrello
	 */
	public double getCostoTotale() {
		int result = 0;
		for (int i = 0; i < edizioniCarrello.size(); i++) {
			Edizione edizione = edizioniCarrello.get(i);
			try {
				result += cs.visualizzaCorso(edizione.getIdCorso()).getCosto();
			} catch (DAOException de) {
				de.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "Carrello [edizioniAcquistate=" + edizioniAcquistate + "]";
	}

}
