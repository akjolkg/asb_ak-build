package org.asb.controller.admin;

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
import org.asb.data.DocumentTemplateDataModel;
import org.asb.enums.DocumentTemplateType;
import org.asb.enums.Role;
import org.asb.model.Construction;
import org.asb.model.DocumentTemplate;
import org.asb.service.ConstructionService;
import org.asb.service.DocumentTemplateService;
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
public class DocumentTemplateListController implements Serializable {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private DocumentTemplateDataModel model; 
	private DocumentTemplate company;
	private Integer first;
	private Role filterRole;
	private String searchString;
	private List<DocumentTemplate> filterDocumentTemplates;
	
	
	@Inject
	private LoginUtil loginUtil;
	
	@EJB
	private ConstructionService constructionService;
	
	
	@EJB
	private DocumentTemplateService service;

	public DocumentTemplateListController() {}
	
	@PostConstruct
	private void init(){
		restoreState();
		filterData();
	}
	
	public void filterData(){
		
		List<FilterExample> filters = new ArrayList<>();
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		
		if (searchString != null && !searchString.isEmpty()) {
			filters.add(new FilterExample("attachment.fileName", "%"+searchString+"%", InequalityConstants.LIKE,true));
		}
		if (filterDocumentTemplates != null && !filterDocumentTemplates.isEmpty()) {
			filters.add(new FilterExample("type", filterDocumentTemplates, InequalityConstants.IN));
		}
		
		
		model = new DocumentTemplateDataModel(filters, service);
		
	}
	
	
	public DocumentTemplateDataModel getModel() {
		return model;
	}
	
	public void setModel(DocumentTemplateDataModel model) {
		this.model = model;
	}
	
	public DocumentTemplate getDocumentTemplate() {
		return company;
	}
	
	public void setDocumentTemplate(DocumentTemplate company) {
		this.company = company;
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
		model = (DocumentTemplateDataModel) session.getAttribute("model");
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

	
	
	public Role getFilterRole() {
		return filterRole;
	}

	public void setFilterRole(Role filterRole) {
		this.filterRole = filterRole;
	}

	
	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	

	public void clearFilter() {
		searchString = null;
		filterDocumentTemplates = null;
		filterData();
	}
	

	public List<DocumentTemplate> getFilterDocumentTemplates() {
		return filterDocumentTemplates;
	}

	public void setFilterDocumentTemplates(List<DocumentTemplate> filterDocumentTemplates) {
		this.filterDocumentTemplates = filterDocumentTemplates;
	} 
	
	public DocumentTemplateType[] getAllDocumentTemplateTypes() {
		return DocumentTemplateType.values();
	}

}
