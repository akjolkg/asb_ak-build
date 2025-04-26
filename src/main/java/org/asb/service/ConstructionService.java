package org.asb.service;

import javax.ejb.Local;

import org.asb.model.Construction;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface ConstructionService extends GenericService<Construction, Integer> {

}
