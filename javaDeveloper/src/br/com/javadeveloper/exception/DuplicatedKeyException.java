package br.com.javadeveloper.exception;

/**
 * Classe de Erro de negocio para Registros Duplicados
 * @author Ramon Mardegam
 *
 */
public class DuplicatedKeyException extends BusinessException {

    /**serialVersionUID */
    private static final long serialVersionUID = 1939013287797542698L;
    
    /**
     * Construtor para uma <code>DuplicatedKeyException</code> com a causa da falha
     * ou erro.
     * 
     * @param cause A causa da falha ou erro.
     */
    public DuplicatedKeyException(Throwable cause) {
    	super("Duplicated Key", "Duplicated Key", cause);
    }

}
