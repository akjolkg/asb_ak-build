package org.asb.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.data.UserDataModel;
import org.asb.enums.Role;
import org.asb.enums.UserStatus;
import org.asb.model.User;
import org.asb.service.UserService;
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
public class UserListController implements Serializable {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private UserDataModel model; 
	private User user;
	private Integer first;
	private Role filterRole;
	private UserStatus filterStatus;
	private String searchString;
	
	
	
	@EJB
	private UserService service;

	public UserListController() {}
	
	@PostConstruct
	private void init(){
		restoreState();
		filterData();
	}
	
	public void filterData(){
		
		List<FilterExample> filters = new ArrayList<>();
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		
		if(searchString!=null && searchString.length()>0) {
			filters.add(new FilterExample(true,"fio","%"+searchString+"%",InequalityConstants.LIKE,false));
			filters.add(new FilterExample(true,"username","%"+searchString+"%",InequalityConstants.LIKE,false));
			filters.add(new FilterExample(true,"pin","%"+searchString+"%",InequalityConstants.LIKE,false));
			
			
			
			
			
		}
		
		
		model = new UserDataModel(filters, service);
		
	}
	
	public void filter(){
		first=0;
		filterData();		
	}
	
	public void clearFilter(){
		searchString=null;
		filter();
	}
	
	
	public UserDataModel getModel() {
		return model;
	}
	
	public void setModel(UserDataModel model) {
		this.model = model;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Integer getFirst() {
		return first;
	}
	
	public void setFirst(Integer first) {
		this.first = first;
	}
	
	public void saveState() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("model", model);
		session.setAttribute("first", first);
	}
	
	public void restoreState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		model = (UserDataModel) session.getAttribute("model");
		first = (Integer) session.getAttribute("first");
	}
	
	public void removeState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("model", null);
		session.setAttribute("first", null);
		
		model = null;
		first = null;
	}
	
	public void onPageChange(PageEvent event) {  
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		setFirst(((DataTable) event.getSource()).getRows() * event.getPage());
		session.setAttribute("first", first);
	}

	public UserStatus[] getStatuses(){
		return UserStatus.values();
	}
	
	public Role getFilterRole() {
		return filterRole;
	}

	public void setFilterRole(Role filterRole) {
		this.filterRole = filterRole;
	}

	public UserStatus getFilterStatus() {
		return filterStatus;
	}

	public void setFilterStatus(UserStatus filterStatus) {
		this.filterStatus = filterStatus;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	} 

}
