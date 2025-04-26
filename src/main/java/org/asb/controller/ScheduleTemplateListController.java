package org.asb.controller;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.asb.data.ScheduleTemplateModel;
import org.asb.enums.ClientStatus;
import org.asb.enums.Role;
import org.asb.model.ScheduleTemplate;
import org.asb.model.User;
import org.asb.service.ScheduleTemplateService;
import org.asb.service.UserService;
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
public class ScheduleTemplateListController implements Serializable {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private ScheduleTemplateModel model; 
	private ScheduleTemplate ScheduleTemplate;
	private String searchString;
	private Integer first;
	private List<User> filterCuratorOrks;
	private List<ClientStatus> filterStatuses;
	
	@Inject
	private LoginUtil loginUtil;
	
	@EJB
	private ScheduleTemplateService service;
	@EJB
	private UserService userService;
	
	

	public ScheduleTemplateListController() {}
	
	@PostConstruct
	private void init(){
		restoreState();
		filterData();
	}
	
	public void filter(){
		filterData();	
		saveState();
	}
	
	public void clearFilter(){
		searchString=null;
		filterCuratorOrks=null;
		filterStatuses=null;
		filterData();		
		saveState();
	}
	public void filterData(){
		
		List<FilterExample> filters = new ArrayList<>();
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"client.appartment.title","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.appartment.construction.title","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"note","%"+getSearchString()+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.contractNumber","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.fio","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.uptFio","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.contacts","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.note","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.whatsappNumber","%"+searchString+"%",InequalityConstants.LIKE,true));
		}
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			filters.add(new FilterExample("client.appartment.construction",loginUtil.getCurrentUser().getConstructions(),InequalityConstants.IN));
		}
		
		if(filterStatuses!=null && !filterStatuses.isEmpty()) {
			filters.add(new FilterExample("status",filterStatuses,InequalityConstants.IN));
		}
		if(filterCuratorOrks!=null && !filterCuratorOrks.isEmpty()) {
			filters.add(new FilterExample("client.curatorOrk",filterCuratorOrks,InequalityConstants.IN));
		}
		
		
		model = new ScheduleTemplateModel(filters, service);
		
	}
	
	public void saveState() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("first", getFirst());
		session.setAttribute("searchStringD", searchString);
		session.setAttribute("filterCuratorOrks", filterCuratorOrks);
		session.setAttribute("filterStatuses", filterStatuses);
	}
	
	@SuppressWarnings("unchecked")
	public void restoreState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		setFirst((Integer) session.getAttribute("first"));
		setSearchString((String) session.getAttribute("searchStringD"));
		setFilterCuratorOrks((List<User>) session.getAttribute("filterCuratorOrks"));
		setFilterStatuses((List<ClientStatus>) session.getAttribute("filterStatuses"));
	}
	
	public void removeState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("first", null);
		session.setAttribute("searchStringD", null);
		session.setAttribute("filterCuratorOrks", null);
		session.setAttribute("filterStatuses", null);
		setFirst(null);
	}
	
	public void onPageChange(PageEvent event) {  
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		setFirst(((DataTable) event.getSource()).getRows() * event.getPage());
		session.setAttribute("first", getFirst());
	}
	
	private List<User> curatorOrks;
	public List<User> getCuratorOrks() {
		if(curatorOrks==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			examples.add(new FilterExample("role",Role.ORK, InequalityConstants.EQUAL));
			curatorOrks=userService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "fio");
		}
		return curatorOrks;
	}
	
	
	public ClientStatus[] getClientStatuses() {
		return ClientStatus.values();
	}
	public ScheduleTemplateModel getModel() {
		return model;
	}
	
	public void setModel(ScheduleTemplateModel model) {
		this.model = model;
	}
	
	public ScheduleTemplate getScheduleTemplate() {
		return ScheduleTemplate;
	}
	
	public void setScheduleTemplate(ScheduleTemplate ScheduleTemplate) {
		this.ScheduleTemplate = ScheduleTemplate;
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

	public List<User> getFilterCuratorOrks() {
		return filterCuratorOrks;
	}

	public void setFilterCuratorOrks(List<User> filterCuratorOrks) {
		this.filterCuratorOrks = filterCuratorOrks;
	}

	public List<ClientStatus> getFilterStatuses() {
		return filterStatuses;
	}

	public void setFilterStatuses(List<ClientStatus> filterStatuses) {
		this.filterStatuses = filterStatuses;
	}

	
	
}
