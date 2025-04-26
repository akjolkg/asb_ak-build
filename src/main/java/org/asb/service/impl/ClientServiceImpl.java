package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.ClientDao;
import org.asb.dao.impl.ClientDaoImpl;
import org.asb.model.Client;
import org.asb.service.ClientService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(ClientService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ClientServiceImpl extends GenericServiceImpl<Client, Integer, ClientDao> implements ClientService {

	@Override
	protected ClientDao getDao() {
		return new ClientDaoImpl(em, se);
	}

}
