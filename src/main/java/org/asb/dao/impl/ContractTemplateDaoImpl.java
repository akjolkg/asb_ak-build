package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.ContractTemplateDao;
import org.asb.model.ContractTemplate;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class ContractTemplateDaoImpl extends GenericDaoImpl<ContractTemplate, Integer> implements ContractTemplateDao {

	public ContractTemplateDaoImpl(EntityManager entityManager,Event<ContractTemplate> eventSource) {
		super(entityManager,eventSource);
	}
}
