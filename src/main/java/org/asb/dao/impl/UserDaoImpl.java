package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.UserDao;
import org.asb.model.Attachment;
import org.asb.model.User;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class UserDaoImpl extends GenericDaoImpl<User, Integer> implements UserDao {

	public UserDaoImpl(EntityManager entityManager,Event<User> eventSource) {
		super(entityManager,eventSource);
	}
}
