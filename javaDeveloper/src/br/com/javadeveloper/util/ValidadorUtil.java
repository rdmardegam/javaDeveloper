package br.com.javadeveloper.util;

import org.apache.commons.lang3.StringUtils;

public abstract class ValidadorUtil {

	
	/**
	 * Verifica se o cep é valido
	 * 
	 * @param cep
	 * @param permiteSeparador
	 * @return boolean
	 */
	public static boolean isCepValido(String cep, boolean permiteSeparador) {
		boolean isValid = false;
		
		if(!StringUtils.isEmpty(cep)) {
			// caso permita separado "-" remove o mesmo para check futuro
			cep = permiteSeparador ? cep.replaceAll("-", "") : cep;
			
			// Caso cep contenha agora somente numeros e um tamanho de 8 posicoes
			if(cep.matches("[0-9]+") && cep.length() ==8) {
				isValid = true;
			}
		}
		return isValid;
	}
	
}