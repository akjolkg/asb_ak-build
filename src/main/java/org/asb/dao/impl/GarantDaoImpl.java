package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.GarantDao;
import org.asb.model.Garant;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class GarantDaoImpl extends GenericDaoImpl<Garant, Integer> implements GarantDao {

	public GarantDaoImpl(EntityManager entityManager,Event<Garant> eventSource) {
		super(entityManager,eventSource);
	}
}
