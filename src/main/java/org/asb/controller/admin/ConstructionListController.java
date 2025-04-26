package org.asb.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.asb.data.ConstructionDataModel;
import org.asb.enums.Role;
import org.asb.model.Company;
import org.asb.model.Construction;
import org.asb.service.CompanyService;
import org.asb.service.ConstructionService;
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
public class ConstructionListController extends ClassifierController {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private ConstructionDataModel model; 
	private Construction construction;
	private Integer first;
	private Role filterRole;
	private String searchString;
	private List<Company> companies;
	
	
	
	@EJB
	private ConstructionService service;
	@EJB
	private CompanyService companyService;

	@Inject 
	private LoginUtil loginUtil;
	
	public ConstructionListController() {}
	
	@PostConstruct
	private void init(){
		restoreState();
		filterData();
	}
	
	public void filterData(){
		
		List<FilterExample> filters = new ArrayList<>();
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		
		if(filterCompanies!=null && filterCompanies.size()>0)
			filters.add(new FilterExample("company",filterCompanies,InequalityConstants.IN));
		if(filterDevelopers!=null && filterDevelopers.size()>0)
			filters.add(new FilterExample("developer",filterDevelopers,InequalityConstants.IN));
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"title","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"plannedAddress","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"code","%"+searchString+"%",InequalityConstants.LIKE,true));
		}
		
		if(filterDateFrom!=null){
			filters.add(new FilterExample("plannedDate",filterDateFrom,InequalityConstants.GREATER_OR_EQUAL));
		}
		if(filterDateTo!=null){
			Calendar c=Calendar.getInstance();
			c.setTime(filterDateTo);
			c.add(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MILLISECOND, -1);
			
			filters.add(new FilterExample("plannedDate",c.getTime(),InequalityConstants.LESSER_OR_EQUAL));
		}
		if(filterDateFrom2!=null){
			filters.add(new FilterExample("realDate",filterDateFrom2,InequalityConstants.GREATER_OR_EQUAL));
		}
		if(filterDateTo2!=null){
			Calendar c=Calendar.getInstance();
			c.setTime(filterDateTo2);
			c.add(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MILLISECOND, -1);
			
			filters.add(new FilterExample("realDate",c.getTime(),InequalityConstants.LESSER_OR_EQUAL));
		}
		
		if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			List<Integer> ids=new ArrayList<>();
			for (Construction c:loginUtil.getCurrentUser().getConstructions())
				ids.add(c.getId());
			filters.add(new FilterExample("id",ids,InequalityConstants.IN));
		}
		model = new ConstructionDataModel(filters, service);
		
	}
	
	public void clearFilter(){
		super.clearFilter();
		filterData();
	}
	
	
	public ConstructionDataModel getModel() {
		return model;
	}
	
	public void setModel(ConstructionDataModel model) {
		this.model = model;
	}
	
	public Construction getConstruction() {
		return construction;
	}
	
	public void setConstruction(Construction construction) {
		this.construction = construction;
	}
	
	public Integer getFirst() {
		return first;
	}
	
	public void setFirst(Integer first) {
		this.first = first;
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

	public List<Company> getCompanies() {
		if(companies==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			companies=companyService.findByExample(0, 1000, SortEnum.DESCENDING, examples, "id");
		}
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	} 

}
