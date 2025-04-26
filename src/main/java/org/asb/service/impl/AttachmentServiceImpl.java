package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.AttachmentDao;
import org.asb.dao.impl.AttachmentDaoImpl;
import org.asb.model.Attachment;
import org.asb.service.AttachmentService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(AttachmentService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AttachmentServiceImpl extends GenericServiceImpl<Attachment, Integer, AttachmentDao> implements AttachmentService {

	@Override
	protected AttachmentDao getDao() {
		return new AttachmentDaoImpl(em, se);
	}

}
