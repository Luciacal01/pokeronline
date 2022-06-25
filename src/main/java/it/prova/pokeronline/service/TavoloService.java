package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;


public interface TavoloService {
	
	public List<Tavolo> listAllTavoli();

	public Tavolo caricaSingoloTavolo(Long id);

	public Tavolo caricaSingoloTavoloConUtenti(Long id);

	public Tavolo aggiorna(Tavolo tavoloInstance);

	public Tavolo inserisciNuovo(Tavolo tavoloInstance);

	public void rimuovi(Tavolo tavoloInstance);

	public List<Tavolo> findByExample(Tavolo example);
	
	public Tavolo findByDenominazione(String denominazione);

	List<Tavolo> listAllTavoliCreatiDaSpecialPlayer(Utente utenteInstance);

}
