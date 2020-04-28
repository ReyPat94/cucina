package service;

import java.util.ArrayList;

import dto.EdizioneDTO;
import entity.Edizione;
import exceptions.DAOException;

public interface EdizioneService {

	void modificaEdizione(Edizione e) throws DAOException;
	void inserisciEdizione(Edizione e) throws DAOException;
	abstract void cancellaEdizione(int idEdizione) throws DAOException;
	ArrayList<EdizioneDTO> visualizzaEdizioniPerMese(int mese) throws DAOException;
	ArrayList<EdizioneDTO> visualizzaEdizioniPerAnno(int anno) throws DAOException;	
	ArrayList<EdizioneDTO> visualizzaEdizioniPerCorso(int idCorso) throws DAOException;
	EdizioneDTO visualizzaEdizione(int idEdizione) throws DAOException;
	void iscriviUtente(int idEdizione, String idUtente) throws DAOException;
	void cancellaIscrizioneUtente (int idEdizione, String idUtente) throws DAOException;
}
