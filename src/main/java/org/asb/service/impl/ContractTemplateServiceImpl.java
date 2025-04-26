package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.ContractTemplateDao;
import org.asb.dao.impl.ContractTemplateDaoImpl;
import org.asb.model.ContractTemplate;
import org.asb.service.ContractTemplateService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(ContractTemplateService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ContractTemplateServiceImpl extends GenericServiceImpl<ContractTemplate, Integer, ContractTemplateDao> implements ContractTemplateService {

	@Override
	protected ContractTemplateDao getDao() {
		return new ContractTemplateDaoImpl(em, se);
	}

}
