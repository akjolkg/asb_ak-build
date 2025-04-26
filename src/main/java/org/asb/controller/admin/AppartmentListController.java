package org.asb.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.data.AppartmentDataModel;
import org.asb.enums.AppartmentType;
import org.asb.enums.ClientType;
import org.asb.enums.Role;
import org.asb.model.Appartment;
import org.asb.model.Construction;
import org.asb.util.web.LoginUtil;
import org.asb.util.web.Messages;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
public class AppartmentListController extends ClassifierController {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private AppartmentDataModel model;
	private AppartmentDataModel offices;
	private AppartmentDataModel garages;
	private Appartment appartment;
	private BigDecimal totalArea;
	private BigDecimal totalAreaDoc;
	private BigDecimal contractSum;
	private BigDecimal payedSum;
	private BigDecimal lefSum;
	private BigDecimal leftSumDoc;
	@Inject
	private LoginUtil loginUtil;
	
	
	
	public AppartmentListController() {}
	
	@PostConstruct
	private void init(){
		restoreState();
	}
	public void filter(Integer k){
		first=0;
		filterData(k);		
	}
	
	public void filterData(Integer k){
		List<FilterExample> filters = new ArrayList<>();
		if(filterCompanies!=null && filterCompanies.size()>0)
			filters.add(new FilterExample("construction.company",filterCompanies,InequalityConstants.IN));
		if(filterDevelopers!=null && filterDevelopers.size()>0)
			filters.add(new FilterExample("construction.developer",filterDevelopers,InequalityConstants.IN));
		if(filterConstructions!=null && filterConstructions.size()>0)
			filters.add(new FilterExample("construction",filterConstructions,InequalityConstants.IN));
		if(filterBlock!=null && filterBlock.length()>0)
			filters.add(new FilterExample("blockNumber",filterBlock+"%",InequalityConstants.LIKE,true));
		if(filterBlocks!=null && filterBlocks.size()>0){
			filters.add(new FilterExample("blockNumber",filterBlocks,InequalityConstants.IN));
		}
		if(filterContractTypes!=null && filterContractTypes.size()>0){
			filters.add(new FilterExample("client.contractType",filterContractTypes,InequalityConstants.IN));
		}
		if(filterEntrances!=null && filterEntrances.size()>0){
			filters.add(new FilterExample("entranceNumber",filterEntrances,InequalityConstants.IN));
		}
		if(filterEntrance!=null)
			filters.add(new FilterExample("entranceNumber",filterEntrance,InequalityConstants.EQUAL));
		if(filterTitle!=null && filterTitle.length()>0)
			filters.add(new FilterExample("title",filterTitle+"%",InequalityConstants.LIKE,true));
		if(filterFlats!=null && filterFlats.size()>0){
			filters.add(new FilterExample("flat",filterFlats,InequalityConstants.IN));
		}
		
		if(filterSn!=null && filterSn.size()==1 && filterSn.contains(true)){
			filters.add(new FilterExample("specNote",0,InequalityConstants.GREATER,false,true));
		}
		if(filterSn!=null && filterSn.size()==1 && filterSn.contains(false)){
			filters.add(new FilterExample(true,"specNote",null,InequalityConstants.IS_NULL_SINGLE,false));
			filters.add(new FilterExample(true,"specNote",0,InequalityConstants.EQUAL,false,true));
			filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		}
		
		if(filterTypes!=null && filterTypes.size()>0) {
			if(filterTypes.contains(ClientType.IN_SALE)){
					filters.add(new FilterExample(true,"client.id",null,InequalityConstants.IS_NULL_SINGLE,false));
					filters.add(new FilterExample(true,"client.type",filterTypes,InequalityConstants.IN,false));					
			}else			
				filters.add(new FilterExample("client.type",filterTypes,InequalityConstants.IN));
		}
		if(filterFroms!=null && filterFroms.size()>0) {
			filters.add(new FilterExample("client.clientFrom",filterFroms,InequalityConstants.IN));
		}
		if(filterRoomQuantities!=null && filterRoomQuantities.size()>0){
			filters.add(new FilterExample("roomQuantity",filterRoomQuantities,InequalityConstants.IN));
		}
		if(filterSnText!=null && filterSnText.length()>0){
			filters.add(new FilterExample("specNote","%"+filterSnText+"%",InequalityConstants.LIKE,true));
			
		}
		
		
		if(filterPtypes!=null && filterPtypes.size()>0) {
			filters.add(new FilterExample("ptype",filterPtypes,InequalityConstants.IN));
		}
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"client.contractNumber","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.fio","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.contacts","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.note","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.whatsappNumber","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.spouseFio","%"+searchString+"%",InequalityConstants.LIKE,true));
		}
		
		
		if(filterKeys!=null && filterKeys.size()>0){
			filters.add(new FilterExample("client.keys",filterKeys,InequalityConstants.IN));
		}
		if(filterSigned!=null && filterSigned.size()>0){
			filters.add(new FilterExample("client.signed",filterSigned,InequalityConstants.IN));
		}
		if(filterExtralow!=null && filterExtralow.size()>0){
			filters.add(new FilterExample("client.extralow",filterExtralow,InequalityConstants.IN));
		}
		if(filterCurators!=null && filterCurators.size()>0){
			filters.add(new FilterExample("client.curator",filterCurators,InequalityConstants.IN));
		}
		if(filterCuratorOrks!=null && filterCuratorOrks.size()>0){
			filters.add(new FilterExample("client.curatorOrk",filterCuratorOrks,InequalityConstants.IN));
		}
		
		if(filterGarants!=null && filterGarants.size()>0){
			filters.add(new FilterExample("client.garant",filterGarants,InequalityConstants.IN));
		}
		if(filterResponsiblePersons!=null && filterResponsiblePersons.size()>0){
			filters.add(new FilterExample("client.fromPerson",filterResponsiblePersons,InequalityConstants.IN));
		}
		
		
		if(filterDateFrom!=null){
			filters.add(new FilterExample("client.dateContract",filterDateFrom,InequalityConstants.GREATER_OR_EQUAL));
		}
		if(filterDateTo!=null){
			Calendar c=Calendar.getInstance();
			c.setTime(filterDateTo);
			c.add(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MILLISECOND, -1);
			
			filters.add(new FilterExample("client.dateContract",c.getTime(),InequalityConstants.LESSER_OR_EQUAL));
		}
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		
		if(loginUtil.getCurrentUser().getRole().equals(Role.RIELTOR)){
			filters.add(new FilterExample("client.id",null,InequalityConstants.IS_NULL_SINGLE));
		}
		
		if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			filters.add(new FilterExample("construction",loginUtil.getCurrentUser().getConstructions(),InequalityConstants.IN));
		}
		
		
		
		if(k==1) {
			filters.add(new FilterExample("type",AppartmentType.APPARTMENT,InequalityConstants.EQUAL));
			model = new AppartmentDataModel(filters, apservice);
		}
		if(k==2) {
			filters.add(new FilterExample("type",AppartmentType.OFFICE,InequalityConstants.EQUAL));
			offices = new AppartmentDataModel(filters, apservice);
		}
		if(k==3) {
			filters.add(new FilterExample("type",AppartmentType.PARKING,InequalityConstants.EQUAL));
			garages = new AppartmentDataModel(filters, apservice);
		}
		
		
		totalArea=apservice.sumByExample("entity.totalArea", filters,new String[] {"client"}); 
		totalAreaDoc=apservice.sumByExample("entity.docTotalArea", filters,new String[] {"client"});
		contractSum=apservice.sumByExample("client.totalSum", filters);
		payedSum=apservice.sumByExample("client.alreadyPayed", filters);
		lefSum=apservice.sumByExample("client.notPayedYet", filters);
		leftSumDoc=apservice.sumByExample("(client.priceForSquare*docTotalArea)-client.alreadyPayed", filters);
		saveState();
		
	}
	
	public void clearFilter(Integer k){
		filterCompanies = null;
		filterDevelopers = null;
		filterConstructions = null;
		filterBlock = null;
		filterEntrance = null;
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
		searchString=null;
		filterFroms=null;
		filterStatus=null;
		filterGarants=null;
		filterResponsiblePersons=null;
		filterSn=null;
		filterSnText=null;
		filterPtypes=null;
		super.clearFilter();
		filter(k);
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

	public AppartmentDataModel getOffices() {
		return offices;
	}

	public void setOffices(AppartmentDataModel offices) {
		this.offices = offices;
	}

	public AppartmentDataModel getGarages() {
		return garages;
	}

	public void setGarages(AppartmentDataModel garages) {
		this.garages = garages;
	}

	public BigDecimal getTotalArea() {
		return totalArea;
	}

	public void setTotalArea(BigDecimal totalArea) {
		this.totalArea = totalArea;
	}

	public BigDecimal getTotalAreaDoc() {
		return totalAreaDoc;
	}

	public void setTotalAreaDoc(BigDecimal totalAreaDoc) {
		this.totalAreaDoc = totalAreaDoc;
	}

	public BigDecimal getPayedSum() {
		return payedSum;
	}

	public void setPayedSum(BigDecimal payedSum) {
		this.payedSum = payedSum;
	}

	public BigDecimal getContractSum() {
		return contractSum;
	}

	public void setContractSum(BigDecimal contractSum) {
		this.contractSum = contractSum;
	}

	public BigDecimal getLefSum() {
		return lefSum;
	}

	public void setLefSum(BigDecimal lefSum) {
		this.lefSum = lefSum;
	}

	public BigDecimal getLeftSumDoc() {
		return leftSumDoc;
	}

	public void setLeftSumDoc(BigDecimal leftSumDoc) {
		this.leftSumDoc = leftSumDoc;
	}
	




}
