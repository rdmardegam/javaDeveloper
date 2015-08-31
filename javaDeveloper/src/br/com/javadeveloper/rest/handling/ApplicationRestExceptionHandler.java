package br.com.javadeveloper.rest.handling;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.javadeveloper.rest.message.ErrorMessageRest;


/**
 * @author Ramon Mardegam
 *
 * Handler que captura a excecao ApplicationRestException e retorna uma informação em formato json para usuario
 */
@Provider
public class ApplicationRestExceptionHandler implements ExceptionMapper<ApplicationRestException> {
	
	public Response toResponse(ApplicationRestException ex) {
		return Response.status(ex.getStatus())
				.entity(new ErrorMessageRest(ex))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}	
}