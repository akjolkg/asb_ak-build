package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.GeneralInfoDao;
import org.asb.dao.impl.GeneralInfoDaoImpl;
import org.asb.model.GeneralInfo;
import org.asb.service.GeneralInfoService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(GeneralInfoService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class GeneralInfoServiceImpl extends GenericServiceImpl<GeneralInfo, Integer, GeneralInfoDao> implements GeneralInfoService {

	@Override
	protected GeneralInfoDao getDao() {
		return new GeneralInfoDaoImpl(em, se);
	}

}
