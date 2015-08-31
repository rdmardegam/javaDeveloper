package br.javadeveloper.service.impl;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.com.javadeveloper.dao.CepDAO;
import br.com.javadeveloper.dao.DAOFactory;
import br.com.javadeveloper.dao.EnderecoDAO;
import br.com.javadeveloper.dto.CepDTO;
import br.com.javadeveloper.dto.EnderecoDTO;
import br.com.javadeveloper.exception.BaseException;
import br.javadeveloper.service.EnderecoServiceLocal;
import br.javadeveloper.service.EnderecoServiceRemote;

/**
 * @author Ramon Mardegam
 *
 * Implementacao do EJB remoto e local de Endereco
 *
 */
@Stateless(name="EnderecoService")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnderecoServiceImpl implements EnderecoServiceLocal, EnderecoServiceRemote {
	/**
	 * SerialVersionUI
	 */
	private static final long serialVersionUID = 6824208729516887032L;

	
	 /**
     * DAO de acesso as informações de usuario.
     */
    private EnderecoDAO enderecoDAO;
    private CepDAO cepDAO;
    
    /* Methods */
    /**
     * Instancia o userDAO recuperado pelo DAOFactory
     */
    @PostConstruct
    public void init() {
    	this.cepDAO = DAOFactory.getInstance().getDAO(CepDAO.class);
    	this.enderecoDAO = DAOFactory.getInstance().getDAO(EnderecoDAO.class);
    	
    }
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CepDTO pesquisaCep(String cep) throws BaseException {
		return cepDAO.findByCep(cep);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cadastraEndereco(EnderecoDTO endereco) throws BaseException {
		// Atualiza o cep para os valores passados pelo usuario
		cepDAO.forceUpdate(endereco.getCepInfo());
		
		// Inseri o endereco relacionado ao cep
		enderecoDAO.insert(endereco);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnderecoDTO pesquisaEndereco(Long id) throws BaseException {
		return enderecoDAO.find(id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizaEndereco(EnderecoDTO endereco) throws BaseException {
		// Atualiza o cep para os valores passados pelo usuario
		cepDAO.forceUpdate(endereco.getCepInfo());
		// atualiza endereco
		enderecoDAO.update(endereco);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletaEndereco(Long id) throws BaseException {
		enderecoDAO.delete(id);
	}
	
}