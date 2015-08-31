package stream;

import org.apache.commons.lang3.StringUtils;



/**
 * @author Ramon Mardegam
 * 
 * Implementacao da interface Stream
 *
 */
public class StreamImpl implements Stream {

	// String de input, que deve ser passada no construtor
	private String input;
	
	// Index que por dafault inicia negativo
	private int index =-1;
	
	/**
	 * Construtor padrao recebendo a String input
	 * */
	public StreamImpl(String input) {
		super();
		this.input = input;
	}

	@Override
	public char getNext() {
		return input.charAt(++index);
	}

	@Override
	public boolean hasNext() {
		boolean hasNext = true;
		// Caso vazio ou nula ou index+1 maior que o tamanho do input
		if(StringUtils.isEmpty(input) || index+1>=input.length() ) {
			hasNext  = false;
		}
		
		return hasNext;
	}
}
