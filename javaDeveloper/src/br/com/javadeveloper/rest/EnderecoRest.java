package br.com.javadeveloper.rest;

import java.lang.reflect.InvocationTargetException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import br.com.javadeveloper.dto.CepDTO;
import br.com.javadeveloper.dto.EnderecoDTO;
import br.com.javadeveloper.exception.BaseException;
import br.com.javadeveloper.exception.BusinessException;
import br.com.javadeveloper.factory.ServiceLocator;
import br.com.javadeveloper.rest.handling.ApplicationRestException;
import br.com.javadeveloper.rest.message.SucessMessageRest;
import br.com.javadeveloper.util.ValidadorUtil;
import br.javadeveloper.service.EnderecoServiceRemote;

/**
 * @author Ramon Mardegam
 *
 * Classe responsavel por enviar e receber informações de cep e endereço atraves de webService Rest
 *
 */
@Path("/endereco")
public class EnderecoRest {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EnderecoRest.class);
	
	/**
	 * Este metodo efetua a pesquisa de um cep passado pelo client.
	 * Como o method do tipo GET nao aceita um json como @Consumes para que fosse possivel receber json: a string cepJson
	 * espera um json no formato ex: {"cep":"123456789"} para que o mesmo seja transformado em json.
	 * 
	 * Ex: http://localhost:8080/JavaDeveloper/rest/endereco/buscarCep/{"cep":"04158050"}
	 * 
	 * @param cepJson
	 * @return Response
	 * @throws ApplicationRestException
	 */	
	@GET
	@Path("/buscarCep/{cep}")
	@Produces({ MediaType.APPLICATION_JSON}) // Retorno Json
	public Response buscarCep(@PathParam("cep") String cepJson) throws ApplicationRestException {
		LOGGER.debug("#init buscarCep");
		
		// Valida json e transforma no formato Json para objeto java String
		// Chama o pesquisaCep para recuperar as informacoes do cep
		CepDTO cepLocalizado = this.pesquisarCep(transformCepFromJsonAndValid(cepJson));

		SucessMessageRest sucessMessage = new SucessMessageRest(cepLocalizado);
		if (cepLocalizado == null) {
			/**Aqui poderiamos add um Status 404 Not Fund dependendo do padrao da aplicação*/
			sucessMessage.setMensagem("CEP NÃO LOCALIZADO");
		}

		LOGGER.debug("#ends buscarCep");
		
		// No caso poderiamos lançar uma excecao caso nao encontrar o cep  com Response.Status.NOT_FOUND dependendo da abordagem
		return Response.status(Response.Status.OK).entity(sucessMessage).build();
	}

	/**
	 * Este metodo efetua o cadastro de um novo Endereço
	 * 
	 * Ex chamada: http://localhost:8080/JavaDeveloper/rest/endereco/cadastrar
	 * Passando o json ex:
	 * 	
	 * {	"numero": "222",
   			"complemento" : "NONE",
   			"cepInfo": {   	"cep": "04158050", "rua": "Doutor Rosalvo de Sales32",
      						"bairro": null, "cidade": "Sao Paulo", "estado": "Sao Paulo"
   			}
		}
	 * @param endereco
	 * @return Response
	 * @throws ApplicationRestException
	 */	
	@POST
	@Path("/cadastrar")
	@Consumes({ MediaType.APPLICATION_JSON }) // Envio do Client
	@Produces({ MediaType.APPLICATION_JSON})  // Retorno do Server
	public Response cadastrar(EnderecoDTO endereco) throws ApplicationRestException {
		LOGGER.debug("#init cadastrar");

		// Valida as informacoes passadas para a inclusão
		validaInfoEndereco(endereco);

		// Verifica cep passado
		verificaCepInformado(endereco);

		try {
			// Intancia ejb remoto existente no site atual e disponibilizado para nosso Eebservice Rest
			EnderecoServiceRemote enderecoService = ServiceLocator.getInstance().getService(EnderecoServiceRemote.class);
			//Cadatra o endereco
			enderecoService.cadastraEndereco(endereco);

		} catch (BaseException e) {
			LOGGER.error(e);

			if (e instanceof BusinessException) {
				throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage(), 99,
						"javaDeveloper/endereco/method", e.getMessage());
			} else {
				throw new ApplicationRestException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
						"ERRO INTERNO, TENTE NOVAMENTE", 2, "javaDeveloper/endereco/method",
						"ERRO INTERNO, TENTE NOVAMENTE");
			}
		}

		LOGGER.debug("#fim cadastrar");
		return Response.status(Response.Status.CREATED).entity(new SucessMessageRest("Endereço Cadastrado com Sucesso")).build();
	}
	
	
	/**
	 * Este metodo efetua a alteracao de um endereco existente
	 * 
	 * Ex chamada: http://localhost:8080/JavaDeveloper/rest/endereco/atualizar
	 * Passando o json ex:
	 * {
   		"id" : 1,
   		"numero": "356",
   		"complemento" : "Prox Correios",
    		"cepInfo": {
      					"cep": "04158050", "rua": "Doutor Rosalvo de Sales",
      					"bairro": null, "cidade": "Sao Paulo", "estado": "Sao Paulo"
   			}
		}
	 * @param endereco
	 * @return Response
	 * @throws ApplicationRestException
	 */	
	@PUT
	@Path("/atualizar")
	@Consumes({ MediaType.APPLICATION_JSON }) // Envio do Client
	@Produces({ MediaType.APPLICATION_JSON }) // Retorno do Server
	public Response atualizar(EnderecoDTO endereco) throws ApplicationRestException {
		LOGGER.debug("#init cadastrar");

		// Valida as informacoes passadas para a alteracao
		validaInfoEndereco(endereco);

		// Verifica cep passado
		verificaCepInformado(endereco);

		// Caso nao informe o id do endereço a atualizar
		if (endereco.getId() == null || endereco.getId() == null || endereco.getId() == 0) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"NÃO FOI INFORMADO O ID DO ENDEREÇO", 99, "javaDeveloper/endereco/method",
					"INFORME O ID DO ENDEREÇO");
		}

		try {
			// Intancia ejb remoto existente no site atual e disponibilizado para nosso serviço Rest
			EnderecoServiceRemote enderecoService = ServiceLocator.getInstance().getService(EnderecoServiceRemote.class);

			// Atuailiza endereço e cep, neste caso o ejb irá lançar uma execaode de erro caso nao encontre o id
			enderecoService.atualizaEndereco(endereco);

		} catch (BaseException e) {
			LOGGER.error(e);
			if (e instanceof BusinessException) {
				throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage(), 99,
						"javaDeveloper/endereco/method", e.getMessage());
			} else {
				throw new ApplicationRestException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
						"ERRO INTERNO, TENTE NOVAMENTE", 2, "javaDeveloper/endereco/method",
						"ERRO INTERNO, TENTE NOVAMENTE");
			}
		}
		LOGGER.debug("#fim atualizar");
		return Response.ok().entity(new SucessMessageRest("Endereço Alterado com Sucesso")).build();
	}
	
	/**
	 * Este metodo efetua a exclusão de um endereco existente
	 * 
	 * Ex chamada: http://localhost:8080/JavaDeveloper/rest/endereco/deletar
	 * Passando o json ex:
	 *  {
   		"id" : 1
		}
		
	 * Este é um caso onde poderiamos passar apenas o id e nao o formato json, mas sim, atraves da url...
	 * 	
	 * @param endereco
	 * @return Response
	 * @throws ApplicationRestException
	 */	
	@DELETE
	@Path("/deletar")
	@Consumes({ MediaType.APPLICATION_JSON }) // Envio do Client
	@Produces({ MediaType.APPLICATION_JSON }) // Retorno do Server
	public Response deletar(EnderecoDTO endereco) throws ApplicationRestException {

		// Verifica se o id foi passado
		if (null == endereco || endereco.getId() == null || 0L == endereco.getId()) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"Não foi informado o endereço para deletar", 99, "javaDeveloper/endereco/method", "Informe o id");
		}

		try {
			// Intancia ejb remoto existente no site atual e disponibilizado para nosso Eebservice Rest
			EnderecoServiceRemote enderecoService = ServiceLocator.getInstance().getService(EnderecoServiceRemote.class);

			// Neste exemplo estarei pesquisando para identificar se existe o endereço a ser deletado, em uma aplicação real, essa validação
			// poderia estar no ejb ou nas camadas mais internas
			EnderecoDTO enderecoLocalizado = enderecoService.pesquisaEndereco(endereco.getId());
			if (enderecoLocalizado == null) {
				throw new ApplicationRestException(Response.Status.NOT_FOUND.getStatusCode(),
						"Endereço nao localizado para deletar", 99, "javaDeveloper/endereco/method", "Informe um id valido de endereço");
			}

			// Efetua o delete
			enderecoService.deletaEndereco(endereco.getId());

		} catch (BaseException e) {
			LOGGER.error(e);

			if (e instanceof BusinessException) {
				throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage(), 99,
						"javaDeveloper/endereco/method", e.getMessage());
			} else {
				throw new ApplicationRestException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
						"ERRO INTERNO, TENTE NOVAMENTE", 2, "javaDeveloper/endereco/method",
						"ERRO INTERNO, TENTE NOVAMENTE");
			}
		}

		return Response.ok().entity(new SucessMessageRest("Endereço Deletado com Sucesso")).build();
	}
	
	
	/**
	 * Este metodo efetua a pesquisa do endereço
	 * 
	 * Como o method do tipo GET nao aceita corpo na requisição ou json deixei que fosse enviado um path @PathParam neste caso. Diferenciando do buscar cep
	 * 
	 * Ex de chamada:
	 * http://localhost:8080/JavaDeveloper/rest/endereco/buscarEndereco/1
	 * 
	 * @param cepJson
	 * @return Response
	 * @throws ApplicationRestException
	 */
	@GET
	@Path("/buscarEndereco/{idEndereco}")
	@Produces({ MediaType.APPLICATION_JSON }) // Retorno do Server
	public Response buscarEndereco(@PathParam("idEndereco") Long id) throws ApplicationRestException {
		LOGGER.debug("init buscarEndereco");
		
		// Obj padrao de retorno
		SucessMessageRest sucessMessage = null;

		try {
			// Intancia ejb remoto existente no site atual e disponibilizadopara nosso Eebservice Rest
			EnderecoServiceRemote enderecoService = ServiceLocator.getInstance().getService(EnderecoServiceRemote.class);

			// Tenta localizar o endereço
			EnderecoDTO enderecoLocalizado = enderecoService.pesquisaEndereco(id);

			// Add informações no retorno
			sucessMessage = new SucessMessageRest(enderecoLocalizado);
			if (enderecoLocalizado == null) {
				// Poderiamos colocar um 404 not found dependendo do padrao da aplicacao
				sucessMessage.setMensagem("Endereço não localizado");
			}

		} catch (BaseException e) {
			LOGGER.error(e);
			
			if (e instanceof BusinessException) {
				throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage(), 99,
						"javaDeveloper/endereco/method", e.getMessage());
			} else {
				throw new ApplicationRestException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
						"ERRO INTERNO, TENTE NOVAMENTE", 2, "javaDeveloper/endereco/method",
						"ERRO INTERNO, TENTE NOVAMENTE");
			}
		}

		LOGGER.debug("fim buscarEndereco");
		return Response.status(Response.Status.OK).entity(sucessMessage).build();
	}
	
	
	/**
	 * Efetua varias tentativas de localizar o cep, efetuando tentativas de colocar 0 a direitas enquanto possivel 
	 * Ex: Existindo o cep 99900000
	 * Informando o cep:   99912345
	 * O sistema irá tentar localizar os ceps nessa sequencia ate localizar, ceps: 99912345 - 99912340 - 99912300 - 99912000 - 99910000 - 99900000   
	 * 
	 * Caso nao encontre o mesmo retorna o objeto nulo
	 * 
	 * @param numeroCep
	 * @return CepDTO
	 * @throws ApplicationRestException
	 */
	private CepDTO pesquisarCep(String numeroCep) throws ApplicationRestException {
		LOGGER.debug("init pesquisarCep");
		CepDTO cepLocalizado = null;
		
		try {
			// Verifica se o cep é valido
			if (!ValidadorUtil.isCepValido(numeroCep, true)) {
				throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(), "CEP inválido", 1,
												   "javaDeveloper/endereco/method", "CEP DEVE CONTER 8 DIGITOS");
			}

			// Tenta possibiliar a pesquisa caso o cliente tenha informado um traco no cep
			StringBuilder cepBuilder = new StringBuilder(numeroCep.replaceAll("-", ""));
			String lastCepSearch = "";
			
			// Intancia ejb remoto existente no site atual e disponibilizado para nosso Eebservice Rest
			EnderecoServiceRemote enderecoService = ServiceLocator.getInstance().getService(EnderecoServiceRemote.class);
			
			// Substitiur o digito da direita para esquerda por 0 ate localizar o cep
			for (int index = cepBuilder.length() - 1; index >= 0; index--) {
				// Evita que o mesmo cep seja pesquisado mais de uma vez,  evitando I/O server/banco de dados
				if (!lastCepSearch.equals(cepBuilder.toString())) {
					LOGGER.debug("PESQUISANDO CEP:" + cepBuilder);
					cepLocalizado = enderecoService.pesquisaCep(cepBuilder.toString());
					
					// Caso localizado para de procurar
					if (cepLocalizado != null) {
						break;
					}

					lastCepSearch = cepBuilder.toString();
				}
				// Add zero direita
				cepBuilder.setCharAt(index, '0');
			}

		} catch (BaseException e) {
			LOGGER.error(e);
			if (e instanceof BusinessException) {
				throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage(), 99,
						"javaDeveloper/endereco/method", e.getMessage());
			} else {
				throw new ApplicationRestException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
						"ERRO INTERNO, TENTE NOVAMENTE", 2, "javaDeveloper/endereco/method", "ERRO INTERNO, TENTE NOVAMENTE");
			}
		}

		LOGGER.debug("ends pesquisarCep");
		return cepLocalizado;
	}
	

	/**
	 * Metodo para transformar uma string json de cep em um objeto
	 * Criado pontualmente 
	 * 
	 * @param cepJson
	 * @return
	 * @throws ApplicationRestException
	 */
	private String transformCepFromJsonAndValid(String cepJson) throws ApplicationRestException {
		// Tenta transformar o objeto json em objeto
		Gson gson = new Gson();
		CepDTO cepObj = null;
		try {
			cepObj = gson.fromJson(cepJson, CepDTO.class);
		} catch (Exception e) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(), "JSON NO FORMATO INVALIDO",
					1, "javaDeveloper/rest/endereco/url",
					"Json deve vir no seguinte formato ex: {\"cep\" : \"12345678\" }");
		}
		

		if (cepObj == null || StringUtils.isEmpty(cepObj.getCep())) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(), "JSON NO FORMATO INVALIDO",
					1, "javaDeveloper/endereco/pesquisar/{CEP}",
					"Json deve vir no seguinte formato ex: {\"cep\" : \"12345678\" }");
		}		
		
		return cepObj.getCep();
	}
	
	
	/**
	 * Metodo responsavel por efetuar valições do endereço
	 * 
	 * @param endereco
	 * @throws ApplicationRestException
	 */
	private void validaInfoEndereco(EnderecoDTO endereco) throws ApplicationRestException {
		if (endereco == null || endereco.getCepInfo() == null) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"ENDEREÇO NÃO INFORMADO CORRETAMENTE", 1, "javaDeveloper/endereco/method/",
					"Nao foi possivel identificar nenhuma informação do endereço, informe o json corretamente.");
		}

		// Valida rua
		if (StringUtils.isEmpty(endereco.getCepInfo().getRua()) || endereco.getCepInfo().getRua().length() > 128) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"A RUA DEVE SER INFORMADA CORRETAMENTE", 1, "javaDeveloper/endereco/method/",
					"O atributo rua é obrigatório e deve ser informado com até 128 caracteres.");
		}

		// Valida Numero
		if (StringUtils.isEmpty(endereco.getNumero()) || endereco.getNumero().length() > 8) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"O NÚMERO DEVE SER INFORMADO CORRETAMENTE", 1, "javaDeveloper/endereco/method/",
					"O atributo numero é obrigatório e deve ser informado com até 8 caracteres.");
		}

		// Valida cidade
		if (StringUtils.isEmpty(endereco.getCepInfo().getCidade()) || endereco.getCepInfo().getCidade().length() > 64) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"A CIDADE DEVE SER INFORMADA CORRETAMENTE", 1, "javaDeveloper/endereco/method/",
					"O atributo cidade é obrigatório e deve ser informado com até 64 caracteres.");
		}

		// Valida estado
		if (StringUtils.isEmpty(endereco.getCepInfo().getEstado()) || endereco.getCepInfo().getEstado().length() > 64) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"O ESTADO DEVE SER INFORMADO CORRETAMENTE", 1, "javaDeveloper/endereco/method/",
					"O atributo estado é obrigatório e deve ser informado com até 64 caracteres.");
		}

		// Valida cep
		if (!ValidadorUtil.isCepValido(endereco.getCepInfo().getCep(), true)) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"O CEP DEVE SER INFORMADO CORRETAMENTE", 1, "javaDeveloper/endereco/method/",
					"O atributo cep é obrigatório e deve ser informado com até 8 dígitos.");
		}

		// Verifica os campos não obrigatórios Bairro e complemento
		if (!StringUtils.isEmpty(endereco.getCepInfo().getEstado())
				&& endereco.getCepInfo().getEstado().length() > 64) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"O BAIRRO FOI INFORMADO COM UM VALOR SUPEIOR AO PERMITIDO", 1, "javaDeveloper/endereco/method/",
					"O atributo estado não é obrigatório, caso informado, deve possuir no máximo 64 caracteres.");
		}

		if (!StringUtils.isEmpty(endereco.getComplemento()) && endereco.getComplemento().length() > 64) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(),
					"O COMPLEMENTO FOI INFORMADO COM UM VALOR SUPEIOR AO PERMITIDO", 1,
					"javaDeveloper/endereco/method/",
					"O atributo complemento não é obrigatório, caso informado, deve possuir no máximo 64 caracteres.");
		}
		
	}
	
	/**
	 * Metodo responsavel por verificar se o cep do endereço informado é localizado ou aproximadamente localizado para inclusao/alteracao
	 * 
	 *   **OBS Na minha visao, deveria se localizar exatamente o cep e nao aproximadamento como pede o exercicio.
	 *      Então, nesse exemplo, o cep será uma informação que sempre será provida pelo client e atualizado para todos os endereços que estão a ele relacionado.
	 */
	private void verificaCepInformado(EnderecoDTO endereco) throws ApplicationRestException {
		// Neste caso irei vincular o endereço ao cep aproximado localizado e irei alterar as informações deste cep para as informações providas pelo usuário
		CepDTO cepLocalizado =  pesquisarCep(endereco.getCepInfo().getCep());
				
		if(cepLocalizado == null) {
			throw new ApplicationRestException(Response.Status.BAD_REQUEST.getStatusCode(), "CEP NÃO LOCALIZADO", 99,
						"javaDeveloper/endereco/method", "CEP NÃO LOCALIZADO");
		}
		
		// Neste caso irei vincular o endereço ao cep aproximadamente localizado e irei alterar as informações deste cep 
		// para as informações providas pelo usuário, oque irá alterar todo os endereços vinculados a este cep
		try {
			String cep = cepLocalizado.getCep();
			long id = cepLocalizado.getId();
			BeanUtils.copyProperties(cepLocalizado, endereco.getCepInfo());
			cepLocalizado.setCep(cep);
			cepLocalizado.setId(id);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ApplicationRestException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "ERRO INTERNO", 500,
					"javaDeveloper/endereco/method", "ERRO INTERNO DO SERVIDOR, ENTRE EM CONTATO");
		}
		
		// Adiciona cep
		endereco.setCepInfo(cepLocalizado);
	}
}