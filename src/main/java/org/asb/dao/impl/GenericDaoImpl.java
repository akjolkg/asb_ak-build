package org.asb.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.xml.bind.annotation.XmlTransient;

import org.asb.beans.FilterExample;
import org.asb.beans.SortEnum;
import org.asb.dao.GenericDao;
import org.asb.model.PersistentEntity;

/***
 * 
 * @author Akzholbek Omorov
 *
 * @param <T>
 * @param <ID>
 */
@XmlTransient
public abstract class GenericDaoImpl<T extends PersistentEntity<ID>, ID extends Serializable> implements GenericDao<T, ID> {

	private Class<T> persistentClass;
	private final EntityManager entityManager;
	private final Event<T> eventSource;
	
	private final static String FETCH_GRAPH = "javax.persistence.fetchgraph";
	
	@SuppressWarnings("unchecked")
	public GenericDaoImpl(EntityManager entityManager, Event<T> eventSource) {
		this.persistentClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.entityManager = entityManager;
		this.eventSource = eventSource;
	}
	
	@Override
	public EntityManager getEntityManager()
	{
	    return entityManager;
	}
	
	@Override
	public Event<T> getEventSource() {
		return eventSource;
	}
	
	/**
	 * 
	*/
	public void flush() {
	  getEntityManager().flush();
	}
	 
	  /**
	  * 
	  */
	   public void clear() {
	       getEntityManager().clear();
	  }
	  
	  /**
       * @return
       */
      protected Class<T> getPersistentClass() {
    	  return persistentClass;
      }

	
	@Override
	public T persist(T entity) throws PersistenceException {
		getEntityManager().persist(entity);
		getEventSource().fire(entity);
		return entity;
	}
	
	@Override
	public T merge(T entity) {
		entity = getEntityManager().merge(entity);
		getEventSource().fire(entity);
		
		return entity;
	};

	@Override
	public void remove(T entity) {
		getEntityManager().remove(entity);
		getEventSource().fire(entity);
	}
	@Override
	public Integer findMaxByPropertyWithExamples(String property,List<FilterExample> list ) {
		StringBuffer buffer =new StringBuffer("SELECT MAX(entity."+property+") FROM " + getPersistentClass().getCanonicalName() + " entity WHERE"); 
		int count = 1;
		boolean search=true;
		for (FilterExample filterExample : list) {
			if(filterExample.isSearch()){
				if(search){
					buffer.append(" (");
					search=false;
				}else buffer.append(" OR");
			}else if(!search){buffer.append(" )");search=true;if (count <= list.size())buffer.append(" AND");}		
			buffer.append(filterExample.query("entity", count++)); 
			if(filterExample.isSearch()){ if(count > list.size()) buffer.append(" )");}
				else
			if(count <= list.size()) buffer.append(" AND");
		}
	
		TypedQuery<Integer> query = getEntityManager().createQuery(buffer.toString(), Integer.class);
		
		count = 1;
		
		for (FilterExample filterExample : list) {
			if(filterExample.getValue()!=null)
			query.setParameter("value" + count++, filterExample.getValue());
			else count++;
		}
		return query.setMaxResults(1).getSingleResult();
	}
	
	@Override
	public void refresh(T entity) {
		getEntityManager().refresh(entity);
	};
	
	@Override
	public long countAll() {
		return getEntityManager().createQuery(new StringBuilder("select count(*) from ")
			.append(getPersistentClass().getName()).toString(), Long.class).getSingleResult();
	}

	@Override
	public T findById(ID id, boolean lock) {
		 T entity;
	        if (lock)
	            entity = (T) getEntityManager().find(getPersistentClass(), id, LockModeType.WRITE);
	        else
	            entity = (T) getEntityManager().find(getPersistentClass(), id);
	 
	        return entity;
	}

	@Override
	public List<T> findAll() {
		return getEntityManager().createQuery(new StringBuilder("select entity from ").append(getPersistentClass().getSimpleName())
				.append(" as entity").toString(), getPersistentClass()).getResultList();

	}

	@Override
	public List<T> findEntries(int firstResult, int maxResults) {
		return findEntries(firstResult, maxResults, "id", SortEnum.ASCENDING);
	}
	
	@Override
	public List<T> findEntries(int firstResult, int maxResults, String sortProperty, SortEnum sort) {
		String order = SortEnum.ASCENDING.equals(sort) ? " ASC" : " DESC";
		return getEntityManager().createQuery(new StringBuilder("select entity from ").append(getPersistentClass().getSimpleName())
		        .append(" as entity ORDER BY entity." + sortProperty + order).toString(), getPersistentClass()).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}
	
	@Override
	public void evict(ID id) {
		getEntityManager().getEntityManagerFactory().getCache().evict(getPersistentClass(), id);
	}

	@Override
	public void evictByEntity() {
		getEntityManager().getEntityManagerFactory().getCache().evict(getPersistentClass());
	}

