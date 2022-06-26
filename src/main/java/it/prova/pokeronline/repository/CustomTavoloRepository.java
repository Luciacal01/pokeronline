package it.prova.pokeronline.repository;

import java.util.List;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

public interface CustomTavoloRepository {
	public List<Tavolo> findByExample(Tavolo example, Utente utente);

	List<Tavolo> findByExampleAdmin(Tavolo example);
}
