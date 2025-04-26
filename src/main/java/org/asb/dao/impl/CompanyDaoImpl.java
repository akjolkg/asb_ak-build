package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.CompanyDao;
import org.asb.model.Attachment;
import org.asb.model.Company;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class CompanyDaoImpl extends GenericDaoImpl<Company, Integer> implements CompanyDao {

	public CompanyDaoImpl(EntityManager entityManager,Event<Company> eventSource) {
		super(entityManager,eventSource);
	}

}
