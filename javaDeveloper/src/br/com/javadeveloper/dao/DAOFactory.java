package br.com.javadeveloper.dao;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * DAOFactory singleton responsavel por retornar as implementacoes dos DAO's
 * 
 * @author Ramon Mardegam
 *
 */
public abstract class DAOFactory {
    public static final String NAME = "DAOFactory";

    /*
     * Singleton Pattern
     */
    private static DAOFactory instance = null;

    protected DAOFactory() {
    	
    }

    /**
     * Retorna a unica instancia do DAOFactory
     * 
     * @return A unica instancia do DAOFactory
     */
    public static DAOFactory getInstance() {
	if (instance == null) {
	    instance = new DAOFactoryImpl();
	}
	return instance;
    }

    /**
     * Retorna o entityManager
     * 
     * @return EntityManager
     */
    public abstract SqlSessionFactory getEntityManager();

    /*
     * DAOs
     */

    /**
     * Construtor de DAOs 
     * @return A DAO informada
     */
    @SuppressWarnings("rawtypes")
    public abstract <T extends GenericDAO> T getDAO(Class<T> daoClass);

}