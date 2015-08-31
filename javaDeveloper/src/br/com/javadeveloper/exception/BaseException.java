package br.com.javadeveloper.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.ApplicationException;

/**
 * Erro generico usado para encapsular qualquer execao que tenha isntrucao e detalhe.
 */
@ApplicationException(rollback = true)
public class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2710407442339126372L;

	/**
	 * Codigo de identificacao da falha ou erro.
	 */
	private String code;

	/**
	 * Instrucao para tratamento da falha ou erro.
	 */
	private String instruction;

	/**
	 * Detalhe da falha ou erro.
	 */
	private String detail;

	/**
	 * Listagem de erros de negociacao
	 */
	private List<String> errorCodes;

	/**
	 * Informa se a Exception já foi tratada
	 */
	private boolean handled;

	/**
	 * Construtor para uma <code>BaseException</code> com o código de
	 * identificação
	 * 
	 * @param errorCode
	 *            código de identificação da falha ou erro.
	 */
	public BaseException(Integer errorCode) {
		this.code = errorCode.toString();
		this.errorCodes = new ArrayList<String>();
	}

	/**
	 * Construtor para uma <code>BaseException</code> com o código de
	 * identificação, mensagem, instrução e detalhe da falha ou erro.
	 * 
	 * @param code
	 *            código de identificação da falha ou erro.
	 * @param message
	 *            Mensagem explicativa da falha ou erro.
	 * @param instruction
	 *            instrução para tratamento da falha ou erro.
	 * @param detail
	 *            Detalhe da falha ou erro.
	 */
	public BaseException(String code, String message, String instruction, String detail) {
		super(message);
		this.code = code;
		this.instruction = instruction;
		this.detail = detail;
		this.errorCodes = new ArrayList<String>();
	}

	/**
	 * @return currentTimeMilis + nanoTime
	 */
	private String generetedUniqueCode() {
		return System.currentTimeMillis() + "-" + System.nanoTime();
	}

	/**
	 * Construtor para uma <code>BaseException</code> com a mensagem da falha ou
	 * erro.
	 * 
	 * @param message
	 *            Mensagem explicativa da falha ou erro.
	 */
	public BaseException(String message) {
		super(message);
		this.code = generetedUniqueCode();
		this.errorCodes = new ArrayList<String>();
	}

	/**
	 * Construtor para uma <code>BaseException</code> com a causa da falha ou
	 * erro.
	 * 
	 * @param cause
	 *            A causa da falha ou erro.
	 */
	public BaseException(Throwable cause) {
		super(cause);
		this.code = generetedUniqueCode();
		this.errorCodes = new ArrayList<String>();
	}

	/**
	 * Construtor para uma <code>BaseException</code> com a mensagem e a causa
	 * da falha ou erro.
	 * 
	 * @param message
	 *            Mensagem explicativa da falha ou erro.
	 * @param cause
	 *            A causa da falha ou erro. Constructs a
	 *            <code>BaseException</code> with the specified detail message
	 *            and the cause of the throwable.
	 */
	public BaseException(String message, Throwable cause) {
		super(message, cause);
		this.code = generetedUniqueCode();
		this.errorCodes = new ArrayList<String>();
	}

	/**
	 * Obtém o código de identificação da falha ou erro.
	 * 
	 * @return O código de identificação da falha ou erro.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Atribui o código de identificação da falha ou erro.
	 * 
	 * @param code
	 *            O código de identificação da falha ou erro para atribuir.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Obtém a instrução para tratamento da falha ou erro.
	 * 
	 * @return A instrução para tratamento da falha ou erro.
	 */
	public String getInstruction() {
		return instruction;
	}

	/**
	 * Atribui a instrução para tratamento da falha ou erro.
	 * 
	 * @param instruction
	 *            A instrução para tratamento da falha ou erro para atribuir.
	 */
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	/**
	 * Obtém o detalhe da falha ou erro.
	 * 
	 * @return o detalhe da falha ou erro.
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * Atribui o detalhe da falha ou erro.
	 * 
	 * @param detail
	 *            O detalhe da falha o erro para atribuir.
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * Obtém a listagem dos códigos de erro
	 * 
	 * @return os códigos de erro
	 */
	public List<String> getErrorCodes() {
		if (this.errorCodes == null) {
			this.errorCodes = new ArrayList<String>();
		}
		return this.errorCodes;
	}

	/**
	 * Atribui a listagem dos códigos de erro
	 * 
	 * @param errorCodes
	 *            Os códigos de erro
	 */
	public void setErrorCodes(List<String> errorCodes) {
		this.errorCodes = errorCodes;
	}

	/**
	 * Adiciona um código de erro à lista de erros
	 * 
	 * @param errorCode
	 */
	public void addErrorCode(String errorCode) {
		this.getErrorCodes().add(errorCode);
	}

	/**
	 * Retorna uma String com a stack trace.
	 * 
	 * @return stack trace.
	 */
	public String getExceptionStack() {
		String exceptionStack = null;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			printStackTrace(pw);
			exceptionStack = sw.toString();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			}
			if (pw != null) {
				try {
					pw.close();
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			}
		}

		return exceptionStack;
	}

	/**
	 * toString
	 */
	public String toString() {
		if (this.code == null) {
			return super.toString();
		} else {
			return "Error Code[" + code + "] " + super.getMessage();
		}
	}

	/**
	 * Informa se a Exception já foi tratada
	 * 
	 * @return true - Tratada <br/>
	 *         false - Não tratada
	 */
	public boolean isHandled() {
		return handled;
	}

	/**
	 * Altera a informação de que a Exception foi tratada
	 * 
	 * @param handled
	 *            - true para Tratada, ou false para Não tratada
	 */
	public void setHandled(boolean handled) {
		this.handled = handled;
	}

}
