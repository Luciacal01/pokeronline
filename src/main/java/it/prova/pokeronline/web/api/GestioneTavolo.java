package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;

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
			List<TavoloDTO> list = TavoloDTO.createTavoloDTOListFromModelList(tavoloServiceInstance.listAllTavoli());
			return list;
		}
		return TavoloDTO.createTavoloDTOListFromModelList(tavoloServiceInstance.listAllTavoliCreatiDaSpecialPlayer(utenteServiceInstance.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));
	}
	
	
	
	

}
