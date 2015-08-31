package br.com.javadeveloper.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import br.com.javadeveloper.dto.BaseDTO;
import br.com.javadeveloper.exception.BaseException;
import br.com.javadeveloper.exception.BusinessException;
import br.com.javadeveloper.exception.DuplicatedKeyException;
import br.com.javadeveloper.exception.TechnicalException;
import br.com.javadeveloper.helper.DTOComparator;

/**
 * @author Ramon Mardegam
 * 
 * DAO GENERICA que deve ser extendido por todos os DAO's da aplicacao que
 * queiram utilizar o MyBatis
 * 
 *	
 */
public abstract class GenericDAO<E, T extends BaseDTO> implements Serializable {
    
    /** serialVersionUID */
    private static final long serialVersionUID = -8858151246267266279L;

    /** LOGGER */
    private static final Logger LOGGER = Logger.getLogger(GenericDAO.class);

    /** Nome padrao do SQL para listar os registros */
    private static final String PREFIX_LIST_QUERY = "list";
    
    /** Nome padrao do SQL para retornar um unico registro */
    private static final String PREFIX_FIND_QUERY = "find";
    
    /** Nome padrao do SQL para inserir um registro */
    private static final String PREFIX_INSERT_QUERY = "insert";
    
    /** Nome padrao do SQL para atualizar um registro */
    private static final String PREFIX_UPDATE_QUERY = "update";
    
    /** Nome padrao do SQL para remover um registro */
    private static final String PREFIX_DELETE_QUERY = "delete";
    
    /** Codigo do oracle para registros duplicados */
    private static final String ORA_DUPLICATED_RECORD = "DUPLICATE ENTRY";

    /** Session Factory do myBatis */
    private SqlSessionFactory sf;
    
    /** Tipo da classe da especificacao do DAO */
    private Class<E> daoType;

    /**
     * Construtor
     * 
     */
    @SuppressWarnings("unchecked")
    public GenericDAO() {
        // Armazenando o tipo da Classe DAO via reflection
        Type type = getClass().getGenericSuperclass();
               
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                this.daoType = (Class<E>) parameterizedType.getActualTypeArguments()[0];
            } else {
                this.daoType = null;
            }        
            
