package org.asb.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.asb.beans.SortEnum;
import org.asb.data.AppartmentDataModel;
import org.asb.enums.ClientType;
import org.asb.enums.Role;
import org.asb.model.Company;
import org.asb.model.Construction;
import org.asb.model.User;
import org.asb.model.Appartment;
import org.asb.service.CompanyService;
import org.asb.service.ConstructionService;
import org.asb.service.UserService;
import org.asb.service.AppartmentService;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
public class OfficesListController implements Serializable {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private AppartmentDataModel model; 
	private Appartment appartment;
	private Integer first;
	private Role filterRole;
	private String searchString;
	private List<Company> companies;
	private List<Construction> constructions;
	private List<User> curators;
	private List<Company> filterCompanies;
	private List<Construction> filterConstructions;
	private String filterBlock;
	private Integer filterEntrance;
	private List<Integer> filterFlats;
	private List<Integer> filterRoomQuantities;
	private String filterTitle;
	private List<ClientType> filterTypes;
	private List<Boolean> filterKeys;
	private List<Boolean> filterSigned;
	private List<User> filterCurators;
	private Date filterDateFrom;
	private Date filterDateTo;
	
	
	@EJB
	private AppartmentService service;
	@EJB
	private ConstructionService constructionService;
	@EJB
	private CompanyService companyService;
	@EJB
	private UserService userService;

	public OfficesListController() {}
	
	@PostConstruct
	private void init(){
		restoreState();
		filterData();
	}
	public void filter(){
		filterData();
		
	}
	
	public void filterData(){
		List<FilterExample> filters = new ArrayList<>();
		if(filterCompanies!=null && filterCompanies.size()>0)
			filters.add(new FilterExample("construction.company",filterCompanies,InequalityConstants.IN));
		if(filterConstructions!=null && filterConstructions.size()>0)
			filters.add(new FilterExample("construction",filterConstructions,InequalityConstants.IN));
		if(filterBlock!=null && filterBlock.length()>0)
			filters.add(new FilterExample("blockNumber",filterBlock+"%",InequalityConstants.LIKE,true));
		
		if(filterEntrance!=null)
			filters.add(new FilterExample("entranceNumber",filterEntrance,InequalityConstants.EQUAL));
		if(filterTitle!=null && filterTitle.length()>0)
			filters.add(new FilterExample("title",filterTitle+"%",InequalityConstants.LIKE,true));
		if(filterFlats!=null && filterFlats.size()>0){
			System.out.println(filterFlats);
			filters.add(new FilterExample("flat",filterFlats,InequalityConstants.IN));
		}
		if(filterRoomQuantities!=null && filterRoomQuantities.size()>0){
			System.out.println(filterRoomQuantities);
			filters.add(new FilterExample("roomQuantity",filterRoomQuantities,InequalityConstants.IN));
		}
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"client.fio","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.contacts","%"+searchString+"%",InequalityConstants.LIKE,true));
		}
		if(filterKeys!=null && filterKeys.size()>0){
			System.out.println(filterKeys);
			filters.add(new FilterExample("client.keys",filterKeys,InequalityConstants.IN));
		}
		if(filterSigned!=null && filterSigned.size()>0){
			System.out.println(filterSigned);
			filters.add(new FilterExample("client.signed",filterSigned,InequalityConstants.IN));
		}
		if(filterCurators!=null && filterCurators.size()>0){
			System.out.println(filterCurators);
			filters.add(new FilterExample("client.curator",filterCurators,InequalityConstants.IN));
		}
		if(filterDateFrom!=null){
			System.out.println(filterDateFrom);
			filters.add(new FilterExample("client.dateContract",filterDateFrom,InequalityConstants.GREATER_OR_EQUAL));
		}
		if(filterDateTo!=null){
			System.out.println(filterDateTo);
			filters.add(new FilterExample("client.dateContract",filterDateTo,InequalityConstants.LESSER_OR_EQUAL));
		}
		
		
		
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		model = new AppartmentDataModel(filters, service);
		
	}
	
	
	public AppartmentDataModel getModel() {
		return model;
	}
	
	public void setModel(AppartmentDataModel model) {
		this.model = model;
	}
	
	public Appartment getAppartment() {
		return appartment;
	}
	
	public void setAppartment(Appartment appartment) {
		this.appartment = appartment;
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
		model = (AppartmentDataModel) session.getAttribute("model");
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

	public List<Integer> getIntegerList(Integer from,Integer to){
		List<Integer> list=new ArrayList<>();
		while(from<=to){
			list.add(from++);
		}
		return list;
	}
	
	public void onCompanyChange() {
        constructions=null;
        getConstructions();
    }
	
	public List<Construction> getConstructions() {
		if(constructions==null) {
			List<FilterExample> examples=new ArrayList<>();
			if(filterCompanies!=null && filterCompanies.size()>0)
				examples.add(new FilterExample("company",filterCompanies,InequalityConstants.IN));
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			constructions=constructionService.findByExample(0, 10000, SortEnum.ASCENDING, examples, "title");
		}
		return constructions;
	}

	public void setConstructions(List<Construction> constructions) {
		this.constructions = constructions;
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

	public List<User> getCurators() {
		if(curators==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			curators=userService.findByExample(0, 1000, SortEnum.DESCENDING, examples, "id");
		}
		return curators;
	}

	public void setCurators(List<User> curators) {
		this.curators = curators;
	} 
	

	public String getFilterBlock() {
		return filterBlock;
	}

	public void setFilterBlock(String filterBlock) {
		this.filterBlock = filterBlock;
	}

	public Integer getFilterEntrance() {
		return filterEntrance;
	}

	public void setFilterEntrance(Integer filterEntrance) {
		this.filterEntrance = filterEntrance;
	}


	public String getFilterTitle() {
		return filterTitle;
	}

	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}

	public ClientType[] getClientTypes() {
		
		return ClientType.values();
	}
	public List<ClientType> getFilterTypes() {
		return filterTypes;
	}

	public void setFilterTypes(List<ClientType> filterTypes) {
		this.filterTypes = filterTypes;
	}

	public List<Boolean> getFilterKeys() {
		return filterKeys;
	}

	public void setFilterKeys(List<Boolean> filterKeys) {
		this.filterKeys = filterKeys;
	}

	public List<Boolean> getFilterSigned() {
		return filterSigned;
	}

	public void setFilterSigned(List<Boolean> filterSigned) {
		this.filterSigned = filterSigned;
	}

	public List<User> getFilterCurators() {
		return filterCurators;
	}

	public void setFilterCurators(List<User> filterCurators) {
		this.filterCurators = filterCurators;
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

	public List<Integer> getFilterRoomQuantities() {
		return filterRoomQuantities;
	}

	public void setFilterRoomQuantities(List<Integer> filterRoomQuantities) {
		this.filterRoomQuantities = filterRoomQuantities;
	}

	public List<Integer> getFilterFlats() {
		return filterFlats;
	}

	public void setFilterFlats(List<Integer> filterFlats) {
		this.filterFlats = filterFlats;
	}

	public List<Company> getFilterCompanies() {
		return filterCompanies;
	}

	public void setFilterCompanies(List<Company> filterCompanies) {
		this.filterCompanies = filterCompanies;
	}

	public List<Construction> getFilterConstructions() {
		return filterConstructions;
	}

	public void setFilterConstructions(List<Construction> filterConstructions) {
		this.filterConstructions = filterConstructions;
	}

}
