package org.asb.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.enums.ClientFrom;
import org.asb.enums.ClientStatus;
import org.asb.enums.ClientType;
import org.asb.enums.ContractDateType;
import org.asb.enums.ContractType;
import org.asb.enums.Loyality;
import org.asb.enums.PlanType;
import org.asb.enums.Role;
import org.asb.enums.UserStatus;
import org.asb.model.Appartment;
import org.asb.model.Company;
import org.asb.model.Construction;
import org.asb.model.Garant;
import org.asb.model.ResponsiblePerson;
import org.asb.model.User;
import org.asb.service.CompanyService;
import org.asb.service.ConstructionService;
import org.asb.service.GarantService;
import org.asb.service.ResponsiblePersonService;
import org.asb.service.UserService;
import org.asb.util.web.LoginUtil;
import org.asb.util.web.Messages;
import org.asb.service.AppartmentService;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;
import org.asb.enums.AppartmentType;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class ClassifierController implements Serializable {

	private static final long serialVersionUID = -3292487804020367668L;
	protected Integer first;
	protected Role filterRole;
	protected String searchString;
	protected List<Company> companies;
	protected List<Company> developers;
	protected List<Construction> constructions;
	protected List<User> curators;
	protected List<User> curatorOrks;
	protected List<Garant> garants;
	protected List<ResponsiblePerson> rps;
	protected List<Company> filterCompanies;
	protected List<Construction> filterConstructions;
	protected String filterBlock;
	protected List<String> filterBlocks;
	protected String filterSnText;
	protected Integer filterEntrance;
	protected List<Integer> filterEntrances;
	protected List<Integer> filterFlats;
	protected List<Integer> filterRoomQuantities;
	protected String filterTitle;
	protected List<ClientType> filterTypes;
	protected List<PlanType> filterPtypes;
	protected List<ClientFrom> filterFroms;
	protected List<Boolean> filterKeys;
	protected List<Boolean> filterSigned;
	protected List<Boolean> filterExtralow;
	protected List<User> filterCurators;
	protected List<User> filterCuratorOrks;
	protected Date filterDateFrom;
	protected Date filterDateTo;
	protected Date filterDateFrom2;
	protected Date filterDateTo2;
	protected List<ClientStatus> filterStatus;
	protected List<Garant> filterGarants;
	protected List<ResponsiblePerson> filterResponsiblePersons; 
	protected List<Company> filterDevelopers;
	protected List<Boolean> filterSn;
	protected List<AppartmentType> filterAppartmentTypes;
	protected List<Loyality> filterLoyalitites;
	protected List<ContractType> filterContractTypes;

	protected ContractDateType filterContractDateType;
	
	
	
	@EJB
	protected AppartmentService apservice;
	@EJB
	protected ConstructionService constructionService;
	@EJB
	protected CompanyService companyService;
	@EJB
	protected UserService userService;
	@EJB
	protected ResponsiblePersonService rpService;
	@EJB
	protected GarantService garantService;
	@Inject
	protected LoginUtil loginUtil;

	public ClassifierController() {}
	
	
	
	public  void clearFilter(){
		filterLoyalitites=null;
		filterCompanies = null;
		filterDevelopers = null;
		filterConstructions = null;
		filterBlock = null;
		filterBlocks = null;
		filterEntrance = null;
		filterEntrances = null;
		filterFlats = null;
		filterRoomQuantities = null;
		filterTitle = null;
		filterTypes = null;
		filterKeys = null;
		filterSigned = null;
		filterCurators = null;
		filterCuratorOrks = null;
		filterDateFrom = null;
		filterDateTo = null;
		filterDateFrom2 = null;
		filterDateTo2 = null;
		searchString=null;
		filterFroms=null;
		filterStatus=null; 
		filterGarants=null;
		filterResponsiblePersons=null;
		filterSn=null;
		filterSnText=null;
		filterPtypes=null;
		filterAppartmentTypes=null;
		filterContractTypes=null;
		filterContractDateType=null;
		filterExtralow=null;
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
		session.setAttribute("first", first);		
		session.setAttribute("filterCompanies",filterCompanies);
		session.setAttribute("filterDevelopers",filterDevelopers);
		session.setAttribute("filterConstructions",filterConstructions);
		session.setAttribute("filterBlock",filterBlock);
		session.setAttribute("filterBlocks",filterBlocks);
		session.setAttribute("filterEntrance",filterEntrance);
		session.setAttribute("filterEntrances",filterEntrances);
		session.setAttribute("filterFlats",filterFlats);
		session.setAttribute("filterRoomQuantities",filterRoomQuantities);
		session.setAttribute("filterTitle",filterTitle);
		session.setAttribute("filterTypes",filterTypes);
		session.setAttribute("filterKeys",filterKeys);
		session.setAttribute("filterSigned",filterSigned);
		session.setAttribute("filterCurators",filterCurators);
		session.setAttribute("filterCuratorOrks",filterCuratorOrks);
		session.setAttribute("filterDateFrom",filterDateFrom);
		session.setAttribute("filterDateTo",filterDateTo);
		session.setAttribute("filterDateFrom2",filterDateFrom2);
		session.setAttribute("filterDateTo2",filterDateTo2);
		session.setAttribute("searchString",searchString);
		session.setAttribute("filterFroms",filterFroms);
		session.setAttribute("filterStatus",filterStatus);
		session.setAttribute("filterGarants",filterGarants);
		session.setAttribute("filterExtralow",filterExtralow);
		session.setAttribute("filterRp",filterResponsiblePersons);
		session.setAttribute("filterSn",filterSn);
		session.setAttribute("filterSnText",filterSnText);
		session.setAttribute("filterPtypes",filterPtypes);
		session.setAttribute("filterAppartmentTypes",filterAppartmentTypes);
		session.setAttribute("filterLoyalitites",filterLoyalitites);
		session.setAttribute("filterContractTypes",filterContractTypes);
		session.setAttribute("filterContractDateType",filterContractDateType);
		
		
		
		
		
		
	}
	
	public void restoreState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		first = (Integer) session.getAttribute("first");
		filterCompanies= (List<Company>)session.getAttribute("filterCompanies");
		filterDevelopers= (List<Company>)session.getAttribute("filterDevelopers");
		filterConstructions= (List<Construction>)session.getAttribute("filterConstructions");
		filterBlock= (String) session.getAttribute("filterBlock");
		filterBlocks= (List<String>) session.getAttribute("filterBlocks");
		filterEntrance= (Integer) session.getAttribute("filterEntrance");
		filterEntrances= (List<Integer>) session.getAttribute("filterEntrances");
		filterFlats= (List<Integer>) session.getAttribute("filterFlats");
		filterRoomQuantities= (List<Integer>) session.getAttribute("filterRoomQuantities");
		filterTitle= (String) session.getAttribute("filterTitle");
		filterTypes= (List<ClientType>) session.getAttribute("filterTypes");
		filterKeys= (List<Boolean>) session.getAttribute("filterKeys");
		filterSigned= (List<Boolean>) session.getAttribute("filterSigned");
		filterExtralow= (List<Boolean>) session.getAttribute("filterExtralow");
		filterCurators= (List<User>) session.getAttribute("filterCurators");
		filterCuratorOrks= (List<User>) session.getAttribute("filterCuratorOrks");
		filterDateFrom= (Date) session.getAttribute("filterDateFrom");
		filterDateTo= (Date) session.getAttribute("filterDateTo");
		filterDateFrom2= (Date) session.getAttribute("filterDateFrom2");
		filterDateTo2= (Date) session.getAttribute("filterDateTo2");
		searchString= (String) session.getAttribute("searchString");
		filterFroms= (List<ClientFrom>) session.getAttribute("filterFroms");
		filterStatus= (List<ClientStatus>) session.getAttribute("filterStatus");
		filterGarants= (List<Garant>) session.getAttribute("filterGarants");
		filterResponsiblePersons= (List<ResponsiblePerson>) session.getAttribute("filterRp");
		filterSn= (List<Boolean>) session.getAttribute("filterSn");
		filterSnText= (String) session.getAttribute("filterSnText");
		filterPtypes= (List<PlanType>) session.getAttribute("filterPtypes");
		filterAppartmentTypes= (List<AppartmentType>) session.getAttribute("filterAppartmentTypes");
		filterLoyalitites=(List<Loyality>) session.getAttribute("filterLoyalities");
		filterContractTypes=(List<ContractType>) session.getAttribute("filterContractTypes");
		setFilterContractDateType((ContractDateType) session.getAttribute("filterContractDateType"));
	}
	
	public void removeState() {
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("first", null);
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

	public void onCompanyChange() {
        constructions=null;
        getConstructions();
    }
	
	public List<Construction> getConstructions() {
		if(constructions==null) {
			List<FilterExample> examples=new ArrayList<>();
			if(filterCompanies!=null && filterCompanies.size()>0)
				examples.add(new FilterExample("company",filterCompanies,InequalityConstants.IN));
			if(filterDevelopers!=null && filterDevelopers.size()>0)
				examples.add(new FilterExample("developer",filterDevelopers,InequalityConstants.IN));
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			constructions=constructionService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "title");
			if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
				constructions=new ArrayList<>();
				constructions.addAll(loginUtil.getCurrentUser().getConstructions());
			}
		}
		return constructions;
	}

	public void setConstructions(List<Construction> constructions) {
		this.constructions = constructions;
	}
	
	
	public List<Company> getCompanies() {
		if(companies==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("main",true,InequalityConstants.EQUAL));
			companies=companyService.findByExample(0, 1000, SortEnum.DESCENDING, examples, "id");
			if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
				companies=new ArrayList<>();
			}
		}
		return companies;
	}
	
	

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public List<User> getCurators() {
		if(curators==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("status",UserStatus.ACTIVE, InequalityConstants.EQUAL));
			examples.add(new FilterExample(true,"role",Role.ADMIN, InequalityConstants.EQUAL,false));
			examples.add(new FilterExample(true,"role",Role.FILIAL, InequalityConstants.EQUAL,false));			
			examples.add(new FilterExample(true,"role",Role.JUNIOR, InequalityConstants.EQUAL,false));
			examples.add(new FilterExample(true,"role",Role.SUBADMIN, InequalityConstants.EQUAL,false));
			curators=userService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "fio");
		}
		return curators;
	}

	
	public List<Integer> getIntegerList(Integer from,Integer to){
		List<Integer> list=new ArrayList<>();
		while(from<=to){
			list.add(from++);
		}
		return list;
	}
	
	public List<String> getBlockList(){
		List<String> list=new ArrayList<>();
		String alphabet="АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЭЮЯ";
		
		for(char ch:alphabet.toCharArray()) {
			list.add(ch+"");
		}
		
		
		
		return list;
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

	public ClientStatus[] getClientStatuses() {
		return ClientStatus.values();
	}
	
	public ContractType[] getContractTypes() {
		return ContractType.values();
	}
	public ContractDateType[] getContractDateTypes() {
		return ContractDateType.values();
	}
	
	public List<ClientType> getClientTypes() {
		List<ClientType> types2=Arrays.asList(ClientType.values());
		List<ClientType> types=new ArrayList<>();
		for(ClientType ct:types2) {
			if(!ct.equals(ClientType.CHANGE))
				types.add(ct);
		}
		Collections.sort(types , new Comparator<ClientType>() {
            @Override
			public int compare(ClientType o1, ClientType o2) {
            	return Messages.getEnumMessage(o1.toString()).compareTo(Messages.getEnumMessage(o2.toString()));
			}
        });
		return types;
	}
	
	public ClientFrom[] getClientFroms() {
		
		return ClientFrom.values();
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

	public List<ClientFrom> getFilterFroms() {
		return filterFroms;
	}

	public void setFilterFroms(List<ClientFrom> filterFroms) {
		this.filterFroms = filterFroms;
	}



	public List<ClientStatus> getFilterStatus() {
		return filterStatus;
	}



	public void setFilterStatus(List<ClientStatus> filterStatus) {
		this.filterStatus = filterStatus;
	}



	public List<Garant> getFilterGarants() {
		return filterGarants;
	}



	public void setFilterGarants(List<Garant> filterGarants) {
		this.filterGarants = filterGarants;
	}



	public List<Garant> getGarants() {
		if(garants==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			garants=garantService.findByExample(0, 1000, SortEnum.DESCENDING, examples, "id");
		}
		return garants;
	}

	public Loyality[] getLoyalities() {
		return Loyality.values();
	}
	


	public void setGarants(List<Garant> garants) {
		this.garants = garants;
	}



	public List<Boolean> getFilterExtralow() {
		return filterExtralow;
	}



	public void setFilterExtralow(List<Boolean> filterExtralow) {
		this.filterExtralow = filterExtralow;
	}



	public List<ResponsiblePerson> getFilterResponsiblePersons() {
		return filterResponsiblePersons;
	}



	public void setFilterResponsiblePersons(List<ResponsiblePerson> filterResponsiblePersons) {
		this.filterResponsiblePersons = filterResponsiblePersons;
	}



	public List<ResponsiblePerson> getRps() {
		if(rps==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			rps=rpService.findByExample(0, 100000, SortEnum.ASCENDING, examples, "fio");
			if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
				rps=new ArrayList<>();
			}
		}
		return rps;
	}



	public void setRps(List<ResponsiblePerson> rps) {
		this.rps = rps;
	}



	public List<User> getFilterCuratorOrks() {
		return filterCuratorOrks;
	}



	public void setFilterCuratorOrks(List<User> filterCuratorOrks) {
		this.filterCuratorOrks = filterCuratorOrks;
	}



	public List<Company> getFilterDevelopers() {
		return filterDevelopers;
	}



	public void setFilterDevelopers(List<Company> filterDevelopers) {
		this.filterDevelopers = filterDevelopers;
	}



	public List<Company> getDevelopers() {
		if(developers==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("developer",true,InequalityConstants.EQUAL));
			developers=companyService.findByExample(0, 1000, SortEnum.DESCENDING, examples, "id");
			if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
				developers=new ArrayList<>();
			}
		}
		return developers;
	}



	public void setDevelopers(List<Company> developers) {
		this.developers = developers;
	}



	public List<User> getCuratorOrks() {
		if(curatorOrks==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("status",UserStatus.ACTIVE, InequalityConstants.EQUAL));
			examples.add(new FilterExample("role",Role.ORK, InequalityConstants.EQUAL));
			curatorOrks=userService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "fio");
		}
		return curatorOrks;
	}



	public void setCuratorOrks(List<User> curatorOrks) {
		this.curatorOrks = curatorOrks;
	}



	public List<Boolean> getFilterSn() {
		return filterSn;
	}



	public void setFilterSn(List<Boolean> filterSn) {
		this.filterSn = filterSn;
	}



	public String getFilterSnText() {
		return filterSnText;
	}



	public void setFilterSnText(String filterSnText) {
		this.filterSnText = filterSnText;
	}



	public Date getFilterDateFrom2() {
		return filterDateFrom2;
	}

	public PlanType[] getPtypes() {
		return PlanType.values();
	}
	
	public AppartmentType[] getAppartmentTypes() {
		return AppartmentType.values();
	}

	public void setFilterDateFrom2(Date filterDateFrom2) {
		this.filterDateFrom2 = filterDateFrom2;
	}



	public Date getFilterDateTo2() {
		return filterDateTo2;
	}



	public void setFilterDateTo2(Date filterDateTo2) {
		this.filterDateTo2 = filterDateTo2;
	}



	public List<PlanType> getFilterPtypes() {
		return filterPtypes;
	}



	public void setFilterPtypes(List<PlanType> filterPtypes) {
		this.filterPtypes = filterPtypes;
	}



	public List<AppartmentType> getFilterAppartmentTypes() {
		return filterAppartmentTypes;
	}



	public void setFilterAppartmentTypes(List<AppartmentType> filterAppartmentTypes) {
		this.filterAppartmentTypes = filterAppartmentTypes;
	}

	public List<Loyality> getFilterLoyalitites() {
		return filterLoyalitites;
	}

	public void setFilterLoyalitites(List<Loyality> filterLoyalitites) {
		this.filterLoyalitites = filterLoyalitites;
	}



	public List<String> getFilterBlocks() {
		return filterBlocks;
	}



	public void setFilterBlocks(List<String> filterBlocks) {
		this.filterBlocks = filterBlocks;
	}



	public List<Integer> getFilterEntrances() {
		return filterEntrances;
	}



	public void setFilterEntrances(List<Integer> filterEntrances) {
		this.filterEntrances = filterEntrances;
	}



	public List<ContractType> getFilterContractTypes() {
		return filterContractTypes;
	}



	public void setFilterContractTypes(List<ContractType> filterContractTypes) {
		this.filterContractTypes = filterContractTypes;
	}



	public ContractDateType getFilterContractDateType() {
		return filterContractDateType;
	}



	public void setFilterContractDateType(ContractDateType filterContractDateType) {
		this.filterContractDateType = filterContractDateType;
	}

	
}
