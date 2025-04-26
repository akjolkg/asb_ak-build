package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.ResponsiblePersonDao;
import org.asb.model.ResponsiblePerson;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class ResponsiblePersonDaoImpl extends GenericDaoImpl<ResponsiblePerson, Integer> implements ResponsiblePersonDao {

	public ResponsiblePersonDaoImpl(EntityManager entityManager,Event<ResponsiblePerson> eventSource) {
		super(entityManager,eventSource);
	}
}
