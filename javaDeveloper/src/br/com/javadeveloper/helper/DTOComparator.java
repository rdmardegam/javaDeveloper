package br.com.javadeveloper.helper;

import org.apache.commons.lang3.builder.EqualsBuilder;

import br.com.javadeveloper.dto.BaseDTO;
import br.com.javadeveloper.exception.TechnicalException;

public class DTOComparator {
    
    /**
     * Constructor da classe
     */
	private DTOComparator() {
		super();
	}
    
    /**
     * Compara dois objetos e verifica se tem diferenças entre eles
     * @param a
     * @param b
     * @return
     * @throws TechnicalException
     */
    public static <T extends BaseDTO> boolean hasDifferences(T a, T b) throws TechnicalException {
		try {
		    return !EqualsBuilder.reflectionEquals(a, b);
		} catch (Exception e) {
		    throw new TechnicalException(e);
		}
    }


}
