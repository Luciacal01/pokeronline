package it.prova.pokeronline.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;

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
				.anyMatch(r -> r.getAuthority().equals("ADMIN"))) {
			List<TavoloDTO> list = TavoloDTO.createTavoloDTOListFromModelList(tavoloServiceInstance.listAllTavoli());
			return list;
		}
		return TavoloDTO.createTavoloDTOListFromModelList(tavoloServiceInstance.listAllTavoliCreatiDaSpecialPlayer(utenteServiceInstance.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));
	}
	
	@PostMapping
	public TavoloDTO createNew(@Valid @RequestBody TavoloDTO tavoloInput) {
		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
		// non sta bene
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ADMIN"))) {
			if (tavoloInput.getId() != null)
				throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
	
			Tavolo tavoloInserito = tavoloServiceInstance.inserisciNuovoAdmin(tavoloInput.buildTavoloModel(true));
			return TavoloDTO.buildTavoloDTOFromModel(tavoloInserito);
		}
		Tavolo tavoloInserito = tavoloServiceInstance.inserisciNuovoConSpecialPlayer(tavoloInput.buildTavoloModel(true));
		return TavoloDTO.buildTavoloDTOFromModel(tavoloInserito);
		
	}
	
	
	
	
	
	

}
