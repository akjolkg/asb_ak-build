package org.asb.conversion;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.asb.dao.GenericDao;
import org.asb.model.PersistentEntity;
import org.asb.service.GenericService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public abstract class EntityConvertor<T extends PersistentEntity<ID>, ID extends Serializable, D extends GenericDao<T, ID>, 
			S extends GenericService<T, ID>> implements Converter {
	
	public EntityConvertor() {}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String key) {
		ID id = getID(key);
		if(id != null){
			T entity = getService().findById(id, false);
			return entity;
		}
		return null;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value instanceof PersistentEntity){
			@SuppressWarnings("unchecked")
			PersistentEntity<ID> entity = (PersistentEntity<ID>)value;
			return entity.getId().toString();
		}
		return null;
	}
	
	protected abstract S getService();
	protected abstract ID getID(String key);

}
