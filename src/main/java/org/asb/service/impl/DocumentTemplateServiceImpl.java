package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.DocumentTemplateDao;
import org.asb.dao.impl.DocumentTemplateDaoImpl;
import org.asb.model.DocumentTemplate;
import org.asb.service.DocumentTemplateService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(DocumentTemplateService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DocumentTemplateServiceImpl extends GenericServiceImpl<DocumentTemplate, Integer, DocumentTemplateDao> implements DocumentTemplateService {

	@Override
	protected DocumentTemplateDao getDao() {
		return new DocumentTemplateDaoImpl(em, se);
	}

}
