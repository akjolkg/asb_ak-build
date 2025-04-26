package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.ScheduleDao;
import org.asb.model.Attachment;
import org.asb.model.Schedule;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class ScheduleDaoImpl extends GenericDaoImpl<Schedule, Integer> implements ScheduleDao {

	public ScheduleDaoImpl(EntityManager entityManager,Event<Schedule> eventSource) {
		super(entityManager,eventSource);
	}
}
