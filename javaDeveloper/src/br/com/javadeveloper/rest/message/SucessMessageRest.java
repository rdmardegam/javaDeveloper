package br.com.javadeveloper.rest.message;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ramon Mardegam
 * 
 * Classe padrao para retornos com sucesso ou informativos
 *
 */
@XmlRootElement
public class SucessMessageRest implements Serializable {

	/**
	 * SerialVersionUI
	 */
	private static final long serialVersionUID = 8830980284626243476L;

	@XmlElement(name = "sucess")
	static final boolean sucess = true;
		
	@XmlElement(name = "data")
	Object data;
		
	/** mensagem com a descricao do sucesso*/
	@XmlElement(name = "mensagem")
	String mensagem;
	

	
	public SucessMessageRest(Object data, String mensagem) {
		super();
		this.data = data;
		this.mensagem = mensagem;
	}
	
	public SucessMessageRest(Object data) {
		super();
		this.data = data;
	}
	
	public SucessMessageRest(String mensagem) {
		super();
		this.mensagem = mensagem;
	}


	public boolean isSucess() {
		return sucess;
	}


	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}


	public String getMensagem() {
		return mensagem;
	}


	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}
