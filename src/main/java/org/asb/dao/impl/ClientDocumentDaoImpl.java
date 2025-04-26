package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.ClientDocumentDao;
import org.asb.model.ClientDocument;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class ClientDocumentDaoImpl extends GenericDaoImpl<ClientDocument, Integer> implements ClientDocumentDao {

	public ClientDocumentDaoImpl(EntityManager entityManager,Event<ClientDocument> eventSource) {
		super(entityManager,eventSource);
	}
}
