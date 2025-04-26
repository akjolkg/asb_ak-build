package org.asb.service;

import javax.ejb.Local;

import org.asb.model.Payment;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface PaymentService extends GenericService<Payment, Integer> {

}