	@Override
	public void evictAll() {
		getEntityManager().getEntityManagerFactory().getCache().evictAll();
	}
	
	@Override
	public Long countByProperty(String property, Object value) {
		String query = new StringBuilder("select count(*) from ").append(getPersistentClass().getSimpleName())
				.append(" as entity where entity." + property + " = :property").toString();
		return getEntityManager().createQuery(query, Long.class).setParameter("property", value).getSingleResult();
	}
	
	@Override
	public Integer maxPosition() {
		return getEntityManager().createQuery(new StringBuilder("select max(position) from ")
			.append(getPersistentClass().getName()).toString(), Integer.class).getSingleResult();
	}
	
	@Override
	public List<T> findByProperty(String property, Object value) {
		String query = new StringBuilder("select entity from ").append(getPersistentClass().getSimpleName())
				.append(" as entity where entity." + property + " = :property").toString();
		return getEntityManager().createQuery(query, getPersistentClass()).setParameter("property", value).getResultList();
	}
	
	@Override
	public List<T> fingByProperty(String property, Object value, String[] fields) {
		StringBuffer buffer = new StringBuffer("SELECT DISTINCT entity FROM ");
		buffer.append(getPersistentClass().getSimpleName());
		buffer.append(" AS entity");
		for (String string : fields) {
			buffer.append(" LEFT JOIN FETCH entity." + string);
		}
		buffer.append(" where entity." + property + " = :property");
		return getEntityManager().createQuery(buffer.toString(), getPersistentClass()).setParameter("property", value).getResultList();
	}
	
	@Override
	public List<T> fingByPropertyWithGraphs(String property, Object value, String[] graphs) {
		StringBuffer buffer = new StringBuffer("SELECT DISTINCT entity FROM ");
		buffer.append(getPersistentClass().getSimpleName());
		buffer.append(" AS entity");
		buffer.append(" where entity." + property + " = :property");
		
		TypedQuery<T> query = getEntityManager().createQuery(buffer.toString(), getPersistentClass());
		
		for (String graphName : graphs) {
			query.setHint(FETCH_GRAPH, entityManager.getEntityGraph(graphName));
		}
		
		return query.setParameter("property", value).getResultList();
	}

	@Override
	public T getByIdWithFields(ID id, String[] fields) {
		StringBuffer buffer = new StringBuffer("SELECT entity FROM ");
		buffer.append(getPersistentClass().getSimpleName());
		buffer.append(" AS entity");
		for (String string : fields) {
			buffer.append(" LEFT JOIN FETCH entity." + string);
		}
		buffer.append(" where entity.id = :id");
		return getEntityManager().createQuery(buffer.toString(), getPersistentClass()).setParameter("id", id).setMaxResults(1).getSingleResult();
	}
	
	@Override
	public T getByIdWithGraphs(ID id, String[] graphs) {
		StringBuffer buffer = new StringBuffer("SELECT entity FROM ");
		buffer.append(getPersistentClass().getSimpleName());
		buffer.append(" AS entity");
		buffer.append(" where entity.id = :id");
		
		TypedQuery<T> query = getEntityManager().createQuery(buffer.toString(), getPersistentClass());
		
		for (String graphName : graphs) {
			query.setHint(FETCH_GRAPH, entityManager.getEntityGraph(graphName));
		}
		
		return query.setParameter("id", id).setMaxResults(1).getSingleResult();
	}

	@Override
	public Long countByExample(List<FilterExample> list) {
		
		StringBuffer buffer = new StringBuffer("SELECT COUNT(*) FROM " + getPersistentClass().getCanonicalName() + " entity");
		if(!list.isEmpty()) buffer.append(" WHERE");
		
		@SuppressWarnings("unchecked")
		TypedQuery<Long> query = (TypedQuery<Long>) applyFilter(Long.class, list, buffer, "");
		
		return query.setMaxResults(1).getSingleResult();
	}
	
