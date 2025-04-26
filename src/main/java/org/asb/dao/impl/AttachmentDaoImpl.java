package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.AttachmentDao;
import org.asb.model.Appartment;
import org.asb.model.Attachment;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class AttachmentDaoImpl extends GenericDaoImpl<Attachment, Integer> implements AttachmentDao {

	public AttachmentDaoImpl(EntityManager entityManager,Event<Attachment> eventSource) {
		super(entityManager,eventSource);
	}
}
