package org.asb.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.controller.admin.ClassifierController;
import org.asb.data.ClientDataModel;
import org.asb.enums.ContractDateType;
import org.asb.enums.Loyality;
import org.asb.model.Client;
import org.asb.service.ClientService;
import org.bouncycastle.crypto.engines.CamelliaWrapEngine;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
@RolesAllowed(roles=0)
public class ClientListController extends ClassifierController {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private ClientDataModel model; 
	private Client client;
	private Date filterDate2From;
	private Date filterDate2To;
	
	
	
	@EJB
	private ClientService service;
	
	

	public ClientListController() {}
	
	@PostConstruct
	private void init(){
		
		restoreState();
		filterData();
	}
	
	public void filter(){
		first=0;
		filterData();		
	}
	
	public void filterData(){
		
		List<FilterExample> filters = new ArrayList<>();
		if(filterCompanies!=null && filterCompanies.size()>0)
			filters.add(new FilterExample("appartment.construction.company",filterCompanies,InequalityConstants.IN));
		if(filterDevelopers!=null && filterDevelopers.size()>0)
			filters.add(new FilterExample("appartment.construction.developer",filterDevelopers,InequalityConstants.IN));
		if(filterConstructions!=null && filterConstructions.size()>0)
			filters.add(new FilterExample("appartment.construction",filterConstructions,InequalityConstants.IN));
		if(filterBlock!=null && filterBlock.length()>0)
			filters.add(new FilterExample("appartment.blockNumber",filterBlock+"%",InequalityConstants.LIKE,true));
		if(filterEntrance!=null)
			filters.add(new FilterExample("appartment.entranceNumber",filterEntrance,InequalityConstants.EQUAL));
		if(filterTitle!=null && filterTitle.length()>0)
			filters.add(new FilterExample("appartment.title",filterTitle+"%",InequalityConstants.LIKE,true));
		if(filterFlats!=null && filterFlats.size()>0){
			filters.add(new FilterExample("appartment.flat",filterFlats,InequalityConstants.IN));
		}
		if(filterBlocks!=null && filterBlocks.size()>0){
			filters.add(new FilterExample("appartment.blockNumber",filterBlocks,InequalityConstants.IN));
		}
		if(filterEntrances!=null && filterEntrances.size()>0){
			filters.add(new FilterExample("appartment.entranceNumber",filterEntrances,InequalityConstants.IN));
		}
		if(filterTypes!=null && filterTypes.size()>0) {
			filters.add(new FilterExample("type",filterTypes,InequalityConstants.IN));
		}
		if(filterPtypes!=null && filterPtypes.size()>0) {
			filters.add(new FilterExample("appartment.ptype",filterPtypes,InequalityConstants.IN));
		}
		if(filterLoyalitites!=null && filterLoyalitites.size()>0) {
			filters.add(new FilterExample("loyality",filterLoyalitites,InequalityConstants.IN));
		}
		
		if(filterAppartmentTypes!=null && filterAppartmentTypes.size()>0){
			filters.add(new FilterExample("appartment.type",filterAppartmentTypes,InequalityConstants.IN));
		}
		if(filterContractTypes!=null && filterContractTypes.size()>0){
			filters.add(new FilterExample("contractType",filterContractTypes,InequalityConstants.IN));
		}
		
		if(filterFroms!=null && filterFroms.size()>0) {
			filters.add(new FilterExample("clientFrom",filterFroms,InequalityConstants.IN));
		}
		if(filterRoomQuantities!=null && filterRoomQuantities.size()>0){
			filters.add(new FilterExample("appartment.roomQuantity",filterRoomQuantities,InequalityConstants.IN));
		}
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"contractNumber","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"fio","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"uptFio","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"contacts","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"note","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"whatsappNumber","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"spouseFio","%"+searchString+"%",InequalityConstants.LIKE,true));
			
			
			
			
			if(searchString.toUpperCase().equals("УПТ")){
				filters.add(new FilterExample(true,"uptStatus",true,InequalityConstants.EQUAL,false));
			}
		}
		
		
		if(filterSnText!=null && filterSnText.length()>0){
			filters.add(new FilterExample("appartment.specNote","%"+filterSnText+"%",InequalityConstants.LIKE,true));
			
		}
		if(filterKeys!=null && filterKeys.size()>0){
			filters.add(new FilterExample("keys",filterKeys,InequalityConstants.IN));
		}
		
		if(filterSn!=null && filterSn.size()==1 && filterSn.contains(true)){
			filters.add(new FilterExample("appartment.specNote",0,InequalityConstants.GREATER,false,true));
		}
		if(filterSn!=null && filterSn.size()==1 && filterSn.contains(false)){
			filters.add(new FilterExample(true,"appartment.specNote",null,InequalityConstants.IS_NULL_SINGLE,false));
			filters.add(new FilterExample(true,"appartment.specNote",0,InequalityConstants.EQUAL,false,true));
			filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		}
		
		if(filterExtralow!=null && filterExtralow.size()>0){
			filters.add(new FilterExample("extralow",filterExtralow,InequalityConstants.IN));
		}
		if(filterSigned!=null && filterSigned.size()>0){
			filters.add(new FilterExample("signed",filterSigned,InequalityConstants.IN));
		}
		if(filterCurators!=null && filterCurators.size()>0){
			filters.add(new FilterExample("curator",filterCurators,InequalityConstants.IN));
		}
		if(filterCuratorOrks!=null && filterCuratorOrks.size()>0){
			filters.add(new FilterExample("curatorOrk",filterCuratorOrks,InequalityConstants.IN));
		}
		
		if(filterContractDateType==null) {
		
			if(filterDateFrom!=null){
				filters.add(new FilterExample("dateContract",filterDateFrom,InequalityConstants.GREATER_OR_EQUAL));
			}
			if(filterDateTo!=null){
				Calendar c=Calendar.getInstance();
				c.setTime(filterDateTo);
				c.add(Calendar.DAY_OF_MONTH, 1);
				c.add(Calendar.MILLISECOND, -1);
				filters.add(new FilterExample("dateContract",c.getTime(),InequalityConstants.LESSER_OR_EQUAL));
			}
		}else {
			if(filterContractDateType.equals(ContractDateType.INITIAL_CONTRACT)) {
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
			}else {
				if(filterDateFrom!=null){
					filters.add(new FilterExample("updatedPlannedDate",filterDateFrom,InequalityConstants.GREATER_OR_EQUAL));
				}
				if(filterDateTo!=null){
					Calendar c=Calendar.getInstance();
					c.setTime(filterDateTo);
					c.add(Calendar.DAY_OF_MONTH, 1);
					c.add(Calendar.MILLISECOND, -1);
					filters.add(new FilterExample("updatedPlannedDate",c.getTime(),InequalityConstants.LESSER_OR_EQUAL));
				}
			}
		}
		
		if(filterDate2From!=null){
			filters.add(new FilterExample("dateLegalSign",filterDate2From,InequalityConstants.GREATER_OR_EQUAL));
		}
		if(filterDate2To!=null){
			filters.add(new FilterExample("dateLegalSign",filterDate2To,InequalityConstants.LESSER_OR_EQUAL));
		}
		
		if(filterStatus!=null && filterStatus.size()>0){
			filters.add(new FilterExample("status",filterStatus,InequalityConstants.IN));
		}
		if(filterGarants!=null && filterGarants.size()>0){
			filters.add(new FilterExample("garant",filterGarants,InequalityConstants.IN));
		}
		
		if(filterResponsiblePersons!=null && filterResponsiblePersons.size()>0){
			filters.add(new FilterExample("fromPerson",filterResponsiblePersons,InequalityConstants.IN));
		}
		
		if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			filters.add(new FilterExample("appartment.construction",loginUtil.getCurrentUser().getConstructions(),InequalityConstants.IN));
		}
		
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		model = new ClientDataModel(filters, service);
		saveState();
		
	}
	
	
	
	public void clearFilter(){
		filterDate2From=null;
		filterDate2To=null;
		super.clearFilter();
		filter();
	}
	
	
	public ClientDataModel getModel() {
		return model;
	}
	
	public void setModel(ClientDataModel model) {
		this.model = model;
	}
	
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}

	public Date getFilterDate2From() {
		return filterDate2From;
	}

	public void setFilterDate2From(Date filterDate2From) {
		this.filterDate2From = filterDate2From;
	}

	public Date getFilterDate2To() {
		return filterDate2To;
	}

	public void setFilterDate2To(Date filterDate2To) {
		this.filterDate2To = filterDate2To;
	}

	public ContractDateType getFilterContractDateType() {
		return filterContractDateType;
	}

	public void setFilterContractDateType(ContractDateType filterContractDateType) {
		this.filterContractDateType = filterContractDateType;
	}

	
	
	
	
}
