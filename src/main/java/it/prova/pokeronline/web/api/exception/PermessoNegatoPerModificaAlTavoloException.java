package it.prova.pokeronline.web.api.exception;

public class PermessoNegatoPerModificaAlTavoloException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PermessoNegatoPerModificaAlTavoloException(String message) {
		super(message);
	}
}
