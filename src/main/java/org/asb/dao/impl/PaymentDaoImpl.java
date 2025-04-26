package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.PaymentDao;
import org.asb.model.Attachment;
import org.asb.model.Payment;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class PaymentDaoImpl extends GenericDaoImpl<Payment, Integer> implements PaymentDao {

	public PaymentDaoImpl(EntityManager entityManager,Event<Payment> eventSource) {
		super(entityManager,eventSource);
	}
}
