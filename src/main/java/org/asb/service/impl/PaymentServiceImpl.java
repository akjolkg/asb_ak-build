package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.PaymentDao;
import org.asb.dao.impl.PaymentDaoImpl;
import org.asb.model.Payment;
import org.asb.service.PaymentService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(PaymentService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PaymentServiceImpl extends GenericServiceImpl<Payment, Integer, PaymentDao> implements PaymentService {

	@Override
	protected PaymentDao getDao() {
		return new PaymentDaoImpl(em, se);
	}

}
