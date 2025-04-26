package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.DeletedReserveDao;
import org.asb.model.DeletedReserve;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class DeletedReserveDaoImpl extends GenericDaoImpl<DeletedReserve, Integer> implements DeletedReserveDao {

	public DeletedReserveDaoImpl(EntityManager entityManager,Event<DeletedReserve> eventSource) {
		super(entityManager,eventSource);
	}

}
