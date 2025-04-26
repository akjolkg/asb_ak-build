package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.CompanyDao;
import org.asb.dao.impl.CompanyDaoImpl;
import org.asb.model.Company;
import org.asb.service.CompanyService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(CompanyService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CompanyServiceImpl extends GenericServiceImpl<Company, Integer, CompanyDao> implements CompanyService {

	@Override
	protected CompanyDao getDao() {
		return new CompanyDaoImpl(em, se);
	}

}
