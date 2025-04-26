package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.CommentLogDao;
import org.asb.model.CommentLog;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class CommentLogDaoImpl extends GenericDaoImpl<CommentLog, Integer> implements CommentLogDao {

	public CommentLogDaoImpl(EntityManager entityManager,Event<CommentLog> eventSource) {
		super(entityManager,eventSource);
	}

}
