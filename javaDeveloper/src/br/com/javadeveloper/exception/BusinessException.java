package br.com.javadeveloper.exception;

import javax.ejb.ApplicationException;

/**
 * Deve ser utilizada quando for preciso lançar uma exceção de Negócio.
 * 
 * @author Ramon Mardegam
 * 
 */
@ApplicationException(rollback = true)
public class BusinessException extends BaseException {
    
    /** Bundle key da mensagem para internacionalizacao */
    private String messageKey;
    
    /** serialVersionUID **/
    private static final long serialVersionUID = -5607971608566843542L;
    
    /**
     * Construtor para uma <code>BusinessException</code> com o código de
     * identificação
     * 
     * @param errorCode código de identificação da falha ou erro.
     */
    public BusinessException(Integer errorCode) {
	super(errorCode);
    }

    /**
     * Construtor para uma <code>BusinessException</code> com o código de
     * identificação, mensagem, instrução e detalhe da falha ou erro.
     * 
     * @param code código de identificação da falha ou erro.
     * @param message Mensagem explicativa da falha ou erro.
     * @param instruction instrução para tratamento da falha ou erro.
     * @param detail Detalhe da falha ou erro.
     */
    public BusinessException(String code, String message, String instruction, String detail) {
	super(code, message, instruction, detail);
    }

    /**
     * Construtor para uma <code>BusinessException</code> com o código de
     * identificação, mensagem, instrução, detalhe e causa da falha ou erro.
     * 
     * @param code código de identificação da falha ou erro.
     * @param message Mensagem explicativa da falha ou erro.
     * @param instruction instrução para tratamento da falha ou erro.
     * @param cause A causa da falha ou erro.
     */
    public BusinessException(String code, String message, String instruction, Throwable cause) {
	super(code, message, instruction, "");
    }

    /**
     * Construtor para uma <code>BusinessException</code> com a mensagem da
     * falha ou erro.
     * 
     * @param message Mensagem explicativa da falha ou erro.
     */
    public BusinessException(String message) {
	super(message);
    }
    

    /**
     * Construtor para uma <code>BusinessException</code> com a causa da falha
     * ou erro.
     * 
     * @param cause A causa da falha ou erro.
     */
    public BusinessException(Throwable cause) {
	super(cause);
    }

    /**
     * Construtor para uma <code>BusinessException</code> com a mensagem e a
     * causa da falha ou erro.
     * 
     * @param message Mensagem explicativa da falha ou erro.
     * @param cause A causa da falha ou erro. Constructs a
     *            <code>BusinessException</code> with the specified detail
     *            message and the cause of the throwable.
     */
    public BusinessException(String message, Throwable cause) {
	super(message, cause);
    }
    
    /**
     *  Construtor para uma <code>BusinessException</code> com a mensagem da
     *  falha ou erro e bundle key para internacionalizacao
     * 
     * @param message Mensagem explicativa da falha ou erro
     * @param messageKey Bundle key para internacionalizacao
     */
    public BusinessException(String message, String messageKey) {
	super(message);
	this.messageKey = messageKey;
    }
    
    /**
     *  Construtor para uma <code>BusinessException</code> com a mensagem da
     *  falha ou erro, bundle key para internacionalizacao e a causa do erro
     * 
     * @param message Mensagem explicativa da falha ou erro
     * @param messageKey Bundle key para internacionalizacao
     * @param cause A causa da falha ou erro
     */
    public BusinessException(String message, String messageKey, Throwable cause) {
	super(message, cause);
	this.messageKey = messageKey;
    }

    /**
     * Retorna o Bundle key da mensagem para internacionalizacao
     * @return Bundle key da mensagem para internacionalizacao
     */
    public String getMessageKey() {
        return messageKey;
    }

}
