package stream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author Ramon Mardegam
 * 
 * Classe que trabalha com o Stream 
 **/
public class StreamUtil {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(StreamUtil.class);
	
	
	/**
	 * Metodo responsavel por identificar a primera letra contida no Stream que nao se repete ao longo da sua leitura
	 * 
	 * Caso todas as letras sejam repetidas, retorna vazio representado por 0 ou '\u0000'
	 * 
	 * @param input
	 * @return char
	 */
	public static char fistChart(Stream stream) {
		// Hash que contem todos os repetidos
		Set<Character> letrasrepetidas = new HashSet<Character>();
		
		// Array Ordenado
		List<Character> letrasNaoRepetidas = new ArrayList<Character>();
		
		// Recuperando letra a letra ate que todas sejam lidas
		while(stream.hasNext()) {
			
			// recupera letra
			char letra = stream.getNext();
			
			// Caso a mesma letra ja seja repetida, contiua para a proxima
			if (letrasrepetidas.contains(letra)) {
				continue;
			}
			
			// Caso identifique que a letra agora se repete
			if (letrasNaoRepetidas.contains(letra)) {
				// Remove das nao repetidas - caso exista
				letrasNaoRepetidas.remove((Character) letra);
				// Add nas repetidas
				letrasrepetidas.add(letra);
			} else {
				// adiciona nas nao repetidas
				letrasNaoRepetidas.add(letra);
			}
		}
		
		// Apresenta mensagem amigavel caso a letra seja repetida
		if(letrasNaoRepetidas.isEmpty()){
			LOGGER.info("O sistema nao conseguiu localizar nenhum caracter que nao se repete, todas letras repetidas:"+letrasrepetidas);
			System.out.println("O sistema nao conseguiu localizar nenhum caracter que nao se repete, todas letras repetidas:"+letrasrepetidas);
		}
		
		// Retorna o primeiro valor encontrado ou vazio, caso não exista repetido
		return letrasNaoRepetidas.isEmpty() ? 0 : letrasNaoRepetidas.get(0);
	}
}
