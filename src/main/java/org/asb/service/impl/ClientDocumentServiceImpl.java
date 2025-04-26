package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.ClientDocumentDao;
import org.asb.dao.impl.ClientDocumentDaoImpl;
import org.asb.model.ClientDocument;
import org.asb.service.ClientDocumentService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(ClientDocumentService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ClientDocumentServiceImpl extends GenericServiceImpl<ClientDocument, Integer, ClientDocumentDao> implements ClientDocumentService {

	@Override
	protected ClientDocumentDao getDao() {
		return new ClientDocumentDaoImpl(em, se);
	}

}
