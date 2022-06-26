package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;


public interface TavoloService {
	
	public List<Tavolo> listAllTavoli();

	public Tavolo caricaSingoloTavolo(Long id);

	public Tavolo caricaSingoloTavoloConUtenti(Long id);

	public Tavolo aggiorna(Tavolo tavoloInstance);

	public Tavolo inserisciNuovo(Tavolo tavoloInstance);

	public void rimuovi(Tavolo tavoloInstance);
	
	public Tavolo findByDenominazione(String denominazione);

	List<Tavolo> listAllTavoliCreatiDaSpecialPlayer(Utente utenteInstance);

	public Tavolo inserisciNuovoConSpecialPlayer(Tavolo buildTavoloModel);

	public Tavolo inserisciNuovoAdmin(Tavolo buildTavoloModel);

	public Tavolo caricaSingoloTavoloDiSpecialPlayerConUtenti(long id, Utente utenteInstance);

	public List<Tavolo> findByExample(Tavolo example, Utente utente);
	
	public List<Tavolo> findByExampleAdmin(Tavolo example);

	public List<Tavolo> findTavoloByGiocatoreContains(Utente findByUsername);

	public void abbandonaPartita(Long tavoloId, Utente giocatore);

	public List<Tavolo> ricercaTavoli(Integer esperienzaAccumulata);

}
