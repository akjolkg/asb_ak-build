package org.asb.service;

import javax.ejb.Local;

import org.asb.model.Company;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface CompanyService extends GenericService<Company, Integer> {

}
