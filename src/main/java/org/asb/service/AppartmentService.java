package org.asb.service;

import javax.ejb.Local;

import org.asb.model.Appartment;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface AppartmentService extends GenericService<Appartment, Integer> {

}
