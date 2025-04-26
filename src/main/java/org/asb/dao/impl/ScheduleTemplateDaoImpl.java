package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.ScheduleTemplateDao;
import org.asb.model.ScheduleTemplate;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class ScheduleTemplateDaoImpl extends GenericDaoImpl<ScheduleTemplate, Integer> implements ScheduleTemplateDao {

	public ScheduleTemplateDaoImpl(EntityManager entityManager,Event<ScheduleTemplate> eventSource) {
		super(entityManager,eventSource);
	}

}