	@Override
	public Long countByExample(List<FilterExample> list, String[] fields) {
		
		StringBuffer buffer = new StringBuffer("SELECT COUNT(*) FROM " + getPersistentClass().getCanonicalName() + " entity");
		if(fields != null){
			for (String string : fields) {
				buffer.append(" LEFT JOIN entity." + string);
			}
		}
		
		if(!list.isEmpty()) buffer.append(" WHERE");
		
		@SuppressWarnings("unchecked")
		TypedQuery<Long> query = (TypedQuery<Long>) applyFilter(Long.class, list, buffer, "");
		
		return query.setMaxResults(1).getSingleResult();
	}
	
	
	@Override
	public BigDecimal sumByExample(String property,List<FilterExample> list, String[] fields) {
		
		StringBuffer buffer = new StringBuffer("SELECT SUM("+property+") FROM " + getPersistentClass().getCanonicalName() + " entity");
		if(fields != null){
			for (String string : fields) {
				buffer.append(" LEFT JOIN entity." + string);
			}
		}
		if(!list.isEmpty()) buffer.append(" WHERE");
		
		@SuppressWarnings("unchecked")
		TypedQuery<BigDecimal> query = (TypedQuery<BigDecimal>) applyFilter(BigDecimal.class, list, buffer, "");
		
		return query.setMaxResults(1).getSingleResult();
	}
	
	
	@Override
	public BigDecimal sumByExample(String property,List<FilterExample> list) {
		
		StringBuffer buffer = new StringBuffer("SELECT SUM("+property+") FROM " + getPersistentClass().getCanonicalName() + " entity");
		if(!list.isEmpty()) buffer.append(" WHERE");
		
		@SuppressWarnings("unchecked")
		TypedQuery<BigDecimal> query = (TypedQuery<BigDecimal>) applyFilter(BigDecimal.class, list, buffer, "");
		
		return query.setMaxResults(1).getSingleResult();
	}
	
	
	
	
	@Override
	public List<T> findByExample(int from, int to, SortEnum sortEnum, List<FilterExample> list, String sortProperty) {

		try {
			StringBuffer buffer = new StringBuffer("SELECT DISTINCT entity FROM " + getPersistentClass().getCanonicalName() + " entity");			
			if(!list.isEmpty()) buffer.append(" WHERE");
			
			return getByExample(from, to, sortEnum, list, sortProperty, buffer, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}
	
	@Override
	public List<T> findByExample(int from, int to,  List<FilterExample> list) {
		StringBuffer buffer = new StringBuffer("SELECT DISTINCT entity FROM " + getPersistentClass().getCanonicalName() + " entity");
		if(!list.isEmpty()) buffer.append(" WHERE");
		@SuppressWarnings("unchecked")
		TypedQuery<T> query = (TypedQuery<T>) applyFilter(getPersistentClass(), list, buffer,"");
		
		
		
		return query.setFirstResult(from).setMaxResults(to).getResultList();
		
	}
	
	@Override
	public List<T> findByExample(int from, int to, SortEnum sortEnum, List<FilterExample> list, String sortProperty, String[] fields) {
		try {
			StringBuffer buffer = new StringBuffer("SELECT DISTINCT entity FROM " + getPersistentClass().getCanonicalName() + " entity");
			
			
			if(fields != null){
				for (String string : fields) {
					buffer.append(" LEFT JOIN FETCH entity." + string);
				}
			}
			if(!list.isEmpty()) buffer.append(" WHERE");
			
			
			return getByExample(from, to, sortEnum, list, sortProperty, buffer, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}
	
	@Override
	public List<T> findByExampleWithGraphs(int from, int to, SortEnum sortEnum, List<FilterExample> list, String sortProperty, String[] graphs) {
		try {
			StringBuffer buffer = new StringBuffer("SELECT DISTINCT entity FROM " + getPersistentClass().getCanonicalName() + " entity");
			
			if(!list.isEmpty()) buffer.append(" WHERE");
			
			
			return getByExample(from, to, sortEnum, list, sortProperty, buffer, graphs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}
	
	private List<T> getByExample(int from, int to, SortEnum sortEnum, List<FilterExample> list, String sortProperty, StringBuffer buffer, String[] graphs) throws Exception {
		String order = SortEnum.ASCENDING.equals(sortEnum) ? "ASC" : "DESC";
		
		
		
		@SuppressWarnings("unchecked")
		TypedQuery<T> query = (TypedQuery<T>) applyFilter(getPersistentClass(), list, buffer, " ORDER BY entity." + sortProperty + " " + order);
		
		if(graphs != null){
			for (String graphName : graphs) {
				query.setHint(FETCH_GRAPH, entityManager.getEntityGraph(graphName));
			}
		}
		
		return query.setFirstResult(from).setMaxResults(to).getResultList();
	}
	
	protected TypedQuery<?> applyFilter(Class<?> type, List<FilterExample> list, StringBuffer buffer, String orderBy) {
		int count = 1;
		boolean search=true;
		for (FilterExample filterExample : list) {
			if(filterExample.isSearch()){
				if(search){
					buffer.append(" (");
					search=false;
				}else buffer.append(" OR");
			}else if(!search){buffer.append(" )");search=true;if (count <= list.size())buffer.append(" AND");}				
			buffer.append(filterExample.query("entity", count++));
			if(filterExample.isSearch()){ if(count > list.size()) buffer.append(" )");}
			else
			if(count <= list.size()) buffer.append(" AND");
			
		}
	
		buffer.append(orderBy);
		System.out.println("query="+buffer.toString());
		TypedQuery<?> query = getEntityManager().createQuery(buffer.toString(), type);
		count = 1;
	
		for (FilterExample filterExample : list) {
			if(filterExample.getValue()!=null){
				System.out.println("value"+(count)+"==="+filterExample.getValue());
				query.setParameter("value" + count++, filterExample.getValue());
				
			}
			else count++;
			
		}
		
		return query;
	}
	
}