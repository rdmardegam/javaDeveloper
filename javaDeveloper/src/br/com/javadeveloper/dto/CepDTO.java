package br.com.javadeveloper.dto;


public class CepDTO extends BaseDTO {

	/**
	 * SErialVersioIU
	 */
	private static final long serialVersionUID = -1061525627731930046L;

	private String cep;
	
	private String rua;
	
	private String bairro;
	
	private String cidade;
	
	private String estado;

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
