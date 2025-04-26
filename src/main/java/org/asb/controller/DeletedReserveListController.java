package org.asb.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.data.DeletedReserveDataModel;
import org.asb.model.DeletedReserve;
import org.asb.model.Denounce;
import org.asb.service.DeletedReserveService;
import org.asb.util.web.LoginUtil;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.PageEvent;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
public class DeletedReserveListController implements Serializable {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private DeletedReserveDataModel model; 
	private DeletedReserve deletedReserve;
	private String searchString;
	private Integer first;
	private Date filterDateFrom;
	private Date filterDateTo;
	@Inject
	protected LoginUtil loginUtil;
	
	
	@EJB
	private DeletedReserveService service;
	
	

	public DeletedReserveListController() {}
	
	@PostConstruct
	private void init(){
		filterData();
	}
	
	public void filter(){
		filterData();		
	}
	
	public void clearFilter(){
		searchString=null;
		filterData();		
	}
	public void filterData(){
		
		List<FilterExample> filters = new ArrayList<>();
		if(getFilterDateFrom()!=null)
			filters.add(new FilterExample("dateCreated",getFilterDateFrom(),InequalityConstants.GREATER_OR_EQUAL));
		if(getFilterDateTo()!=null){
			filters.add(new FilterExample("dateCreated",getFilterDateTo(),InequalityConstants.LESSER_OR_EQUAL));
		}
		if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			filters.add(new FilterExample("appartment.construction",loginUtil.getCurrentUser().getConstructions(),InequalityConstants.IN));
		}
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"fio","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"headInfo","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"note","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
		}
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		
		model = new DeletedReserveDataModel(filters, service);
		
	}
	
	public void saveState() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("first", getFirst());
	}
	
	public void restoreState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		setFirst((Integer) session.getAttribute("first"));
	}
	
	public void removeState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("first", null);
		setFirst(null);
	}
	
	public void onPageChange(PageEvent event) {  
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		setFirst(((DataTable) event.getSource()).getRows() * event.getPage());
		session.setAttribute("first", getFirst());
	}
	
	public DeletedReserveDataModel getModel() {
		return model;
	}
	
	public void setModel(DeletedReserveDataModel model) {
		this.model = model;
	}
	
	public DeletedReserve getDeletedReserve() {
		return deletedReserve;
	}
	
	public void setDeletedReserve(DeletedReserve deletedReserve) {
		this.deletedReserve = deletedReserve;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public Integer getFirst() {
		return first;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public Date getFilterDateFrom() {
		return filterDateFrom;
	}

	public void setFilterDateFrom(Date filterDateFrom) {
		this.filterDateFrom = filterDateFrom;
	}

	public Date getFilterDateTo() {
		return filterDateTo;
	}

	public void setFilterDateTo(Date filterDateTo) {
		this.filterDateTo = filterDateTo;
	}

	
	
}
