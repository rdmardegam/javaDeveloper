package br.com.javadeveloper.rest.handling;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import br.com.javadeveloper.rest.message.ErrorMessageRest;
/**
 * Handle default de erro caso o erro nao seja capturado por nenhum handle propositalmente ou ocorra um erro na aplicacao
 * 
 * */
public class DefaultRestExceptionHandler implements ExceptionMapper<Throwable> {

	public Response toResponse(Throwable ex) {
		ErrorMessageRest errorMessage = new ErrorMessageRest();		
		setHttpStatus(ex, errorMessage);
		
		// Adiciona um erro Generico
		errorMessage.setCodigo(5001);
		errorMessage.setMensagem((ex.getMessage()));
		
		// Recupera o StrackTrace e o adiciona ao desenvolvedor
		StringWriter errorStackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(errorStackTrace));
		
		// Erro completo ao desenvolvedor, em tempo de desenolvimento é interessante utilizar
		errorMessage.setMensagemDesenvolvedor(errorStackTrace.toString());
		// local web
		errorMessage.setLink("http:\\javadeveloper\rest");
				
		return Response.status(errorMessage.getStatus())
				.entity(errorMessage)
				.type(MediaType.APPLICATION_JSON)
				.build();	
	}

	private void setHttpStatus(Throwable ex, ErrorMessageRest errorMessage) {
		// Tentanando identificar o status do erro
		if(ex instanceof WebApplicationException ) {  
			errorMessage.setStatus(((WebApplicationException)ex).getResponse().getStatus());
		} else {
			//Default error 500 - Erro interno server
			errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); 
		}
	}
}