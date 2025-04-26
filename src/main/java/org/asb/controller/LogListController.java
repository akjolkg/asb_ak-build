package org.asb.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.controller.admin.ClassifierController;
import org.asb.data.LogModel;
import org.asb.enums.LogType;
import org.asb.enums.ObjectType;
import org.asb.model.Log;
import org.asb.service.LogService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
@RolesAllowed(roles= {0,4,12})
public class LogListController extends ClassifierController {

	private static final long serialVersionUID = -5952534783254087382L;
	private LogModel model; 
	private Log log;
	
	
	@EJB
	private LogService service;
	private List<LogType> filterLogTypes;
	private List<ObjectType> filterObjectTypes;
	
	public LogListController() {}
	
	@PostConstruct
	private void init(){
		restoreState();
		filterData();
	}
	public void filter(){
		first=0;
		filterData();
	}
	
	public void filterData(){
		
		List<FilterExample> filters = new ArrayList<>();
		
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"description","%"+searchString+"%",InequalityConstants.LIKE,true));
		}
		if(filterKeys!=null && filterKeys.size()>0){
			filters.add(new FilterExample("client.keys",filterKeys,InequalityConstants.IN));
		}
		if(filterLogTypes!=null && filterLogTypes.size()>0){
			filters.add(new FilterExample("type",filterLogTypes,InequalityConstants.IN));
		}
		if(filterObjectTypes!=null && filterObjectTypes.size()>0){
			filters.add(new FilterExample("objectType",filterObjectTypes,InequalityConstants.IN));
		}
		if(filterCurators!=null && filterCurators.size()>0){
			filters.add(new FilterExample("user",filterCurators,InequalityConstants.IN));
		}
		
		if(filterDateFrom!=null)
			filters.add(new FilterExample("dateCreated",filterDateFrom,InequalityConstants.GREATER_OR_EQUAL));
		if(filterDateTo!=null){
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(filterDateTo);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			filters.add(new FilterExample("dateCreated",calendar.getTime(),InequalityConstants.LESSER_OR_EQUAL));
			
			
		}
		
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		
		model = new LogModel(filters, service);
		saveState();
		
	}
	
	
	public void clearFilter(){
		super.clearFilter();
		filterObjectTypes=null;
		filterLogTypes=null;
		filter();
	}
	
	public LogType[] getLogTypes() {
		return LogType.values();
	}
	public ObjectType[] getObjectTypes() {
		return ObjectType.values();
	}
	
	public LogModel getModel() {
		return model;
	}
	
	public void setModel(LogModel model) {
		this.model = model;
	}
	
	public Log getLog() {
		return log;
	}
	
	public void setLog(Log log) {
		this.log = log;
	}

	public List<LogType> getFilterLogTypes() {
		return filterLogTypes;
	}

	public void setFilterLogTypes(List<LogType> filterLogTypes) {
		this.filterLogTypes = filterLogTypes;
	}

	public List<ObjectType> getFilterObjectTypes() {
		return filterObjectTypes;
	}

	public void setFilterObjectTypes(List<ObjectType> filterObjectTypes) {
		this.filterObjectTypes = filterObjectTypes;
	}

}
