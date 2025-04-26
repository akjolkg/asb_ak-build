package org.asb.service;

import javax.ejb.Local;

import org.asb.model.Client;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface ClientService extends GenericService<Client, Integer> {

}
