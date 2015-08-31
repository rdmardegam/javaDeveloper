package br.com.javadeveloper.rest.handling;

import java.io.Serializable;


/**
 * @author Ramon Mardegam
 *
 *
 */
public class ApplicationRestException extends Exception implements Serializable {

	/**
	 * SerialVersionUI
	 */
	private static final long serialVersionUID = -1181478093214850558L;

	/** 
	 * Contem HTTP status para o response  
	 */
	Integer status;
	
	/** Codigo de erro da aplicacao */
	int codigo; 
		
	/** Link com documentação da excecao ou algum local de ajuda */	
	String link;
	
	/** Erro detalhado para o desenvolvedor*/
	String mensagemDesenvolvedor;
	
	
	public ApplicationRestException(int status, String mensagem, int codigo, String link, String mensagemDesenvolvedor) {
		super(mensagem);
		this.status = status;
		this.codigo = codigo;
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		this.link = link;
	}
	
	public ApplicationRestException(){}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMensagemDesenvolvedor() {
		return mensagemDesenvolvedor;
	}

	public void setMensagemDesenvolvedor(String mensagemDesenvolvedor) {
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}
}