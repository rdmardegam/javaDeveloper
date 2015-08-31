package br.com.javadeveloper.rest.message;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;

import br.com.javadeveloper.rest.handling.ApplicationRestException;


/**
 * @author Ramon Mardegam
 * 
 * Classe padrao para retornos com erro
 *
 */
@XmlRootElement(name="error")
public class ErrorMessageRest implements Serializable {
	/**
	 * SerialVersionUI
	 */
	private static final long serialVersionUID = 1962677368528112732L;

	@XmlElement(name = "sucess")
	static final boolean  sucess = false;
	
	/** contem o HTTP status que deve ser retornado pelo server  */
	@XmlElement(name = "status")
	int status;
	
	/** codigo de erro da aplicacao */
	@XmlElement(name = "codigoErro")
	int codigo;
	
	/** mensagem com a descricao do erro*/
	@XmlElement(name = "mensagem")
	String mensagem;
		
	/** link contendo o ponto de erro documentado*/
	@XmlElement(name = "link")
	String link;
	
	/** uma informacao extra para desenvolvedor */
	@XmlElement(name = "mensagemDesenvolvedor")
	String mensagemDesenvolvedor;	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagemDesenvolvedor() {
		return mensagemDesenvolvedor;
	}

	public void setMensagemDesenvolvedor(String mensagemDesenvolvedor) {
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
		
	public boolean isSucess() {
		return sucess;
	}

	public ErrorMessageRest (ApplicationRestException ex){
		try {
			BeanUtils.copyProperties(this, ex);
			this.mensagem =ex.getMessage(); 
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public ErrorMessageRest (NotFoundException ex){
		this.status = Response.Status.NOT_FOUND.getStatusCode();
		this.codigo = Response.Status.NOT_FOUND.getStatusCode();
		this.mensagem = ex.getMessage();
		this.link = "https://docs.jboss.org/resteasy/docs/1.2.GA/javadocs/org/jboss/resteasy/spi/NotFoundException.html";
		this.mensagemDesenvolvedor = "Foi solicitada uma url nao reconhecida pelo sistema";
	}

	public ErrorMessageRest() {}
	
}
