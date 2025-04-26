package org.asb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.model.Appartment;
import org.asb.service.AppartmentService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class AppartmentDataModel extends LazyDataModel<Appartment> {

	private static final long serialVersionUID = 1575455424649374631L;
	private List<FilterExample> filters = new ArrayList<>();
	private int rowCount;
	private int totalCount;
	private AppartmentService service;

	public AppartmentDataModel(List<FilterExample> filters, AppartmentService service) {
		this.filters=filters;
		this.service = service;
		initRowCount();
	}
	
	@Override
	public Object getRowKey(Appartment entity) {
		return entity.getId();
	}
	
	@Override
	public Appartment getRowData(String key) {
		try {
			Integer id = Integer.parseInt(key);
			
			return service.findById(id, false);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	@Override	
	 public List<Appartment> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
		try {
			sortField = sortField == null ? "construction ASC, entity.flat ASC, entity.roomQuantity ASC, entity.blockNumber" : sortField;
			SortEnum sortEnum = SortEnum.ASCENDING;
			if(sortOrder!=null && sortOrder==SortOrder.DESCENDING) sortEnum=SortEnum.DESCENDING; 
			return service.findByExample(first, pageSize, sortEnum, this.filters, sortField,new String[]{"client"}); 
		} catch(Exception e){
			return Collections.emptyList();
		}
	}
	
	@Override
	public int getRowCount() {
		return rowCount;
	}
	
	public void setFilters(List<FilterExample> filters) {
		this.filters = filters;
		initRowCount();
	}
	
	public List<FilterExample> getFilters() {
		return filters;
	}
	
	private void initRowCount(){
		try {
			rowCount = this.filters == null || this.filters.isEmpty()
					? (int)service.countAll()
					: service.countByExample(filters,new String[]{"client"}).intValue();
			List<FilterExample> examples=new ArrayList<>();
			for(FilterExample fe:filters) {
				if(fe.getProperty().equals("type"))
					examples.add(fe);
			}
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			totalCount=service.countByExample(examples).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			rowCount = 0;
		}
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
