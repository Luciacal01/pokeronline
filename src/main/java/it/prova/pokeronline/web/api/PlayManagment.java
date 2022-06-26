package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.CreditoInsufficienteException;
import it.prova.pokeronline.web.api.exception.NonPuoiGiocareAQuestoTavoloException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("/api/playManagment")
public class PlayManagment {
	
	@Autowired
	private UtenteService utenteService;
	@Autowired
	private TavoloService tavoloService;
	
	@GetMapping("/compraCredito/{valoreDaSommare}")
	public UtenteDTO compraCredito(@PathVariable(value = "valoreDaSommare", required = true) Integer valoreDaSommare){
		
		Utente giocatoreAcquirente = utenteService
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if(giocatoreAcquirente == null || giocatoreAcquirente.getId()==null) throw new UtenteNotFoundException("Non è stato possibile trovare l'utente");
		
		utenteService.aggiornaCreditoUtente(giocatoreAcquirente, valoreDaSommare);
		
		return UtenteDTO.buildUtenteDTOFromModel(giocatoreAcquirente);
		
	} 
	
	@GetMapping("/dammiIlLastGame")
	public boolean dammiIlLastGame() {
		List<TavoloDTO> listaTavoliUtente=TavoloDTO
				.createTavoloDTOListFromModelList(tavoloService.findTavoloByGiocatoreContains(utenteService
						.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));
		
		if(listaTavoliUtente.isEmpty())
			return false;
		
		return true;
	}
	
	@GetMapping("/abbandonaPartita/{tavoloId}")
	public void abbandonaPartita(@PathVariable(value = "tavoloId", required = true) Long tavoloId) {
		Utente giocatore= utenteService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		tavoloService.abbandonaPartita(tavoloId, giocatore);
	}
	
	@GetMapping("/ricerca")
	public List<TavoloDTO> ricerca(){
		List<Tavolo> TavoliConEsperienzaMinimaMinoreDiQuellaAccumulata=tavoloService.ricercaTavoli(
				utenteService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
				.getEsperienzaAccumulata());
		return TavoloDTO.createTavoloDTOListFromModelList(TavoliConEsperienzaMinimaMinoreDiQuellaAccumulata);
		
	}
	
	@GetMapping("/giocaPartitaAQuelTavolo/{tavoloId}")
	public int giocaPartita(@PathVariable(value = "tavoloId", required = true) Long tavoloId) {
		Utente giocatore = utenteService
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		Tavolo tavolo= tavoloService.caricaSingoloTavoloConUtenti(tavoloId);
		
		if(tavolo==null) throw new TavoloNotFoundException("Il tavolo non è presente, inserirne uno diverso");
		
		if(giocatore.getEsperienzaAccumulata()<= tavolo.getEsperienzaMinima()) throw new NonPuoiGiocareAQuestoTavoloException("L'esperienza richiesta è minore di quella accumulata, gioca ancora");
		
		if(giocatore.getCreditoAccumulato()<= tavolo.getCifraMinima()) throw new CreditoInsufficienteException("Non hai abbastanza denaro per giocare!!");
		
		if(!tavolo.getGiocatori().contains(giocatore)) {
			tavoloService.aggiungiGiocatore(tavolo, giocatore);
		}
		
		tavoloService.giocaPartita(giocatore,tavolo);
		
		return giocatore.getCreditoAccumulato();
	}
	
}
