package it.prova.pokeronline.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;
import it.prova.pokeronline.web.api.exception.PermessoNegatoPerModificaAlTavoloException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("/api/gestioneTavolo")
public class GestioneTavolo {
	
	@Autowired
	private TavoloService tavoloServiceInstance;
	@Autowired
	private UtenteService utenteServiceInstance;
	
	@GetMapping
	public List<TavoloDTO> getAll() {
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
			return  TavoloDTO.createTavoloDTOListFromModelList(tavoloServiceInstance.listAllTavoli());
		}
		return TavoloDTO.createTavoloDTOListFromModelList(tavoloServiceInstance.listAllTavoliCreatiDaSpecialPlayer(utenteServiceInstance.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));
	}
	
	@PostMapping
	public TavoloDTO createNew(@Valid @RequestBody TavoloDTO tavoloInput) {
		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
		// non sta bene
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
			if (tavoloInput.getId() != null)
				throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
	
			Tavolo tavoloInserito = tavoloServiceInstance.inserisciNuovoAdmin(tavoloInput.buildTavoloModel(true));
			return TavoloDTO.buildTavoloDTOFromModel(tavoloInserito);
		}
		Tavolo tavoloInserito = tavoloServiceInstance.inserisciNuovoConSpecialPlayer(tavoloInput.buildTavoloModel(true));
		return TavoloDTO.buildTavoloDTOFromModel(tavoloInserito);
		
	}
	
	@GetMapping("/{id}")
	public TavoloDTO findById(@PathVariable(value = "id", required = true) long id) {
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
			Tavolo tavoloAdmin = tavoloServiceInstance.caricaSingoloTavoloConUtenti(id);
			if (tavoloAdmin == null)
				throw new TavoloNotFoundException("Tavolo not found con id: " + id);
			
			return TavoloDTO.buildTavoloDTOFromModel(tavoloAdmin);
		}
		Tavolo tavolo = tavoloServiceInstance.caricaSingoloTavoloDiSpecialPlayerConUtenti(id, utenteServiceInstance.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
		return TavoloDTO.buildTavoloDTOFromModel(tavolo);
	}
	
	@PutMapping("/{id}")
	public TavoloDTO update(@Valid @RequestBody TavoloDTO tavoloInput, @PathVariable(required = true) Long id) {
		Tavolo tavolo = tavoloServiceInstance.caricaSingoloTavoloConUtenti(id);
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
			if (tavolo == null)
				throw new TavoloNotFoundException("Tavolo not found con id: " + id);
	
			tavoloInput.setId(id);
			Tavolo tavoloAggiornato = tavoloServiceInstance.aggiorna(tavoloInput.buildTavoloModel(true));
			return TavoloDTO.buildTavoloDTOFromModel(tavoloAggiornato);
		}
		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);
			
		if(tavoloInput.getUtenteCreazione().getId()!= tavolo.getUtenteCreazione().getId()) throw new PermessoNegatoPerModificaAlTavoloException("Non hai l'autorizzazione per modificare il tavolo");
	
		tavoloInput.setId(id);
		Tavolo tavoloAggiornato = tavoloServiceInstance.aggiorna(tavoloInput.buildTavoloModel(true));
		return TavoloDTO.buildTavoloDTOFromModel(tavoloAggiornato);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable(required = true) Long id) {
		Tavolo tavoloInstance = tavoloServiceInstance.caricaSingoloTavoloConUtenti(id);

		if (tavoloInstance == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		tavoloServiceInstance.rimuovi(tavoloInstance);
	}
	
	@PostMapping("/search")
	public List<TavoloDTO> Search(@RequestBody TavoloDTO exampleDTO) {

		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
			return TavoloDTO.createTavoloDTOListFromModelList(
					tavoloServiceInstance.findByExampleAdmin(exampleDTO.buildTavoloModel(true)));

		}
		return TavoloDTO.createTavoloDTOListFromModelList(tavoloServiceInstance.findByExample(exampleDTO.buildTavoloModel(true),
				utenteServiceInstance.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));

	}
	
	

}
