package br.com.javadeveloper.factory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Ramon Mardegam
 * 
 * Classe ServiceLocator para instanciar os EJBs.
 * 
 *
 */
final public class ServiceLocator {
    /** LOGGER */
    private static final Logger LOGGER = Logger.getLogger(ServiceLocator.class);
    
    /** Singleton do ServiceLocator */
    private static ServiceLocator instance = null;
    
    /** InitialContext para lookup */
    private Context ctx = null;
    
    /** Cache para criar singleton dos serviços */
    private Map<String, Object> cache = null;
    
    /** Flag para informar se o cache esta ativo */
    private boolean cacheEnabled = false;
    
    /**
     * Constructor do Singleton
     * @throws NamingException
     */
    private ServiceLocator() throws NamingException {
		String cacheEnabledProp = System.getProperty("ServiceLocator.cacheEnabled");
		this.cacheEnabled = cacheEnabledProp != null ? cacheEnabledProp.equals("true") : false;
		this.ctx = new InitialContext();

		if (this.cacheEnabled) {
			LOGGER.debug("ServiceLocator.cacheEnabled = true");
			this.cache = Collections.synchronizedMap(new HashMap<String, Object>());
		}
    }
    
    /**
	 * Retorna o Singleton do ServiceLocator
	 * 
	 * @return Singleton do ServiceLocator
	 */
	public static ServiceLocator getInstance() {
		if (instance == null) {
			try {
				instance = new ServiceLocator();
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
		return instance;
    }
    
    /**
     * Cria instancia do servidor informado
     * @param classInterface Interface do EJB
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> classInterface) {
		T result = null;
	        try {
	            String className = classInterface.getSimpleName();
	            LOGGER.debug(String.format("Service %s solicitado", className));
	        	
	            if(cacheEnabled && cache.containsKey(className)) {
	            	result = (T) cache.get(className);
	            	LOGGER.debug("Servico recuperado do cache.");
	            } else {
	            	String appName = (String) ctx.lookup("java:app/AppName"); 
	            	
	            	result = (T) ctx.lookup(new StringBuilder("java:global/").append(appName).append("/").append(StringUtils.removeEnd(className, "Remote")) .append("!").append(classInterface.getCanonicalName()).toString());
	            	
	            	
	            	if(cacheEnabled) {
	            	    LOGGER.debug("Adicionando servico ao cache.");
	            	    cache.put(className, result);
	            	}
	            }
	        } catch(Exception e) {
	            LOGGER.error(e);
	        }
		return result;
    }
}