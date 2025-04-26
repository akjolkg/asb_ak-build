package org.asb.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.asb.dao.CommentLogDao;
import org.asb.dao.impl.CommentLogDaoImpl;
import org.asb.model.CommentLog;
import org.asb.service.CommentLogService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(CommentLogService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CommentLogServiceImpl extends GenericServiceImpl<CommentLog, Integer, CommentLogDao> implements CommentLogService {

	@Override
	protected CommentLogDao getDao() {
		return new CommentLogDaoImpl(em, se);
	}

}
