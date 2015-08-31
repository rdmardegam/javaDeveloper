package br.com.javadeveloper.dto;

public class EnderecoDTO extends BaseDTO {
	
	/**
	 * SerialVersionUI
	 */
	private static final long serialVersionUID = 8024182757847943426L;

	/**Representa o numero/Letra referente a casa*/
	private String numero;
	
	private String complemento;

	private CepDTO cepInfo;
	
	
	public CepDTO getCepInfo() {
		return cepInfo;
	}

	public void setCepInfo(CepDTO cepInfo) {
		this.cepInfo = cepInfo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
}