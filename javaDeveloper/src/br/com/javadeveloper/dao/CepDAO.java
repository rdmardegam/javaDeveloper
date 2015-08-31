package br.com.javadeveloper.dao;

import java.util.HashMap;
import java.util.Map;

import br.com.javadeveloper.dto.CepDTO;
import br.com.javadeveloper.exception.BaseException;

/**
 * CepDAO
 * 
 */
public class CepDAO extends GenericDAO<CepDAO,CepDTO> {

	/**
	 * SerialVersionUI
	 */
	private static final long serialVersionUID = -8621525357967744354L;
	
	
	/**
	 * Localiza o cep pelo cep
	 * 
	 * @param cepFilter
	 * @return CepDTO
	 * @throws BaseException
	 */
	public CepDTO findByCep(String cepFilter) throws BaseException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cepFilter", cepFilter);
		return this.find("findByCep", map);
	}

	/**
	 * Atualiza as informacoes de cep
	 * 
	 * @param cepFilter
	 * @throws BaseException
	 */
	public void atualizaCep(CepDTO cepinfo) throws BaseException {
		this.forceUpdate(cepinfo);		
	}
	
}