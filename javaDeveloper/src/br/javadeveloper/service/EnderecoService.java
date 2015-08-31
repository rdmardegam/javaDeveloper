package br.javadeveloper.service;

import java.io.Serializable;

import br.com.javadeveloper.dto.CepDTO;
import br.com.javadeveloper.dto.EnderecoDTO;
import br.com.javadeveloper.exception.BaseException;

/**
 * @Ramon Mardegam
 *
 * Interface do servico de Endereco
 */
public interface EnderecoService extends Serializable {

	 /**
	  * Efetua pesquisa do endere�o pelo cep
	  * 
	 * @param cep
	 * @return EnderecoDTO
	 */
	 public CepDTO pesquisaCep(String cep) throws BaseException;
	
	 /**
	 * Efetua pesquisa do endere�o pelo id
	 * 
	 * @param cep
	 * @return EnderecoDTO
	 */
	public EnderecoDTO pesquisaEndereco (Long id) throws BaseException;
	 
	
	/**
	 * Cadastra um novo endere�o
	 * 
	 * @param endereco
	 * 
	 */
	public void cadastraEndereco(EnderecoDTO endereco) throws BaseException;
	
	
	/**
	 * Cadastra um novo endere�o
	 * 
	 * @param endereco
	 * 
	 */
	public void atualizaEndereco(EnderecoDTO endereco) throws BaseException;
	 
	
	 /**
	 * Deleta endereco
	 * 
	 * @param cep
	 * @return EnderecoDTO
	 */
	public void deletaEndereco (Long id) throws BaseException;
	
	
}
