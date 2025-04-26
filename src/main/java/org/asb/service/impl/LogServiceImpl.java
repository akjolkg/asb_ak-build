package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.LogDao;
import org.asb.dao.impl.LogDaoImpl;
import org.asb.model.Log;
import org.asb.service.LogService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(LogService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class LogServiceImpl extends GenericServiceImpl<Log, Integer, LogDao> implements LogService {

	@Override
	protected LogDao getDao() {
		return new LogDaoImpl(em, se);
	}

}
