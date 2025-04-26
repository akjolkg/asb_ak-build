package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.ClientDao;
import org.asb.model.Attachment;
import org.asb.model.Client;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class ClientDaoImpl extends GenericDaoImpl<Client, Integer> implements ClientDao {

	public ClientDaoImpl(EntityManager entityManager,Event<Client> eventSource) {
		super(entityManager,eventSource);
	}

}
