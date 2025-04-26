package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.DenounceDao;
import org.asb.dao.impl.DenounceDaoImpl;
import org.asb.model.Denounce;
import org.asb.service.DenounceService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(DenounceService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DenounceServiceImpl extends GenericServiceImpl<Denounce, Integer, DenounceDao> implements DenounceService {

	@Override
	protected DenounceDao getDao() {
		return new DenounceDaoImpl(em, se);
	}

}
