package org.asb.dao.impl;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;

import org.asb.dao.GeneralInfoDao;
import org.asb.model.Attachment;
import org.asb.model.GeneralInfo;

/****
 * 
 * @author Akzholbek Omorov
 *
 */
public class GeneralInfoDaoImpl extends GenericDaoImpl<GeneralInfo, Integer> implements GeneralInfoDao {

	public GeneralInfoDaoImpl(EntityManager entityManager,Event<GeneralInfo> eventSource) {
		super(entityManager,eventSource);
	}
}
