package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.LogDao;
import org.asb.model.Attachment;
import org.asb.model.Log;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class LogDaoImpl extends GenericDaoImpl<Log, Integer> implements LogDao {

	public LogDaoImpl(EntityManager entityManager,Event<Log> eventSource) {
		super(entityManager,eventSource);
	}
}
