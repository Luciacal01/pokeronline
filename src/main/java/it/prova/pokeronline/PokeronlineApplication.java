package it.prova.pokeronline;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.RuoloService;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;


@SpringBootApplication
public class PokeronlineApplication implements CommandLineRunner {
	
	@Autowired
	private RuoloService ruoloServiceInstance;
	@Autowired
	private UtenteService utenteServiceInstance;
	@Autowired
	private TavoloService tavoloService;
	
	public static void main(String[] args) {
		SpringApplication.run(PokeronlineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Administrator", Ruolo.ROLE_ADMIN));
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Player", Ruolo.ROLE_PLAYER) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Player", Ruolo.ROLE_PLAYER));
		}
		
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("SpecialPlayer", Ruolo.ROLE_SPECIAL_PLAYER) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("SpecialPlayer", Ruolo.ROLE_SPECIAL_PLAYER));
		}

		// a differenza degli altri progetti cerco solo per username perche' se vado
		// anche per password ogni volta ne inserisce uno nuovo, inoltre l'encode della
		// password non lo
		// faccio qui perche gia lo fa il service di utente, durante inserisciNuovo
		if (utenteServiceInstance.findByUsername("admin") == null) {
			Utente admin = new Utente("admin", "admin", "Giovanni", "Rossi", new Date());
			admin.getRuoli().add(ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN));
			utenteServiceInstance.inserisciNuovo(admin);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(admin.getId());
		}

		if (utenteServiceInstance.findByUsername("player") == null) {
			Utente classicUser = new Utente("player", "player", "Antonio", "Verdi", new Date());
			classicUser.getRuoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Player", Ruolo.ROLE_PLAYER));
			utenteServiceInstance.inserisciNuovo(classicUser);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(classicUser.getId());
		}

		if (utenteServiceInstance.findByUsername("specialplayer") == null) {
			Utente classicUser2 = new Utente("specialplayer", "specialplayer", "Giuseppe", "Verdiii", new Date());
			classicUser2.getRuoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("SpecialPlayer", Ruolo.ROLE_SPECIAL_PLAYER));
			utenteServiceInstance.inserisciNuovo(classicUser2);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(classicUser2.getId());
		}
		
		String denominazione="Tavolo 1";
		Tavolo tavolo= tavoloService.findByDenominazione(denominazione);
		
		if(tavolo==null) {
//			Set<Utente> giocatori= new HashSet<Utente>();
//			giocatori.add(utenteServiceInstance.findByUsername("specialplayer"));
//			giocatori.add(utenteServiceInstance.findByUsername("player"));
			tavolo= new Tavolo(11, 1, denominazione, new Date(), utenteServiceInstance.findByUsername("admin"));
			tavoloService.inserisciNuovo(tavolo);
		}
		
		String denominazione2="Tavolo 2";
		Tavolo tavolo2= tavoloService.findByDenominazione(denominazione2);
		
		if(tavolo2==null) {
			tavolo2= new Tavolo(0, 10, denominazione, new Date(), utenteServiceInstance.findByUsername("admin"));
			Set<Utente> giocatori= new HashSet<Utente>();
			giocatori.add(utenteServiceInstance.findByUsername("specialplayer"));
			giocatori.add(utenteServiceInstance.findByUsername("player"));
			tavolo2.setGiocatori(giocatori);
			tavoloService.inserisciNuovo(tavolo2);
		}
		
	}

}
