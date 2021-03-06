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
import it.prova.pokeronline.web.api.exception.CreditoInsufficienteException;
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
		if(!tavoloInstance.getGiocatori().isEmpty()) 
			throw new TavoloAncoraAttivoException("Ci Sono ancora dei giocatori impossibile aggiornare il tavolo");
		tavoloRepository.delete(tavoloInstance);

	}

	@Transactional(readOnly = true)
	public List<Tavolo> findByExample(Tavolo example, Utente utente) {
		return tavoloRepository.findByExample(example, utente);
	}
	
	@Transactional(readOnly = true)
	public List<Tavolo> findByExampleAdmin(Tavolo example) {
		return tavoloRepository.findByExampleAdmin(example);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Tavolo findByDenominazione(String denominazione) {
		return tavoloRepository.findByDenominazione(denominazione);
	}

	@Override
	@Transactional
	public Tavolo inserisciNuovoConSpecialPlayer(Tavolo buildTavoloModel) {
		Utente utenteCreazione=utenteRepository.findByUsername("specialplayer").orElse(null);
		buildTavoloModel.setUtenteCreazione(utenteCreazione);
		return tavoloRepository.save(buildTavoloModel);
	}

	@Override
	@Transactional
	public Tavolo inserisciNuovoAdmin(Tavolo buildTavoloModel) {
		Utente utenteCreazione=utenteRepository.findByUsername("admin").orElse(null);
		buildTavoloModel.setUtenteCreazione(utenteCreazione);	
		return tavoloRepository.save(buildTavoloModel);
	}

	@Override
	@Transactional(readOnly = true)
	public Tavolo caricaSingoloTavoloDiSpecialPlayerConUtenti(long id, Utente utenteInstance) {
		return tavoloRepository.findByIdAndUtenteCreazione(id, utenteInstance).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> findTavoloByGiocatoreContains(Utente utente) {
		return tavoloRepository.findTavoloGiocatorePresente(utente);
	}

	@Override
	@Transactional
	public void abbandonaPartita(Long tavoloId, Utente giocatore) {
		Tavolo tavolo = tavoloRepository.findById(tavoloId).orElse(null);
		
		if(tavolo== null) {
			return;
		}
		
		tavolo.getGiocatori().remove(giocatore);
		giocatore.setEsperienzaAccumulata(giocatore.getEsperienzaAccumulata()+1);
		utenteRepository.save(giocatore);
		tavoloRepository.save(tavolo);
		
	}

	@Override
	public List<Tavolo> ricercaTavoli(Integer esperienzaAccumulata) {
		return tavoloRepository.findAllByEsperienzaMinimaIsLessThanEqual(esperienzaAccumulata);
	}

	@Override
	@Transactional
	public Tavolo aggiungiGiocatore(Tavolo tavolo, Utente giocatore) {
		tavolo.getGiocatori().add(giocatore);
		return tavoloRepository.save(tavolo);
	}

	@Override
	public void giocaPartita(Utente giocatore, Tavolo tavolo) {
		int credito=giocatore.getCreditoAccumulato();
		if(credito<=0) {
			giocatore.setCreditoAccumulato(0);
			utenteRepository.save(giocatore);
			throw new CreditoInsufficienteException("Il suo credito ?? esaurito, ricaricare o abbandonare la partita");
		}
		
		double segno= Math.random();
		int risultato= (int) Math.random()*1000;
		double totale= segno*risultato;
		if(segno<0.5) {
			credito-=totale;
		}else {
			credito+=totale;
		}
		
		giocatore.setCreditoAccumulato(credito);
		utenteRepository.save(giocatore);
	}


}
