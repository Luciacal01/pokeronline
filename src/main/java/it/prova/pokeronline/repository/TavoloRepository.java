package it.prova.pokeronline.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;


public interface TavoloRepository extends CrudRepository<Tavolo, Long>, CustomTavoloRepository {
	Tavolo findByDenominazione(String denominazione);
	
	@EntityGraph(attributePaths = { "giocatori", "utenteCreazione" })
	List<Tavolo> findAllByUtenteCreazione_Id(Long idUtente);
	
	@Query("from Tavolo t left join fetch t.giocatori where t.id = ?1")
	Optional<Tavolo> findByIdConUtenti(Long id);

	Optional<Tavolo> findByIdAndUtenteCreazione(long id, Utente utenteInstance);
	
	@Query("from Tavolo t join fetch t.giocatori join fetch t.utenteCreazione where t.id = ?1")
	Tavolo findByIdEager(Long id);
	
	@Query("from Tavolo t join fetch t.giocatori g where g=:utente")
	List<Tavolo> findTavoloGiocatorePresente (Utente utente);
	
	List<Tavolo> findAllByEsperienzaMinimaIsLessThanEqual(Integer esperienzaAccumulata);

}
