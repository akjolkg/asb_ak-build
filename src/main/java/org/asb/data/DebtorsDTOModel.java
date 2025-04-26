package org.asb.data;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asb.beans.SortEnum;
import org.asb.service.ScheduleService;
import org.asb.util.Serializer;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class DebtorsDTOModel extends LazyDataModel<DebtorsDTO> {

	private static final long serialVersionUID = -7546341313558743210L;
	protected ScheduleService service;
	private List<String> expressionsList = new ArrayList<String>(0);
	private Map<Integer, Object> parametersMap = new HashMap<Integer, Object>();
	
	private int rowCount;
	private List<DebtorsDTO> data = Collections.emptyList();
	private Serializer serializer;
	private Double totalSum;
	private Double totalSumInvent;
	
	

	public DebtorsDTOModel(List<String> expressionsList,Map<Integer, Object> parametersMap, Serializer serializer, ScheduleService service) {
		this.expressionsList=expressionsList;
		this.parametersMap=parametersMap;
		this.serializer = serializer;
		this.service=service;
		initRowCount();
	}
	
	@Override
	public Object getRowKey(DebtorsDTO entity) {
		try {
			return serializer.serialize(entity);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public DebtorsDTO getRowData(String key) {
		try {
			return (DebtorsDTO) serializer.deserialize(key);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	@Override	
	public List<DebtorsDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
		try {
			sortField = sortField == null ? "id" : sortField;
			
			
			SortEnum sortEnum = SortEnum.DESCENDING;
			if(sortOrder!=null && sortOrder==SortOrder.ASCENDING) sortEnum=SortEnum.ASCENDING; 
			data = getByExample(first, first + pageSize, sortEnum, sortField); 
			
			
			return data;
		} catch(Exception e){
			return Collections.emptyList();
		}
	}
	
	private void initRowCount(){
		try {
			Map<String, Object> rows=service.getModelTotal(expressionsList, parametersMap);
			if(rows.get("count")!=null)
				rowCount =((BigInteger)rows.get("count")).intValue();
			else 
				rowCount=0;
			if(rows.get("amount")!=null)
				totalSum =((BigDecimal)rows.get("amount")).doubleValue();
			else 
				totalSum=0.0d;	
			if(rows.get("inventAmount")!=null)
				totalSumInvent =((BigDecimal)rows.get("inventAmount")).doubleValue();
			else 
				totalSumInvent=0.0d;	
			
			
		} catch (Exception e) {
			e.printStackTrace();
			rowCount = 0;
		}
	}
	
	
	private List<DebtorsDTO> getByExample(int first, int pageSize, SortEnum sortEnum, String sortField) {
		String extraOrder="";
		for(String expressions:expressionsList){
			if(expressions.contains("DESC")){
				extraOrder=expressions;			
			}
			
		}
		
		
		return service.getModel(pageSize, first, expressionsList, parametersMap,extraOrder);
					
	}
	
	@Override
	public int getRowCount() {
		return rowCount;
	}

	public Double getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(Double totalSum) {
		this.totalSum = totalSum;
	}

	public Double getTotalSumInvent() {
		return totalSumInvent;
	}

	public void setTotalSumInvent(Double totalSumInvent) {
		this.totalSumInvent = totalSumInvent;
	}
		
}
