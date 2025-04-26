package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.DeletedReserveDao;
import org.asb.dao.impl.DeletedReserveDaoImpl;
import org.asb.model.DeletedReserve;
import org.asb.service.DeletedReserveService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(DeletedReserveService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DeletedReserveServiceImpl extends GenericServiceImpl<DeletedReserve, Integer, DeletedReserveDao> implements DeletedReserveService {

	@Override
	protected DeletedReserveDao getDao() {
		return new DeletedReserveDaoImpl(em, se);
	}

}
