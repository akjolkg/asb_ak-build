package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.SubScheduleDao;
import org.asb.dao.impl.SubScheduleDaoImpl;
import org.asb.model.SubSchedule;
import org.asb.service.SubScheduleService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(SubScheduleService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SubScheduleServiceImpl extends GenericServiceImpl<SubSchedule, Integer, SubScheduleDao> implements SubScheduleService {

	@Override
	protected SubScheduleDao getDao() {
		return new SubScheduleDaoImpl(em, se);
	}

}
