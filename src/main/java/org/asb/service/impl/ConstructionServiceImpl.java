package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.ConstructionDao;
import org.asb.dao.impl.ConstructionDaoImpl;
import org.asb.model.Construction;
import org.asb.service.ConstructionService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(ConstructionService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ConstructionServiceImpl extends GenericServiceImpl<Construction, Integer, ConstructionDao> implements ConstructionService {

	@Override
	protected ConstructionDao getDao() {
		return new ConstructionDaoImpl(em, se);
	}

}
