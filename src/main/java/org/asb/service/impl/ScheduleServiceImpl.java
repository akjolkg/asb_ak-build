package org.asb.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;

import org.asb.dao.ScheduleDao;
import org.asb.dao.impl.ScheduleDaoImpl;
import org.asb.data.DebtorsDTO;
import org.asb.model.Schedule;
import org.asb.service.ScheduleService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Stateless
@Local(ScheduleService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ScheduleServiceImpl extends GenericServiceImpl<Schedule, Integer, ScheduleDao> implements ScheduleService {

	@Override
	protected ScheduleDao getDao() {
		return new ScheduleDaoImpl(em, se);
	}

		

	
	@Override
	public List<DebtorsDTO> getModel(Integer limit, Integer offset, List<String> expressionsList, Map<Integer, Object> parameters, String extraOrder) {
			String queryText = getQueryTextBlock("mainHeader",null);
			for (String expression : expressionsList) {
				if(expression.contains("DESC")){			
				}else
				queryText += " AND "+expression;
				
			}
			queryText = queryText.concat(getQueryTextBlock("mainFooter",extraOrder));
			
			Query query = em.createNativeQuery(queryText, "DebtorsDTO");
			
			
			for (Map.Entry<Integer, Object> entry : parameters.entrySet()) {				
				query.setParameter(entry.getKey(), entry.getValue());
			}
			query.setParameter(1000, limit);
			query.setParameter(1001, offset);
			
			
			@SuppressWarnings("unchecked")
			List<DebtorsDTO> results = (List<DebtorsDTO>) query.getResultList();
			return results;
	}

	@Override
	public Map<String, Object> getModelTotal(List<String> expressionsList, Map<Integer, Object> parameters) {
		String queryText = getQueryTextBlock("countHeader",null);		
		
		for (String expression : expressionsList) {
			if(expression.contains("DESC")){			
			}else
				queryText += " AND "+expression;
		}		
		queryText = queryText.concat(getQueryTextBlock("countFooter",null));
		
		Query query = em.createNativeQuery(queryText);
		for (Map.Entry<Integer, Object> entry : parameters.entrySet()) {
			
			query.setParameter(entry.getKey(), entry.getValue());
		}
		query.setMaxResults(1);		
		
		
		
		Object[] results = (Object[]) query.getSingleResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("count", (BigInteger) results[0]);
		resultMap.put("amount", (BigDecimal) results[1]);
		return resultMap;
	}

	private String getQueryTextBlock(String block, String extraOrder){
		switch (block) {
		case "mainHeader":
			return 	"select c.id as id,con.title||' : '||ap.title as object,c.fio,c.contacts,c.whatsapp_number as whatsapp,min(s.date_payment) as date, sum(s.amount_to_pay-s.already_payed) as sum, "
					+ "c.note,cu.fio as curator,cu.id as curator_id,cast(DATE_PART('day',now()-min(s.date_payment)) as integer) as days, g.fio as garant, "
					+ "case when ap.doc_total_area is not null then ((ap.doc_total_area*c.price_for_square)-c.already_payed) else null end as sum_invent, c.curator_ork_note as curator_note, "
					+ "c.date_contract as contract_date,c.status  "
					+ " from schedule s "
					+ "	inner join client c on c.id=s.client_id "
					+ "	inner join appartment ap on c.appartment_id=ap.id"
					+ "	inner join construction con on con.id=ap.construction"
					+ "	inner join users cu on cu.id=c.curator_id"
					+ " left join garant g on g.id=c.garant_id "
					+ " where s.date_payment<cast(now() as date) and s.active=false and (ap.doc_total_area*c.price_for_square>(c.already_payed+0.01) or ap.doc_total_area is null)";
					
		case "mainFooter":
			return  " group by c.id,object,c.fio,c.contacts,whatsapp,c.note,cu.fio,cu.id, g.fio, c.price_for_square, ap.doc_total_area,ap.total_area, c.curator_note, c.date_contract,c.status "+
					 "ORDER BY "+extraOrder+" "+
			    	 "	date ASC "+
			    	 "	LIMIT ?1000" +
			    	 "	OFFSET ?1001";
			
		case "countHeader":
			return  "select count(distinct(c.id)) as total,sum(s.amount_to_pay-s.already_payed)  from schedule s "+ 
					"inner join client c on c.id=s.client_id  "+
					"inner join appartment ap on c.appartment_id=ap.id "+
					"inner join construction con on con.id=ap.construction "+
					"inner join users cu on cu.id=c.curator_id "+
					"left join garant g on g.id=c.garant_id "+
					"where s.date_payment<cast(now() as date) and s.active=false  and (ap.doc_total_area*c.price_for_square>(c.already_payed+0.01) or ap.doc_total_area is null)";
		case "countFooter":
			return "";
    	default:
			return "";
		}
	}

	
	
	

}
