package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.ConstructionDao;
import org.asb.model.Attachment;
import org.asb.model.Construction;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class ConstructionDaoImpl extends GenericDaoImpl<Construction, Integer> implements ConstructionDao {

	public ConstructionDaoImpl(EntityManager entityManager,Event<Construction> eventSource) {
		super(entityManager,eventSource);
	}

}
