package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.DocumentTemplateDao;
import org.asb.model.DocumentTemplate;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class DocumentTemplateDaoImpl extends GenericDaoImpl<DocumentTemplate, Integer> implements DocumentTemplateDao {

	public DocumentTemplateDaoImpl(EntityManager entityManager,Event<DocumentTemplate> eventSource) {
		super(entityManager,eventSource);
	}
}
