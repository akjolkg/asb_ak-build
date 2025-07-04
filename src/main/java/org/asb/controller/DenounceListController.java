package org.asb.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.asb.controller.admin.ClassifierController;
import org.asb.data.DenounceDataModel;
import org.asb.enums.ClientStatus;
import org.asb.model.Denounce;
import org.asb.service.DenounceService;
import org.asb.util.web.LoginUtil;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
@RolesAllowed(roles=0)
public class DenounceListController implements Serializable {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private DenounceDataModel model; 
	private Denounce denounce;
	private String searchString;
	private Integer first;
	private Date filterDateFrom;
	private Date filterDateTo;
	private List<ClientStatus> filterStatus;
	
	@Inject
	private LoginUtil loginUtil;
	
	@EJB
	private DenounceService service;
	
	

	public DenounceListController() {}
	
	@PostConstruct
	private void init(){
		restoreState();
		filterData();
	}
	
	public void filter(){
		first=0;
		saveState();
		filterData();		
	}
	
	public void clearFilter(){
		first=0;
		searchString=null;
		filterDateFrom=null;
		filterDateTo=null;
		filterStatus=null;
		saveState();
		filterData();		
	}
	public void filterData(){
		
		List<FilterExample> filters = new ArrayList<>();
		if(getFilterDateFrom()!=null)
			filters.add(new FilterExample("dateDenounce",getFilterDateFrom(),InequalityConstants.GREATER_OR_EQUAL));
		if(getFilterDateTo()!=null){
			filters.add(new FilterExample("dateDenounce",getFilterDateTo(),InequalityConstants.LESSER_OR_EQUAL));
		}
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"fio","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"headInfo","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"note","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"history","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
		}
		if(filterStatus!=null && filterStatus.size()>0){
			filters.add(new FilterExample("status",filterStatus,InequalityConstants.IN));
		}
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			filters.add(new FilterExample("appartment.construction",loginUtil.getCurrentUser().getConstructions(),InequalityConstants.IN));
		}
		
		model = new DenounceDataModel(filters, service);
		
	}
	
	public String getCurator(Denounce d) {
		String k="";
		try {
			if(d!=null && d.getHistory()!=null) {
				Integer start=d.getHistory().indexOf("<span class=\"form-table-label disp-blc\">Куратор</span>");
				k=d.getHistory().substring(start,start+200);
				start=k.indexOf("<td>")+4;
				k=k.substring(start);
				Integer end=k.indexOf("</td>");
				k=k.substring(0,end);
				
			}
		}catch (Exception e) {
		
			// TODO: handle exception
		}
		return k;
	}
	public String getCuratorOrk(Denounce d) {
		String k="";
		try {
			if(d!=null && d.getHistory()!=null) {
				Integer start=d.getHistory().indexOf("<span class=\"form-table-label disp-blc\">Куратор ОРК</span>");
				k=d.getHistory().substring(start,start+200);
				System.out.println("1--"+k+"--1");
				start=k.indexOf("<td>")+4;
				System.out.println("1--"+start+"--1");
				k=k.substring(start);
				System.out.println("1--"+k+"--1");
				Integer end=k.indexOf("</td>");
				
				k=k.substring(0,end);
				
				System.out.println("--"+k+"--");
			}
		}catch (Exception e) {
		
			// TODO: handle exception
		}
		return k;
	}
	
	
	
	public void saveState() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("firstD", getFirst());
		session.setAttribute("searchStringD", searchString);
		session.setAttribute("dateFromD", filterDateFrom);
		session.setAttribute("dateToD", filterDateTo);
		session.setAttribute("filterStatus", filterStatus);
	}
	
	@SuppressWarnings("unchecked")
	public void restoreState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		setFirst((Integer) session.getAttribute("firstD"));
		setSearchString((String) session.getAttribute("searchStringD"));
		filterDateFrom=(Date)session.getAttribute("dateFromD");		
		filterDateTo=(Date)session.getAttribute("dateToD");
		filterStatus=(List<ClientStatus>)session.getAttribute("filterStatus");
	}
	
	public void removeState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("firstD", null);
		setFirst(null);
	}
	
	public void onPageChange(PageEvent event) {  
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		setFirst(((DataTable) event.getSource()).getRows() * event.getPage());
		session.setAttribute("firstD", getFirst());
	}
	
	public ClientStatus[] getClientStatuses() {
		return ClientStatus.values();
	}
	public DenounceDataModel getModel() {
		return model;
	}
	
	public void setModel(DenounceDataModel model) {
		this.model = model;
	}
	
	public Denounce getDenounce() {
		return denounce;
	}
	
	public void setDenounce(Denounce denounce) {
		this.denounce = denounce;
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

	public List<ClientStatus> getFilterStatus() {
		return filterStatus;
	}

	public void setFilterStatus(List<ClientStatus> filterStatus) {
		this.filterStatus = filterStatus;
	}

	
	
}
