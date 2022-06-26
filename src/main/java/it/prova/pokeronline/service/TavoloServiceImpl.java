package it.prova.pokeronline.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.TavoloRepository;
import it.prova.pokeronline.repository.UtenteRepository;
import it.prova.pokeronline.web.api.exception.TavoloAncoraAttivoException;
import net.bytebuddy.asm.Advice.Return;

@Service
public class TavoloServiceImpl implements TavoloService {
	
	@Autowired
	private TavoloRepository tavoloRepository;
	@Autowired
	private UtenteRepository utenteRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> listAllTavoli() {
		return (List<Tavolo>) tavoloRepository.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> listAllTavoliCreatiDaSpecialPlayer(Utente utenteInstance) {
		return (List<Tavolo>) tavoloRepository.findAllByUtenteCreazione_Id(utenteInstance.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public Tavolo caricaSingoloTavolo(Long id) {
		return tavoloRepository.findByIdEager(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Tavolo caricaSingoloTavoloConUtenti(Long id) {
		return tavoloRepository.findByIdConUtenti(id).orElse(null);
	}

	@Override
	@Transactional
	public Tavolo aggiorna(Tavolo tavoloInstance) {
		if(!tavoloInstance.getGiocatori().isEmpty()) 
			throw new TavoloAncoraAttivoException("Ci Sono ancora dei giocatori impossibile aggiornare il tavolo");
		return tavoloRepository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		return tavoloRepository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Tavolo tavoloInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> findByExample(Tavolo example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Tavolo findByDenominazione(String denominazione) {
		return tavoloRepository.findByDenominazione(denominazione);
	}

	@Override
	public Tavolo inserisciNuovoConSpecialPlayer(Tavolo buildTavoloModel) {
		Utente utenteCreazione=utenteRepository.findByUsername("specialplayer").orElse(null);
		buildTavoloModel.setUtenteCreazione(utenteCreazione);
		return tavoloRepository.save(buildTavoloModel);
	}

	@Override
	public Tavolo inserisciNuovoAdmin(Tavolo buildTavoloModel) {
		Utente utenteCreazione=utenteRepository.findByUsername("admin").orElse(null);
		buildTavoloModel.setUtenteCreazione(utenteCreazione);	
		return tavoloRepository.save(buildTavoloModel);
	}

	@Override
	public Tavolo caricaSingoloTavoloDiSpecialPlayerConUtenti(long id, Utente utenteInstance) {
		return tavoloRepository.findByIdAndUtenteCreazione(id, utenteInstance).orElse(null);
	}

}
