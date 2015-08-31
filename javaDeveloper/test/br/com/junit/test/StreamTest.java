package br.com.junit.test;

import org.junit.Assert;
import org.junit.Test;

import stream.Stream;
import stream.StreamImpl;
import stream.StreamUtil;

/**
 * @author Ramon Mardegam
 *
 * Classe de test junit para Stream e StreamUtil
 *
 */
public class StreamTest {

	
	/**
	 * Teste onde Stream nao possui mais resultados 
	 */
	@Test
	public void streamHasNextFalseTest() {
		Stream stream = new StreamImpl("");
		Assert.assertTrue( false == stream.hasNext());
	}
	
	/**
	 * Teste onde Stream possui mais resultados 
	 */
	@Test
	public void streamHasNextTrueTest() {
		Stream stream = new StreamImpl(" ");
		Assert.assertTrue( true == stream.hasNext());
	}
	
	/**
	 * Teste onde Stream retorna o caracter esperado 
	 */
	@Test
	public void streamNextTrueTest() {
		Stream stream = new StreamImpl("A");
		Assert.assertTrue('A' == stream.getNext());
	}
	
	/**
	 * Teste onde Stream lança uma excecao por nao existir mais resultado e mesmo assim ser solicitado
	 */
	@Test(expected = StringIndexOutOfBoundsException.class)
	public void streamNextNullPointerTest() {
		Stream stream = new StreamImpl("");
		stream.getNext();
	}
	
	/**
	 * Teste onde StreamUtil encontra o primeiro caracter esperado que nao se repete  
	 */
	@Test
	public void streamSimplesTest() {
		Stream stream = new StreamImpl("aAbBABac");
		char  out = StreamUtil.fistChart(stream);
		Assert.assertTrue('b' == out);
	}
	
	/**
	 * Teste onde StreamUtil identifica que nao existe primeiro caracter que nao se repete e retorna vazio 
	 */
	@Test
	public void streamVazioTest() {
		Stream stream = new StreamImpl("");
		char  out = StreamUtil.fistChart(stream);
		// Representa o vazioem char
		Assert.assertTrue(0 == out);
	}
	
	/**
	 * Teste onde StreamUtil identifica que nao existe primeiro caracter e somente a informação null e retorna vazio 
	 */
	@Test
	public void streamStringNullTest() {
		Stream stream = new StreamImpl(null);
		char  out = StreamUtil.fistChart(stream);
		Assert.assertTrue(0 == out);
	}
	
	/**
	 * Teste onde StreamUtil encontra o primeiro caracter esperado que nao se repete  em uma grande String
	 */
	@Test
	public void streamGrandeTest() {
		Stream stream = new StreamImpl("QWERTYUIOPASDFGHJKLÇZXCVBNM<>:?qwertyuiopasdfghjklçzxcvbnm,.;/1234567890QWhuihsaduiasd7621378912n asxda s asdasuda7162378912738912daskjdase12834712 c7fdshgry3u2432746asd123487");
		char  out = StreamUtil.fistChart(stream);
		Assert.assertTrue('E' == out);
	}
	
	@Test
	public void streamGrandeTodosRepeteTest() {
		Stream stream = new StreamImpl("ABCDEFGHIJKLMNOPQABCDEFGHIJKLMNOPQ");
		char  out = StreamUtil.fistChart(stream);
		Assert.assertTrue(0 == out);
	}
	
	/**
	 * Teste onde Stream lança uma excecao por nao ser enviado a interface Stream
	 */
	@Test(expected = NullPointerException.class)  
	public void streamNullPointerTest() {
		StreamUtil.fistChart(null);
	}
	
}