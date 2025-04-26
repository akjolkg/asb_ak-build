package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.SubScheduleDao;
import org.asb.model.SubSchedule;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class SubScheduleDaoImpl extends GenericDaoImpl<SubSchedule, Integer> implements SubScheduleDao {

	public SubScheduleDaoImpl(EntityManager entityManager,Event<SubSchedule> eventSource) {
		super(entityManager,eventSource);
	}

}
