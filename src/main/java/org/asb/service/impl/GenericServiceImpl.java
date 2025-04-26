package org.asb.service.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnitUtil;

import org.asb.beans.FilterExample;
import org.asb.beans.SortEnum;
import org.asb.dao.GenericDao;
import org.asb.interceptor.MethodLoggerInterceptor;
import org.asb.model.PersistentEntity;
import org.asb.service.GenericService;

/****
 * 
 * @author Akzholbek Omorov
 *
 * @param <T>
 * @param <ID>
 * @param <D>
 */

@Interceptors({MethodLoggerInterceptor.class})
public abstract class GenericServiceImpl<T extends PersistentEntity<ID>, ID extends Serializable, D extends GenericDao<T, ID>> implements GenericService<T, ID> {
	
	@Inject protected EntityManager em;
	@Inject protected Event<T> se;
	
	protected abstract D getDao();
	
	private static Class<?>[] standards = {Byte.class, Character.class, Short.class, Integer.class, Long.class, 
		Float.class, Double.class, Boolean.class, String.class, Date.class};
	
	@Override
	public T persist(T entity) throws PersistenceException {
		return getDao().persist(entity);
	}

	@Override
	public T merge(T entity) throws PersistenceException {
		return getDao().merge(entity);
	}

	@Override
	public void remove(T entity) {
		getDao().remove(getDao().findById(entity.getId(), false));
	}
	
	@Override
	public void refresh(T entity) {
		getDao().refresh(entity);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public long countAll() {
		return getDao().countAll();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public T findById(ID id, boolean lock) {
		try {
			return getDao().findById(id, lock);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public T getByIdWithFields(ID id, String[] fields){
		try {
			return getDao().getByIdWithFields(id, fields) ;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public T getByIdWithGraphs(ID id, String[] graphs) {
		try {
			return getDao().getByIdWithGraphs(id, graphs);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Long countByProperty(String property, Object value) {
		try {
			return getDao().countByProperty(property, value);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Long countByExample(List<FilterExample> list){
		return getDao().countByExample(list);
	}
	
	@Override
	public Long countByExample(List<FilterExample> list, String[] fields){
		return getDao().countByExample(list,fields);
	}
	
	
	@Override
	public BigDecimal sumByExample(String property,List<FilterExample> list){
		return getDao().sumByExample(property,list);
	}
	
	@Override
	public BigDecimal sumByExample(String property,List<FilterExample> list, String[] fields){
		return getDao().sumByExample(property,list,fields);
	}
	
	
	@Override
	public Integer findMaxByPropertyWithExamples(String property, List<FilterExample> list){
		return getDao().findMaxByPropertyWithExamples(property,list);
	}
	
	@Override
	public Integer maxPosition(){
		return getDao().maxPosition();
	}
	

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<T> findByProperty(String property, Object value) {
		try {
			return getDao().findByProperty(property, value);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public List<T> findByPropertyWithFields(String property, Object value, String[] fields){
		try {
			return getDao().fingByProperty(property, value, fields);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public List<T> fingByPropertyWithGraphs(String property, Object value, String[] graphs) {
		try {
			return getDao().fingByPropertyWithGraphs(property, value, graphs);
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<T> findAll() {
		return getDao().findAll();
	}
	

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<T> findEntries(int firstResult, int maxResults) {
		return getDao().findEntries(firstResult, maxResults);
	}
	
	@Override
	public List<T> findEntries(int firstResult, int maxResults, String sortProperty, SortEnum sort) {
		return getDao().findEntries(firstResult, maxResults, sortProperty, sort);
	}
	
	@Override
	public List<T> findByExample(int from, int to, SortEnum sortEnum,
			List<FilterExample> list, String sortProperty) {
		return getDao().findByExample(from, to, sortEnum, list, sortProperty);
	}
	
	@Override
	public List<T> findByExample(int from, int to, SortEnum sortEnum,
			List<FilterExample> list, String sortProperty, String[] fields) {
		return getDao().findByExample(from, to, sortEnum, list, sortProperty, fields);
	}
	
	@Override
	public List<T> findByExample(int from, int to,List<FilterExample> list) {
		return getDao().findByExample(from, to, list);
	}
	
	@Override
	public List<T> findByExampleWithGraphs(int from, int to, SortEnum sortEnum,
			List<FilterExample> list, String sortProperty, String[] graphs) {
		return getDao().findByExampleWithGraphs(from, to, sortEnum, list, sortProperty, graphs);
	}
	
	@Override
	public List<String> getModifiedFields(T entity, T modifiedEntity, List<String> notChekck) {
		
		Field[] fields = modifiedEntity.getClass().getDeclaredFields();
		List<String> list = new ArrayList<String>();
		
		for (Field field : fields) {
			if(notChekck.contains(field.getName())) continue;
			
			if(Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) continue;
			
			try {
				if(field.getType().isPrimitive() || isStandard(field.getType()) || field.getType().isEnum()
						|| PersistentEntity.class.isAssignableFrom(field.getType())){
					String methodName = "get" + field.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + field.getName().substring(1);
					Method method = modifiedEntity.getClass().getMethod(methodName);
					Object value = method.invoke(entity, new Object[]{});
					Object otherValue = method.invoke(modifiedEntity, new Object[]{});
					if(value == null && otherValue != null) list.add(field.getName());
					else if(value != null && !value.equals(otherValue)) list.add(field.getName());
				}
				else if(Collection.class.isAssignableFrom(field.getType())){
				
					PersistenceUnitUtil unitUtil = getDao().getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
					if(!unitUtil.isLoaded(modifiedEntity, field.getName())) continue;
					if(!unitUtil.isLoaded(entity, field.getName())) entity = getDao().getByIdWithFields(entity.getId(), new String[]{field.getName()});
					String methodName = "get" + field.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + field.getName().substring(1);
				
					Method method = modifiedEntity.getClass().getMethod(methodName);
					Collection<?> value = (Collection<?>)method.invoke(entity, new Object[]{});
					Collection<?> otherValue = (Collection<?>)method.invoke(modifiedEntity, new Object[]{});
					if(value != null && value.size() != otherValue.size()) list.add(field.getName());
					
				}
				else {
					System.out.println("unknow field!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! = " + field.getName());
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return list;
		
	}
	
	private boolean isStandard(Class<?> type) {
		return Arrays.asList(standards).contains(type);
	}

}