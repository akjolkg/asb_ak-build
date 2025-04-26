package org.asb.service;

import javax.ejb.Local;

import org.asb.model.Log;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface LogService extends GenericService<Log, Integer> {

}
