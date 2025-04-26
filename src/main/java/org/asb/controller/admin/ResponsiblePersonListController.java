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
import org.asb.data.ResponsiblePersonDataModel;
import org.asb.model.ResponsiblePerson;
import org.asb.service.ResponsiblePersonService;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
public class ResponsiblePersonListController implements Serializable {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private ResponsiblePersonDataModel model; 
	private ResponsiblePerson responsiblePerson;
	private Integer first;
	private String searchString;
	
	
	
	@EJB
	private ResponsiblePersonService service;

	public ResponsiblePersonListController() {}
	
	@PostConstruct
	private void init(){
		restoreState();
		filterData();
	}
	
	public void filterData(){
		
		List<FilterExample> filters = new ArrayList<>();
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		if(searchString!=null && searchString.length()>0) {
			filters.add(new FilterExample(true,"fio","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"contacts","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"firma","%"+searchString+"%",InequalityConstants.LIKE,true));
			
		}
			
		model = new ResponsiblePersonDataModel(filters, service);
		
	}
	

	public  void clearFilter(){
		searchString=null;
		filterData();
	}
	
	
	public ResponsiblePersonDataModel getModel() {
		return model;
	}
	
	public void setModel(ResponsiblePersonDataModel model) {
		this.model = model;
	}
	
	public ResponsiblePerson getResponsiblePerson() {
		return responsiblePerson;
	}
	
	public void setResponsiblePerson(ResponsiblePerson responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
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
		model = (ResponsiblePersonDataModel) session.getAttribute("model");
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


	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	} 

}