        this.sf = DAOFactory.getInstance().getEntityManager();
    }
   
    /**
     * Retorna um registro com base nos filtros/modelo informado(s).
     * Esse metodo utiliza o SQL {@link GenericDAO#PREFIX_FIND_QUERY} para pesquisar o registro
     * 
     * @param filter Filtros ou Modelo para pesquisa
     * @return DTO recuperado do banco de dados
     * @throws TechnicalException
     */
    public <R> R find(Object filter) throws TechnicalException {
    	return find(PREFIX_FIND_QUERY, filter);
    }

    /**
     * Retorna um registro com base nos filtros/modelo informado(s) e no SQL (queryName).
     * 
     * @param queryName Nome da query mapeada
     * @param filter Filtros ou Modelo para pesquisa
     * @return DTO recuperado do banco de dados
     * @throws TechnicalException
     */
    @SuppressWarnings("unchecked")
    public <R> R find(String queryName, Object filter) throws TechnicalException {
        SqlSession session = openSession();
        R obj = null;
        try {
            long t1 = new Date().getTime();
            String query = buildQueryName(queryName);
            obj = (R) session.selectOne(query, filter);
            LOGGER.debug("Query " + query + " executed in " + (new Date().getTime() - t1) + "ms");
        } catch (Exception e) {
            LOGGER.error(e);
            throw new TechnicalException(e);
        } finally {
            closeSession(session);
        }
        return obj;
    }
    
    /**
     * Retorna uma lista de registros, utilizando o SQL {@link GenericDAO#PREFIX_LIST_QUERY} para realizar a pesquisa.
     * 
     * @return List de DTO's recuperados do banco de dados
     * @throws TechnicalException
     */
    @SuppressWarnings("unchecked")
    public List<T> list() throws TechnicalException {
    	return (List<T>) this.list(PREFIX_LIST_QUERY, null);
    }
    
    /**
     * Retorna uma lista de registros com base nos filtros/modelo informado(s), 
     * utilizando o SQL {@link GenericDAO#PREFIX_LIST_QUERY} para realizar a pesquisa.
     * 
     * @param filter Filtros ou Modelo para pesquisa
     * @return List de DTO's recuperados do banco de dados
     * @throws TechnicalException
     */
    @SuppressWarnings("unchecked")
    public List<T> list(Object filter) throws TechnicalException {
    	return (List<T>) this.list(PREFIX_LIST_QUERY, filter);
    }
        

    /**
     * Retorna uma lista de registros com base nos filtros/modelo informado(s), 
     * utilizando o SQL (queryName) para realizar a pesquisa.
     * 
     * @param queryName Nome da query (SQL) mapeada
     * @param filter Filtros ou Modelo para pesquisa
     * @return List de DTO's recuperados do banco de dados
     * @throws TechnicalException
     */
    protected List<?> list(String queryName, Object filter) throws TechnicalException {
        SqlSession session = openSession();
        ArrayList<Object> list = null;
        try {
            long t1 = new Date().getTime();
            String query = buildQueryName(queryName);
            if (filter == null) {
            list = (ArrayList<Object>) session.selectList(query);
            } else {
            list = (ArrayList<Object>) session.selectList(query, filter);
            }
            LOGGER.debug("Query " + query + " executed in " + (new Date().getTime() - t1) + "ms");
        } catch (Exception e) {
            LOGGER.error(e);
            throw new TechnicalException(e);
        } finally {
            closeSession(session);
        }
        return list;
    }

    /**
     * Insere o registro (model) utilizando o SQL {@link GenericDAO#PREFIX_INSERT_QUERY} para inserir o registro
     * 
     * @param model Objeto DTO que será inserido
     * @return
     * @throws TechnicalException
     */
    public T insert(T model) throws BaseException {
    	return this.insert(PREFIX_INSERT_QUERY, model);
    }

    /**
     * Insere o registro (model) utilizando o SQL (queryName) para inserir o registro
     * @param queryName Nome do SQL que será utilizado para inserir o registro.
     * @param model Objeto DTO que será inserido
     * @return
     * @throws TechnicalException
     */
    protected T insert(String queryName, T model) throws BaseException {
        SqlSession session = openSession();
        try {
            String query = buildQueryName(queryName);
            session.insert(query, model);
        } catch (Exception e) {
            // Ocorreu um erro durante a inserção do registro,
            // então o id do model deve ser zerado (null)
            model.setId(null);
            throw checkExceptionType(e);
        } finally {
            closeSession(session);
        }
        return model;
    }

    /**
     * Atualiza o registro (model) utilizando o SQL {@link GenericDAO#PREFIX_UPDATE_QUERY} para atualizar o registro.
     * 
     * @param model Objeto DTO que será atualizado
     * @return Objeto atualizado
     * @throws TechnicalException
     */
    public T update(T model) throws BaseException {
	 // Para atualização, deve se verificar se o registro existe e se esta sendo modificado
        T oldModel = find(model);
        
        // O registro existe ?
        if(oldModel != null) {
            // Se os registros forem diferentes, atualiza, caso contrário não altera o registro
            if(DTOComparator.hasDifferences(model, oldModel)) {
        		return this.update(PREFIX_UPDATE_QUERY, model);
            } 
        } else {
            // O registro não existe, lanca excecao de registro nao encontrado
            throw new BusinessException("REGISTRO NAO ENCONTRADO");
        }
        
    	return model;
    }
    
    /**
     * Tenta atualiza o registro (model) utilizando o SQL {@link GenericDAO#PREFIX_UPDATE_QUERY} para atualizar o registro sem
     * antes efetuar uma pesquisa para ver se o mesmo existe
     * 
     * @param model Objeto DTO que ser� atualizado
     * @return Objeto atualizado
     * @throws TechnicalException
     */
	public T forceUpdate(T model) throws BaseException {
		return this.update(PREFIX_UPDATE_QUERY, model);
	}

    /**
     * Atualiza o registro (model) utilizando o SQL (queryName) para atualizar o registro.
     * 
     * @param queryName Nome da query mapeada
     * @param model Objeto DTO que será atualizado
     * @return Objeto atualizado
     * @throws TechnicalException
     */
    protected T update(String queryName, T model) throws BaseException {
        SqlSession session = openSession();
        try {
            String query = buildQueryName(queryName);
            session.update(query, model);
        } catch (Exception e) {
            throw checkExceptionType(e);
        } finally {
            closeSession(session);
        }
        return model;
    }
    
    /**
     * Utilizando o SQL (queryName), atualiza um ou mais registros de acordo com os parâmetros informados (parameter).
     * 
     * @param queryName Nome da query mapeada
     * @param parameter Objeto contendo o(s) parametro(s) utilizado(s) na query
     * @throws BaseException
     */
    protected void update(String queryName, Object parameter) throws BaseException {
		SqlSession session = openSession();
		try {
		    String query = buildQueryName(queryName);
		    session.update(query, parameter);
		} catch (Exception e) {
		    throw checkExceptionType(e);
		} finally {
		    closeSession(session);
		}
    }

    /**
     * Exclui registro com base no id informado, utilizando o SQL {@link GenericDAO#PREFIX_DELETE_QUERY}
     * 
     * @param id PK do registro
     * @return O numero de linhas afetadas
     * @throws TechnicalException
     */
	public int delete(Object id) throws TechnicalException {
		return this.delete(PREFIX_DELETE_QUERY, id);
	}

    /**
     * Exclui registro com base no id, utilizando o SQL (queryName)
     * 
     * @param queryName Nome da query mapeada
     * @param params Parametros para o delete
     * @return O numero de linhas afetadas
     * @throws TechnicalException
     */
    protected int delete(String queryName, Object params) throws TechnicalException {
        SqlSession session = openSession();
        Integer status = null;
        try {
            String query = buildQueryName(queryName);
            status = session.delete(query, params);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new TechnicalException(e);
        } finally {
            closeSession(session);
        }
        return status;
    }
    

    /**
     * Insere ou atualiza o registro (model);
     * <p>
     * Insere utilizando o SQL {@link GenericDAO#PREFIX_INSERT_QUERY}<br/>
     * Quando o registro possui ID, atualiza utilizando o SQL {@link GenericDAO#PREFIX_UPDATE_QUERY}<br/>
     * </p>
     * @param model Objeto DTO que será inserido/atualizado
     * @return Objeto atualizado
     * @throws BaseException
     */
	public T save(T model) throws BaseException {

		try {
			// Se o id nao estiver sido informado ou for 0, esta sendo criado um
			// novo registro
			if (model.getId() == null || model.getId() == 0) {
				return this.insert(model);
			}
			// Caso contrario esta sendo atualizado.
			else {
				return this.update(model);
			}

		} catch (Exception e) {
			if (e instanceof BaseException) {
				throw (BaseException) e;
			}

			throw new TechnicalException(e);
		}

	}
    
    
	/** Retorna a Session Factory deste DAO */
	protected SqlSessionFactory getSessionFactory() {
		return sf;
	}

    /**
     * Abre sessão do mybatis
     * @return
     */
    protected SqlSession openSession() throws TechnicalException {
        try {
            return sf.openSession();
        } catch (Exception e) {
            LOGGER.error(e);
            throw new TechnicalException(e);
        }
    }
    
    /**
     * Fecha a sessão do mybatis
     * @param session
     * @throws TechnicalException
     */
    protected void closeSession(SqlSession session) throws TechnicalException {
        if(session != null) {
            try {
            session.close();
            } catch (Exception e) {
                LOGGER.error(e);
                throw new TechnicalException(e);
            }
        }
    }
    
	/**
	 * Retorna o nome da query mybatis com base na queryName informada
	 * 
	 * @param queryName
	 * @return
	 */
	protected String buildQueryName(String queryName) {
		return this.daoType.getSimpleName().concat(".").concat(queryName);
	}
    
	/**
	 * Verifica se o erro é um erro de negocio e retorna o a Exception correta
	 * 
	 * @param t
	 * @return TechnicalException quando for um erro sistemico<br>
	 *         BusinessException quando for um erro de negocio
	 */
	protected BaseException checkExceptionType(Exception e) {
		// Verifica se o registro está duplicado
		if (e.getMessage().toUpperCase().contains(ORA_DUPLICATED_RECORD.toUpperCase())) {
			return new DuplicatedKeyException(e);
		} else {
			LOGGER.error(e);
			return new TechnicalException(e);
		}
	}
}