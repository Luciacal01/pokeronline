package it.prova.pokeronline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;


public interface TavoloRepository extends CrudRepository<Tavolo, Long> {
	Tavolo findByDenominazione(String denominazione);
	
	@EntityGraph(attributePaths = { "giocatori", "utentecreazione" })
	List<Tavolo> findAllByUtenteCreazione_Id(Long idUtente);

}
