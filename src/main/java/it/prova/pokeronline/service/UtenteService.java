package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Utente;


public interface UtenteService {
	
	public List<Utente> listAllUtenti();

	public Utente caricaSingoloUtente(Long id);

	public Utente caricaSingoloUtenteConRuoli(Long id);

	public Utente aggiorna(Utente utenteInstance);

	public Utente inserisciNuovo(Utente utenteInstance);

	public void rimuovi(Utente utenteInstance);

	public List<Utente> findByExample(Utente example);

	public Utente findByUsernameAndPassword(String username, String password);

	public Utente eseguiAccesso(String username, String password);

	public void changeUserAbilitation(Long utenteInstanceId);

	public Utente findByUsername(String username);
	
	public Utente DisabilitaUtente(Utente utenteInstance);

	public void aggiornaCreditoUtente(Utente giocatoreAcquirente, Integer creditoDaAggiungere);

}
