package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.ScheduleTemplateDao;
import org.asb.dao.impl.ScheduleTemplateDaoImpl;
import org.asb.model.ScheduleTemplate;
import org.asb.service.ScheduleTemplateService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(ScheduleTemplateService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ScheduleTemplateServiceImpl extends GenericServiceImpl<ScheduleTemplate, Integer, ScheduleTemplateDao> implements ScheduleTemplateService {

	@Override
	protected ScheduleTemplateDao getDao() {
		return new ScheduleTemplateDaoImpl(em, se);
	}

}
