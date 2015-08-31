package br.com.javadeveloper.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Classe filter para o serviço Rest para tentar adicionar o Encondig UTF-8
 * 
 * */
public class CharacterEncodingFilter implements javax.servlet.Filter {

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
