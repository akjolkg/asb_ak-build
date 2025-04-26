package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.AppartmentDao;
import org.asb.dao.impl.AppartmentDaoImpl;
import org.asb.model.Appartment;
import org.asb.service.AppartmentService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(AppartmentService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AppartmentServiceImpl extends GenericServiceImpl<Appartment, Integer, AppartmentDao> implements AppartmentService {

	@Override
	protected AppartmentDao getDao() { 
		return new AppartmentDaoImpl(em, se);
	}

}
