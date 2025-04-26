package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.ResponsiblePersonDao;
import org.asb.dao.impl.ResponsiblePersonDaoImpl;
import org.asb.model.ResponsiblePerson;
import org.asb.service.ResponsiblePersonService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(ResponsiblePersonService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ResponsibleServiceImpl extends GenericServiceImpl<ResponsiblePerson, Integer, ResponsiblePersonDao> implements ResponsiblePersonService {

	@Override
	protected ResponsiblePersonDao getDao() {
		return new ResponsiblePersonDaoImpl(em, se);
	}

}
