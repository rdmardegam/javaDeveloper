package br.com.javadeveloper.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import br.com.javadeveloper.rest.handling.ApplicationRestException;
import br.com.javadeveloper.rest.message.SucessMessageRest;
import stream.Stream;
import stream.StreamImpl;
import stream.StreamUtil;

/**
 * @author Ramon Mardegam
 *
 * Classe responsavel por receber informação para a stream
 *
 */
@Path("/stream")
public class StreamRest {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(StreamRest.class);
	
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
	@Path("/firstChar/{input}")
	@Produces({ MediaType.APPLICATION_JSON}) // Retorno Json
	public Response buscarCep(@PathParam("input") String input) throws ApplicationRestException {
		LOGGER.debug("#init firstChar");
		
		// Carregando a Stream com o input passado
		Stream stream = new StreamImpl(input);
		
		char outPut = StreamUtil.fistChart(stream);
				
		SucessMessageRest sucessMessage = 
				new SucessMessageRest(outPut, outPut==0 ? "O sistema nao conseguiu localizar nenhum caracter que nao se repete, todas as letras sao repetidas:" 
															:"Primeira letra que nao se repete encontrada com sucesso");
		
		LOGGER.debug("#ends firstChar");
		
		// No caso poderiamos lançar uma excecao caso nao encontrar o cep  com Response.Status.NOT_FOUND dependendo da abordagem
		return Response.status(Response.Status.OK).entity(sucessMessage).build();
	}
	
}