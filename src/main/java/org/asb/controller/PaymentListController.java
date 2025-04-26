package org.asb.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.asb.controller.admin.ClassifierController;
import org.asb.data.PaymentModel;
import org.asb.enums.AppartmentType;
import org.asb.enums.ClientFrom;
import org.asb.enums.PaymentType;
import org.asb.enums.Role;
import org.asb.model.Company;
import org.asb.model.Payment;
import org.asb.model.User;
import org.asb.service.CompanyService;
import org.asb.service.PaymentService;
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
public class PaymentListController extends ClassifierController{
	
	private static final long serialVersionUID = 8475958315897562353L;
	private PaymentModel model; 
	private Payment payment;
	private BigDecimal total;
	private List<User> kassirs;
	private List<User> filterKassirs;
	private BigDecimal amountFrom;
	private BigDecimal amountTo;
	private List<PaymentType> filterPaymentEnumTypes;
	
	
	
	@EJB
	private PaymentService service;
	private List<Boolean> filterPaymentTypes;
	
	public PaymentListController() {}
	
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
		
		if(amountFrom!=null) {
			filters.add(new FilterExample("paymentAmount",amountFrom,InequalityConstants.GREATER_OR_EQUAL));
		}
		if(amountTo!=null) {
			filters.add(new FilterExample("paymentAmount",amountTo,InequalityConstants.LESSER_OR_EQUAL));
		}
		if(filterAppartmentTypes!=null && filterAppartmentTypes.size()>0)
			filters.add(new FilterExample("client.appartment.type",filterAppartmentTypes,InequalityConstants.IN));
		if(filterPaymentEnumTypes!=null && filterPaymentEnumTypes.size()>0)
			filters.add(new FilterExample("paymentType",filterPaymentEnumTypes,InequalityConstants.IN));
		
		if(filterCompanies!=null && filterCompanies.size()>0)
			filters.add(new FilterExample("client.appartment.construction.company",filterCompanies,InequalityConstants.IN));
		if(filterDevelopers!=null && filterDevelopers.size()>0)
			filters.add(new FilterExample("client.appartment.construction.developer",filterDevelopers,InequalityConstants.IN));
		if(filterConstructions!=null && filterConstructions.size()>0)
			filters.add(new FilterExample("client.appartment.construction",filterConstructions,InequalityConstants.IN));
		if(filterBlock!=null && filterBlock.length()>0)
			filters.add(new FilterExample("client.appartment.blockNumber",filterBlock+"%",InequalityConstants.LIKE,true));
		if(filterBlocks!=null && filterBlocks.size()>0){
			filters.add(new FilterExample("client.appartment.blockNumber",filterBlocks,InequalityConstants.IN));
		}
		if(filterEntrances!=null && filterEntrances.size()>0){
			filters.add(new FilterExample("client.appartment.entranceNumber",filterEntrances,InequalityConstants.IN));
		}
		
