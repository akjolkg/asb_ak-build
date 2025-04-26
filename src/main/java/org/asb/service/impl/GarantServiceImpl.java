package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.GarantDao;
import org.asb.dao.impl.GarantDaoImpl;
import org.asb.model.Garant;
import org.asb.service.GarantService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(GarantService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class GarantServiceImpl extends GenericServiceImpl<Garant, Integer, GarantDao> implements GarantService {

	@Override
	protected GarantDao getDao() {
		return new GarantDaoImpl(em, se);
	}

}
