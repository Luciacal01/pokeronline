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
	@Transactional(readOnly = true)
	public List<Utente> listAllUtenti() {
		return (List<Utente>) utenteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Utente caricaSingoloUtente(Long id) {
		return utenteRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Utente caricaSingoloUtenteConRuoli(Long id) {
		return utenteRepository.findByIdConRuoli(id).orElse(null);
	}

	@Override
	@Transactional
	public Utente aggiorna(Utente utenteInstance) {
		Utente utenteReloaded = utenteRepository.findById(utenteInstance.getId()).orElse(null);
		if (utenteReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		utenteReloaded.setNome(utenteInstance.getNome());
		utenteReloaded.setCognome(utenteInstance.getCognome());
		utenteReloaded.setUsername(utenteInstance.getUsername());
		utenteReloaded.setDateRegistrazione(utenteInstance.getDateRegistrazione());
		utenteReloaded.setEsperienzaAccumulata(utenteInstance.getEsperienzaAccumulata());
		utenteReloaded.setCreditoAccumulato(utenteInstance.getCreditoAccumulato());
		utenteReloaded.setRuoli(utenteInstance.getRuoli());
		return utenteRepository.save(utenteReloaded);

	}

	@Override
	@Transactional
	public Utente inserisciNuovo(Utente utenteInstance) {
		utenteInstance.setEsperienzaAccumulata(0);
		utenteInstance.setCreditoAccumulato(0);
		utenteInstance.setStato(StatoUtente.CREATO);
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		utenteInstance.setDateRegistrazione(new Date());
		return utenteRepository.save(utenteInstance);

	}

	@Override
	@Transactional
	public void rimuovi(Utente utenteInstance) {
		utenteRepository.delete(utenteInstance);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Utente> findByExample(Utente example) {
		return utenteRepository.findByExample(example);
	}

	@Override
	@Transactional(readOnly = true)
	public Utente findByUsernameAndPassword(String username, String password) {
		return utenteRepository.findByUsernameAndPassword(username, password);
	}

	@Override
	@Transactional(readOnly = true)
	public Utente eseguiAccesso(String username, String password) {
		return utenteRepository.findByUsernameAndPasswordAndStato(username, password, StatoUtente.ATTIVO);
	}

	@Override
	@Transactional
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
		
		//utenteRepository.save(utenteInstance);
	}

	@Override
	@Transactional(readOnly = true)
	public Utente findByUsername(String username) {
		return utenteRepository.findByUsername(username).orElse(null);
	}

	@Override
	@Transactional
	public Utente DisabilitaUtente(Utente utenteInstance) {
		Utente utenteDaDisabilitare = utenteRepository.findById(utenteInstance.getId()).orElse(null);
		utenteDaDisabilitare.setStato(StatoUtente.DISABILITATO);
		return utenteRepository.save(utenteDaDisabilitare);
	}
	
	

}
