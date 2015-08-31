package br.com.javadeveloper.exception;

import javax.ejb.ApplicationException;

/**
 * Deve ser utilizada quando for preciso lançar uma exceção Técnica.
 * 
 * @author Ramon Mardegam
 * 
 */
@ApplicationException(rollback = true)
public class TechnicalException extends BaseException {

	private static final long serialVersionUID = 8075275395338088829L;

	/**
	 * Construtor para uma <code>TechnicalException</code> com o código de
	 * identificação, mensagem, instrução e detalhe da falha ou erro.
	 * 
	 * @param code
	 *            código de identificação da falha ou erro.
	 * @param message
	 *            Mensagem explicativa da falha ou erro.
	 * @param instruction
	 *            instrução para tratamento da falha ou erro.
	 * @param detail
	 *            Detalhe da falha ou erro.
	 */
	public TechnicalException(String code, String message, String instruction,
			String detail) {
		super(code, message, instruction, detail);
	}

	/**
	 * Construtor para uma <code>TechnicalException</code> com o código de
	 * identificação, mensagem, instrução, detalhe e causa da falha ou erro.
	 * 
	 * @param code 
	 * 			de identificacao da falha ou erro.
	 * 
	 * @param message
	 *            Mensagem explicativa da falha ou erro.
	 * @param instruction
	 *            instrucao para tratamento da falha ou erro.
	 * @param cause
	 *            A causa da falha ou erro.
	 */
	public TechnicalException(String code, String message, String instruction,
			Throwable cause) {
		super(code, message, instruction, "");
	}

	/**
	 * Construtor para uma <code>BaseException</code> com a mensagem da falha ou
	 * erro.
	 * 
	 * @param message
	 *            Mensagem explicativa da falha ou erro.
	 */
	public TechnicalException(String message) {
		super(message);
	}

	/**
	 * Construtor para uma <code>TechnicalException</code> com a causa da falha
	 * ou erro.
	 * 
	 * @param cause
	 *            A causa da falha ou erro.
	 */
	public TechnicalException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construtor para uma <code>TechnicalException</code> com a mensagem e a
	 * causa da falha ou erro.
	 * 
	 * @param message
	 *            Mensagem explicativa da falha ou erro.
	 * @param cause
	 *            A causa da falha ou erro. Constructs a
	 *            <code>ServiceException</code> with the specified detail
	 *            message and the cause of the throwable.
	 */
	public TechnicalException(String message, Throwable cause) {
		super(message, cause);
	}

}
