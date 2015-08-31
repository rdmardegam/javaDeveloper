package br.com.javadeveloper.dto;

import java.io.ObjectStreamClass;
import java.io.Serializable;

/**
 * Classe abstrata que deve ser utilizada como base para os <b>DTO's</b> dos
 * servicos de integracao
 */
public abstract class BaseDTO implements Serializable, Cloneable {

    /** serialVersionUID */
    private static final long serialVersionUID = 527261129726570813L;
    
    /** Identificador unico do DTO */
    private Long id;
   
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Compara se o objeto comparado tem o mesmo ID
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BaseDTO)) {
			// Não é um BaseDTO entao retorna false;
			return false;
		}
		if (obj == this) {
			// Trata-se do mesmo objeto
			return true;
		}
		// Compara o id
		Long objId = ((BaseDTO) obj).getId();
		return (id != null && objId != null && objId.equals(this.id));
	}
    
    /** 
     * Retorna o hashCode do objeto, com base na serialVersionUID e id do DTO
     */
    @Override
    public int hashCode() {
		ObjectStreamClass osc = ObjectStreamClass.lookup(this.getClass());
		try {
		    return (int)(osc.getSerialVersionUID()/12) + ((int)(this.id != null ? this.id : 0));
		} catch (NullPointerException ex) {
		    return (int)(serialVersionUID/12) + ((int)(this.id != null ? this.id : 0));
		}
    }
    
    /**
     * Atribui ao BaseDTO recebido os valores dos atributos deste BaseDTO
     *   
     * @param dto Um objeto filho de BaseDTO
     */
    protected void cloneAttributes(BaseDTO dto) {
       dto.setId(id);
    }

	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}


}
