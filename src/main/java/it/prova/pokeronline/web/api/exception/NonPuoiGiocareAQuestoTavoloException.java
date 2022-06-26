package it.prova.pokeronline.web.api.exception;

public class NonPuoiGiocareAQuestoTavoloException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public NonPuoiGiocareAQuestoTavoloException(String message) {
		super(message);
	}
}
