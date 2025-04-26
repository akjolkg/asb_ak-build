package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.DenounceDao;
import org.asb.model.Denounce;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class DenounceDaoImpl extends GenericDaoImpl<Denounce, Integer> implements DenounceDao {

	public DenounceDaoImpl(EntityManager entityManager,Event<Denounce> eventSource) {
		super(entityManager,eventSource);
	}

}
