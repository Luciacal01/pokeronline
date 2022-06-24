package it.prova.pokeronline.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.StatoUtente;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.UtenteRepository;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<Utente> listAllUtenti() {
		return (List<Utente>) utenteRepository.findAll();
	}

	@Override
	public Utente caricaSingoloUtente(Long id) {
		return utenteRepository.findById(id).orElse(null);
	}

	@Override
	public Utente caricaSingoloUtenteConRuoli(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void aggiorna(Utente utenteInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void inserisciNuovo(Utente utenteInstance) {
		utenteInstance.setStato(StatoUtente.CREATO);
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		utenteInstance.setDateRegistrazione(new Date());
		utenteRepository.save(utenteInstance);

	}

	@Override
	public void rimuovi(Utente utenteInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Utente> findByExample(Utente example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente findByUsernameAndPassword(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente eseguiAccesso(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeUserAbilitation(Long utenteInstanceId) {
		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
		if (utenteInstance == null)
			throw new RuntimeException("Elemento non trovato.");

		if (utenteInstance.getStato() == null || utenteInstance.getStato().equals(StatoUtente.CREATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
		else if (utenteInstance.getStato().equals(StatoUtente.ATTIVO))
			utenteInstance.setStato(StatoUtente.DISABILITATO);
		else if (utenteInstance.getStato().equals(StatoUtente.DISABILITATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);

	}

	@Override
	public Utente findByUsername(String username) {
		return utenteRepository.findByUsername(username).orElse(null);
	}

}
