package it.prova.pokeronline.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import it.prova.pokeronline.dto.UtenteDTO;


@Entity
@Table(name = "Tavolo")
public class Tavolo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "esperienzaMinima")
	private Integer esperienzaMinima;
	
	@Column(name = "cifraMinima")
	private Integer cifraMinima;
	
	@Column(name = "denominazione")
	private String denominazione;
	
	@Column(name = "dataCreazione")
	private Date dataCreazione;
	
	@OneToMany
	private Set<Utente> giocatori = new HashSet<Utente>(0);
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utentecreazione_id", referencedColumnName = "id", nullable = false)
	private Utente utenteCreazione;
	
	
	
	public Tavolo() {
		super();
	}

	public Tavolo(Integer esperienzaMinima, Integer cifraMinima, String denominazione, Date dataCreazione) {
		super();
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
	}

	public Tavolo(Long id, Integer esperienzaMinima, Integer cifraMinima, String denominazione, Date dataCreazione) {
		super();
		this.id = id;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
	}

	public Tavolo(Integer esperienzaMinima, Integer cifraMinima, String denominazione, Date dataCreazione,
			Set<Utente> giocatori) {
		super();
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
		this.giocatori = giocatori;
	}

	public Tavolo(Integer esperienzaMinima, Integer cifraMinima, String denominazione, Date dataCreazione,
			Utente utenteCreazione) {
		super();
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
		this.utenteCreazione = utenteCreazione;
	}

	public Tavolo(Integer esperienzaMinima, Integer cifraMinima, String denominazione, Date dataCreazione,
			Set<Utente> giocatori, Utente utenteCreazione) {
		super();
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
		this.giocatori = giocatori;
		this.utenteCreazione = utenteCreazione;
	}

	

	public Tavolo(Long id, Integer esperienzaMinima, Integer cifraMinima, String denominazione, Date dataCreazione,
			Utente utenteCreazione) {
		super();
		this.id = id;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
		this.utenteCreazione = utenteCreazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEsperienzaMinima() {
		return esperienzaMinima;
	}

	public void setEsperienzaMinima(Integer esperienzaMinima) {
		this.esperienzaMinima = esperienzaMinima;
	}

	public Integer getCifraMinima() {
		return cifraMinima;
	}

	public void setCifraMinima(Integer cifraMinima) {
		this.cifraMinima = cifraMinima;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Set<Utente> getGiocatori() {
		return giocatori;
	}

	public void setGiocatori(Set<Utente> giocatori) {
		this.giocatori = giocatori;
	}

	public Utente getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(Utente utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}
}
