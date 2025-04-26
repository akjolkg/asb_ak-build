package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.AppartmentDao;
import org.asb.model.Appartment;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class AppartmentDaoImpl extends GenericDaoImpl<Appartment, Integer> implements AppartmentDao {

	public AppartmentDaoImpl(EntityManager entityManager, Event<Appartment> eventSource) {
		super(entityManager,eventSource);
	}

}
