package br.com.javadeveloper.dao;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

/**
 * Implementacao do DAOFactory
 * 
 * @author Ramon Mardegam
 *
 */
public class DAOFactoryImpl extends DAOFactory {
    private static final Logger LOG = Logger.getLogger(DAOFactoryImpl.class);

    private SqlSessionFactory manager;

    /**
     * @see DAOFactory#getEntityManager()
     */
    @Override
    public SqlSessionFactory getEntityManager() {
	if (this.manager == null) {
	    Reader reader = null;
	    try {
		reader = Resources.getResourceAsReader("mybatis.javadeveloper.config.xml");
	    } catch (IOException e) {
		LOG.error("Erro ao inicializar o mybatis", e);
	    }
	    this.manager = new SqlSessionFactoryBuilder().build(reader, System.getenv("environment-dev"));
	}
	return this.manager;
    }

    /*
     * DAOs
     */

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public <T extends GenericDAO> T getDAO(Class<T> daoClass) {
	try {
	    return daoClass.newInstance();
	    
	} catch(Exception e) {
	    LOG.error("Erro ao instanciar o DAO "+(daoClass != null ? daoClass.getName() : "null"), e);
	}
	
	return null;
    }

}