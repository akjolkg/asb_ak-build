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
import org.asb.data.CommentLogModel;

import org.asb.enums.ObjectType;
import org.asb.model.CommentLog;
import org.asb.service.CommentLogService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
@RolesAllowed(roles= {0,4,12})
public class CommentLogListController extends ClassifierController {

	private static final long serialVersionUID = -5952534783254087382L;
	private CommentLogModel model; 
	private CommentLog commentLog;
	
	
	@EJB
	private CommentLogService service;
	
	public CommentLogListController() {}
	
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
		if(filterCompanies!=null && filterCompanies.size()>0)
			filters.add(new FilterExample("appartment.construction.company",filterCompanies,InequalityConstants.IN));
		if(filterDevelopers!=null && filterDevelopers.size()>0)
			filters.add(new FilterExample("appartment.construction.developer",filterDevelopers,InequalityConstants.IN));
		if(filterConstructions!=null && filterConstructions.size()>0)
			filters.add(new FilterExample("appartment.construction",filterConstructions,InequalityConstants.IN));
		if(filterTitle!=null && filterTitle.length()>0)
			filters.add(new FilterExample("appartment.title",filterTitle+"%",InequalityConstants.LIKE,true));
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"fio","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"note","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"previousNote","%"+searchString+"%",InequalityConstants.LIKE,true));		}
		
		
		if(filterCuratorOrks!=null && filterCuratorOrks.size()>0){
			filters.add(new FilterExample("user",filterCuratorOrks,InequalityConstants.IN));
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
		
		model = new CommentLogModel(filters, service);
		saveState();
		
	}
	
	
	public void clearFilter(){
		super.clearFilter();
	
		filter();
	}
	
	
	
	public CommentLogModel getModel() {
		return model;
	}
	
	public void setModel(CommentLogModel model) {
		this.model = model;
	}
	
	public CommentLog getCommentLog() {
		return commentLog;
	}
	
	public void setCommentLog(CommentLog commentLog) {
		this.commentLog = commentLog;
	}

	


}