		if(filterEntrance!=null)
			filters.add(new FilterExample("client.appartment.entranceNumber",filterEntrance,InequalityConstants.EQUAL));
		if(filterTitle!=null && filterTitle.length()>0)
			filters.add(new FilterExample("client.appartment.title",filterTitle+"%",InequalityConstants.LIKE,true));
		if(filterFlats!=null && filterFlats.size()>0){
			filters.add(new FilterExample("client.appartment.flat",filterFlats,InequalityConstants.IN));
		}
		if(filterTypes!=null && filterTypes.size()>0) {
			filters.add(new FilterExample("client.type",filterTypes,InequalityConstants.IN));
		}
		if(filterFroms!=null && filterFroms.size()>0) {
			filters.add(new FilterExample("client.clientFrom",filterFroms,InequalityConstants.IN));
		}
		if(filterRoomQuantities!=null && filterRoomQuantities.size()>0){
			filters.add(new FilterExample("client.appartment.roomQuantity",filterRoomQuantities,InequalityConstants.IN));
		}
		if(searchString!=null && searchString.length()>0){
			filters.add(new FilterExample(true,"client.contractNumber","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.fio","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.contacts","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.note","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true,"client.whatsappNumber","%"+searchString+"%",InequalityConstants.LIKE,true));
			filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		}
		if(filterKeys!=null && filterKeys.size()>0){
			filters.add(new FilterExample("client.keys",filterKeys,InequalityConstants.IN));
		}
		if(filterExtralow!=null && filterExtralow.size()>0){
			filters.add(new FilterExample("client.extralow",filterExtralow,InequalityConstants.IN));
		}
		if(filterGarants!=null && filterGarants.size()>0){
			filters.add(new FilterExample("client.garant",filterGarants,InequalityConstants.IN));
		}
		
		if(filterPaymentTypes!=null && filterPaymentTypes.size()>0){
			filters.add(new FilterExample("firstPayment",filterPaymentTypes,InequalityConstants.IN));
		}
		
		if(filterSigned!=null && filterSigned.size()>0){
			filters.add(new FilterExample("client.signed",filterSigned,InequalityConstants.IN));
		}
		if(filterCurators!=null && filterCurators.size()>0){
			filters.add(new FilterExample("client.curator",filterCurators,InequalityConstants.IN,false));
		}
		if(filterCuratorOrks!=null && filterCuratorOrks.size()>0){
			filters.add(new FilterExample("client.curatorOrk",filterCuratorOrks,InequalityConstants.IN,false));
		}
		if(filterKassirs!=null && filterKassirs.size()>0){
			filters.add(new FilterExample("user",filterKassirs,InequalityConstants.IN,false));
		}
		
		if(filterAppartmentTypes!=null && filterAppartmentTypes.size()>0){
			filters.add(new FilterExample("client.appartment.type",filterAppartmentTypes,InequalityConstants.IN));
		}
		
		if(filterDateFrom!=null)
			filters.add(new FilterExample("datePayment",filterDateFrom,InequalityConstants.GREATER_OR_EQUAL));
		if(filterDateTo!=null){
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(filterDateTo);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			filters.add(new FilterExample("datePayment",calendar.getTime(),InequalityConstants.LESSER));
		}
		
		if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			filters.add(new FilterExample("client.appartment.construction",loginUtil.getCurrentUser().getConstructions(),InequalityConstants.IN));
		}
		
		
		if(loginUtil.getCurrentUser().getRole().equals(Role.JUNIOR)||loginUtil.getCurrentUser().getRole().equals(Role.FILIAL))
			filters.add(new FilterExample("client.curator",loginUtil.getCurrentUser(), InequalityConstants.EQUAL));
		
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		
		total=service.sumByExample("paymentAmount",filters);
		model = new PaymentModel(filters, service);
		saveState();
		
	}
	
	
	public void clearFilter(){
		filterKassirs=null;
		filterPaymentTypes=null;
		filterAppartmentTypes=null;
		filterPaymentEnumTypes=null;
		amountFrom=null;
		amountTo=null;
		
		super.clearFilter();
		filter();
	}
	public void saveState() {
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("amountFrom", amountFrom);	
		session.setAttribute("amountTo", amountTo);	
		
		session.setAttribute("filterKassirs", filterKassirs);	
		session.setAttribute("filterPaymentEnumTypes", filterPaymentEnumTypes);	
		
		session.setAttribute("filterPaymentTypes", filterPaymentTypes);	
		session.setAttribute("filterAppartmentTypes",filterAppartmentTypes);
		super.saveState();
		
	}
	
	public void restoreState() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		amountFrom = (BigDecimal) session.getAttribute("amountFrom");
		amountTo = (BigDecimal) session.getAttribute("amountTo");
		
		filterKassirs = (List<User>) session.getAttribute("filterKassirs");
		filterPaymentEnumTypes = (List<PaymentType>) session.getAttribute("filterPaymentEnumTypes");
		filterPaymentTypes= (List<Boolean>)session.getAttribute("filterPaymentTypes");
		filterAppartmentTypes= (List<AppartmentType>)session.getAttribute("filterAppartmentTypes");
		super.restoreState();
	}
	
	
	public PaymentModel getModel() {
		return model;
	}
	
	public void setModel(PaymentModel model) {
		this.model = model;
	}
	
	public Payment getPayment() {
		return payment;
	}
	
	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public List<Boolean> getFilterPaymentTypes() {
		return filterPaymentTypes;
	}

	public void setFilterPaymentTypes(List<Boolean> filterPaymentTypes) {
		this.filterPaymentTypes = filterPaymentTypes;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<User> getKassirs() {
		if(kassirs==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			examples.add(new FilterExample(true,"role",Role.KASSA, InequalityConstants.EQUAL,false));
			examples.add(new FilterExample(true,"role",Role.FIN_DIRECTOR, InequalityConstants.EQUAL,false));			
			examples.add(new FilterExample(true,"role",Role.SUB_FIN_DIRECTOR, InequalityConstants.EQUAL,false));
			examples.add(new FilterExample(true,"role",Role.KASSA_FILIAL, InequalityConstants.EQUAL,false));
			kassirs=userService.findByExample(0, 100, SortEnum.ASCENDING, examples, "fio");
		}		
		return kassirs;
	}
	
	public PaymentType[] getPaymentTypes() {
    	return PaymentType.values();
    }

	public AppartmentType[] getAppartmentTypes() {
		
		return AppartmentType.values();
	}
	
	public void setKassirs(List<User> kassirs) {
		this.kassirs = kassirs;
	}

	public List<User> getFilterKassirs() {
		return filterKassirs;
	}

	public void setFilterKassirs(List<User> filterKassirs) {
		this.filterKassirs = filterKassirs;
	}


	public BigDecimal getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(BigDecimal amountFrom) {
		this.amountFrom = amountFrom;
	}

	public BigDecimal getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(BigDecimal amountTo) {
		this.amountTo = amountTo;
	}

	public List<PaymentType> getFilterPaymentEnumTypes() {
		return filterPaymentEnumTypes;
	}

	public void setFilterPaymentEnumTypes(List<PaymentType> filterPaymentEnumTypes) {
		this.filterPaymentEnumTypes = filterPaymentEnumTypes;
	}
}
