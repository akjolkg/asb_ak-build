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
import org.asb.data.ContractTemplateDataModel;
import org.asb.enums.ClientFizYur;
import org.asb.enums.ContractTemplateType;
import org.asb.enums.Role;
import org.asb.model.Construction;
import org.asb.model.ContractTemplate;
import org.asb.service.ConstructionService;
import org.asb.service.ContractTemplateService;
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
public class ContractTemplateListController implements Serializable {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private ContractTemplateDataModel model; 
	private ContractTemplate company;
	private Integer first;
	private Role filterRole;
	private String searchString;
	private List<Construction> constructions;
	private List<Construction> filterConstructions;
	private List<ContractTemplate> filterContractTemplates;
	private List<ClientFizYur> filterFizYur;
	
	
	@Inject
	private LoginUtil loginUtil;
	
	@EJB
	private ConstructionService constructionService;
	
	
	@EJB
	private ContractTemplateService service;

	public ContractTemplateListController() {}
	
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
		if (filterConstructions != null && !filterConstructions.isEmpty()) {
			filters.add(new FilterExample("construction", filterConstructions, InequalityConstants.IN));
		}
		if (filterContractTemplates != null && !filterContractTemplates.isEmpty()) {
			filters.add(new FilterExample("type", filterContractTemplates, InequalityConstants.IN));
		}
		if (filterFizYur != null && !filterFizYur.isEmpty()) {
			filters.add(new FilterExample("fizYur", filterFizYur, InequalityConstants.IN));
		}
		
		
		model = new ContractTemplateDataModel(filters, service);
		
	}
	
	
	public ContractTemplateDataModel getModel() {
		return model;
	}
	
	public void setModel(ContractTemplateDataModel model) {
		this.model = model;
	}
	
	public ContractTemplate getContractTemplate() {
		return company;
	}
	
	public void setContractTemplate(ContractTemplate company) {
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
		model = (ContractTemplateDataModel) session.getAttribute("model");
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

	public List<Construction> getConstructions() {
		if(constructions==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			constructions=constructionService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "title");
			if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
				constructions=new ArrayList<>();
				constructions.addAll(loginUtil.getCurrentUser().getConstructions());
			}
		}
		return constructions;
	}

	public void clearFilter() {
		searchString = null;
		filterConstructions = null;
		filterContractTemplates = null;
		filterFizYur = null;
		filterData();
	}
	public void setConstructions(List<Construction> constructions) {
		this.constructions = constructions;
	}

	public List<Construction> getFilterConstructions() {
		return filterConstructions;
	}

	public void setFilterConstructions(List<Construction> filterConstructions) {
		this.filterConstructions = filterConstructions;
	}

	public List<ContractTemplate> getFilterContractTemplates() {
		return filterContractTemplates;
	}

	public void setFilterContractTemplates(List<ContractTemplate> filterContractTemplates) {
		this.filterContractTemplates = filterContractTemplates;
	} 
	
	public ContractTemplateType[] getAllContractTemplateTypes() {
		return ContractTemplateType.values();
	}
	public ClientFizYur[] getAllFizYur() {
		return ClientFizYur.values();
	}

	public List<ClientFizYur> getFilterFizYur() {
		return filterFizYur;
	}

	public void setFilterFizYur(List<ClientFizYur> filterFizYur) {
		this.filterFizYur = filterFizYur;
	}

}
