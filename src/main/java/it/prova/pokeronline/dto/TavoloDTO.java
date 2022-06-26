package it.prova.pokeronline.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

public class TavoloDTO {
	private Long id;

	@NotBlank(message = "{denominazione.notblank}")
	private String denominazione;

	@NotNull(message = "{esperienzaMinima.notNull}")
	private Integer esperienzaMinima;

	@NotNull(message = "{cifraMinima.notNull}")
	private Integer cifraMinima;

	private Date dataCreazione;

	Long[] giocatoriIds;
	
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@NotNull(message = "{utenteCreazione.notnull}")
	private UtenteDTO utenteCreazione;

	public TavoloDTO() {
		super();
	}

	public TavoloDTO(@NotBlank(message = "{denominazione.notblank}") String denominazione,
			@NotNull(message = "{esperienzaMinima.notNull}") Integer esperienzaMinima,
			@NotNull(message = "{esperienzaMinima.cifraMinima}") Integer cifraMinima, Date dataCreazione) {
		super();
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.dataCreazione = dataCreazione;
	}
	
	

	public TavoloDTO(Long id, @NotBlank(message = "{denominazione.notblank}") String denominazione,
			@NotNull(message = "{esperienzaMinima.notNull}") Integer esperienzaMinima,
			@NotNull(message = "{esperienzaMinima.cifraMinima}") Integer cifraMinima, Date dataCreazione) {
		super();
		this.id = id;
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.dataCreazione = dataCreazione;
	}
	
	public TavoloDTO(Long id, @NotBlank(message = "{denominazione.notblank}") String denominazione,
			@NotNull(message = "{esperienzaMinima.notNull}") Integer esperienzaMinima,
			@NotNull(message = "{cifraMinima.notNull}") Integer cifraMinima, Date dataCreazione,
			@NotNull(message = "{utenteCreazione.notnull}") UtenteDTO utenteCreazione) {
		super();
		this.id = id;
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.dataCreazione = dataCreazione;
		this.utenteCreazione = utenteCreazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
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

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	

	public UtenteDTO getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(UtenteDTO utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	public Long[] getGiocatoriIds() {
		return giocatoriIds;
	}

	public void setGiocatoriIds(Long[] giocatoriIds) {
		this.giocatoriIds = giocatoriIds;
	}
	public Tavolo buildTavoloModel(boolean includeIdGiocatori) {
		Tavolo result = new Tavolo(this.id, this.esperienzaMinima, cifraMinima, denominazione, dataCreazione);
		if(this.utenteCreazione!=null) {
			Utente utenteCreazione= this.utenteCreazione.buildUtenteModel(true);//new Utente(this.utenteCreazione.getId(), this.utenteCreazione.getUsername(),this.utenteCreazione.getPassword(), this.utenteCreazione.getNome(), this.utenteCreazione.getCognome(), this.utenteCreazione.getDataRegistrazione(),this.utenteCreazione.getStato(), this.utenteCreazione.getEsperienzaAccumulata(),this.utenteCreazione.getCreditoAccumulato());
			result.setUtenteCreazione(utenteCreazione);
		}
		if (includeIdGiocatori && giocatoriIds != null)
			result.setGiocatori(Arrays.asList(giocatoriIds).stream().map(id-> new Utente(id)).collect(Collectors.toSet()));
		
		return result;
	}

	public static TavoloDTO buildTavoloDTOFromModel(Tavolo tavoloModel) {
		TavoloDTO result = new TavoloDTO(tavoloModel.getId(), tavoloModel.getDenominazione(), tavoloModel.getEsperienzaMinima(),
				tavoloModel.getCifraMinima(), tavoloModel.getDataCreazione(), UtenteDTO.buildUtenteDTOFromModel(tavoloModel.getUtenteCreazione()));
		
		if (!tavoloModel.getGiocatori().isEmpty())
			result.giocatoriIds = tavoloModel.getGiocatori ().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}
	


	public static List<TavoloDTO> createTavoloDTOListFromModelList(List<Tavolo> modelListInput) {
		return modelListInput.stream().map(tavoloEntity -> {
			return TavoloDTO.buildTavoloDTOFromModel(tavoloEntity);
		}).collect(Collectors.toList());
	}

}
