package org.asb.service;

import javax.ejb.Local;

import org.asb.model.User;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface UserService extends GenericService<User, Integer> {

}
