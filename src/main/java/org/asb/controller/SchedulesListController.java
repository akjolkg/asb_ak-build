package org.asb.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.asb.controller.admin.ClassifierController;
import org.asb.data.ScheduleModel;
import org.asb.enums.ClientFrom;
import org.asb.enums.ClientType;
import org.asb.enums.Role;
import org.asb.model.Company;
import org.asb.model.Construction;
import org.asb.model.User;
import org.asb.service.ScheduleService;
import org.asb.util.web.LoginUtil;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;

import com.fasterxml.classmate.Filter;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
@RolesAllowed(roles=0)
public class SchedulesListController extends ClassifierController {
	
	private static final long serialVersionUID = 8475958315897562353L;
	private ScheduleModel model; 

	private Date dateFrom;
	private Date dateTo;
	private BigDecimal total;
	private List<Boolean> filterPaymentTypes;
	
	@EJB
	private ScheduleService service;
	
	
	public SchedulesListController() {}
	
	@PostConstruct
	private void init(){
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		filterPaymentTypes=(List<Boolean>) session.getAttribute("filterPaymentTypes");		
		dateFrom=(Date) session.getAttribute("filterDateFromSchedules");	
		dateTo=(Date) session.getAttribute("filterDateToSchedules");	
		
		if(dateFrom==null)
			dateFrom=calendar.getTime();
		if(dateTo==null)
			dateTo=calendar.getTime();
		
	
		restoreState();
		filterData();
	}
	
	public void filter(){
		first=0;
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("filterPaymentTypes",filterPaymentTypes);	
		session.setAttribute("filterDateFromSchedules",dateFrom);	
		session.setAttribute("filterDateToSchedules",dateTo);	
		filterData();
		
	}
	
	public void filterData(){
		List<FilterExample> filters = new ArrayList<>();
		
		if(filterCompanies!=null && filterCompanies.size()>0)
			filters.add(new FilterExample("client.appartment.construction.company",filterCompanies,InequalityConstants.IN));
		if(filterDevelopers!=null && filterDevelopers.size()>0)
			filters.add(new FilterExample("client.appartment.construction.developer",filterDevelopers,InequalityConstants.IN));
		if(filterConstructions!=null && filterConstructions.size()>0)
			filters.add(new FilterExample("client.appartment.construction",filterConstructions,InequalityConstants.IN));
		if(filterBlock!=null && filterBlock.length()>0)
			filters.add(new FilterExample("client.appartment.blockNumber",filterBlock+"%",InequalityConstants.LIKE,true));
		if(filterEntrance!=null)
			filters.add(new FilterExample("client.appartment.entranceNumber",filterEntrance,InequalityConstants.EQUAL));
		if(filterBlocks!=null && filterBlocks.size()>0){
			filters.add(new FilterExample("client.appartment.blockNumber",filterBlocks,InequalityConstants.IN));
		}
		if(filterEntrances!=null && filterEntrances.size()>0){
			filters.add(new FilterExample("client.appartment.entranceNumber",filterEntrances,InequalityConstants.IN));
		}
		if(filterTitle!=null && filterTitle.length()>0)
			filters.add(new FilterExample("client.appartment.title",filterTitle+"%",InequalityConstants.LIKE,true));
		if(filterFlats!=null && filterFlats.size()>0){
			filters.add(new FilterExample("client.appartment.flat",filterFlats,InequalityConstants.IN));
		}
		if(filterAppartmentTypes!=null && filterAppartmentTypes.size()>0){
			filters.add(new FilterExample("client.appartment.type",filterAppartmentTypes,InequalityConstants.IN));
		}
		if(filterStatus!=null && filterStatus.size()>0){
			filters.add(new FilterExample("client.status",filterStatus,InequalityConstants.IN));
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
		
		if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			filters.add(new FilterExample("client.appartment.construction",loginUtil.getCurrentUser().getConstructions(),InequalityConstants.IN));
		}
		
		
		if(loginUtil.getCurrentUser().getRole().equals(Role.JUNIOR)||loginUtil.getCurrentUser().getRole().equals(Role.FILIAL))
			filters.add(new FilterExample("client.curator",loginUtil.getCurrentUser(), InequalityConstants.EQUAL));
		
		filters.add(new FilterExample("active",false,InequalityConstants.EQUAL));
		if(dateFrom!=null)
			filters.add(new FilterExample("datePayment",dateFrom,InequalityConstants.GREATER_OR_EQUAL));
		if(dateTo!=null)
			filters.add(new FilterExample("datePayment",dateTo,InequalityConstants.LESSER_OR_EQUAL));
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		
		if(filterPaymentTypes!=null && filterPaymentTypes.size()>0){
			if(filterPaymentTypes.size()<2){
				if(filterPaymentTypes.get(0).equals(true))
					filters.add(new FilterExample("note","%Первый взнос%",InequalityConstants.LIKE,true));
				else
					filters.add(new FilterExample(true,"note","%Первый взнос%",InequalityConstants.NOT_LIKE,true));
				filters.add(new FilterExample(true,"note",null,InequalityConstants.IS_NULL_SINGLE,false));
			}
		}
		filters.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
		
		//posle inventarizacii proverka		
		filters.add(new FilterExample(true,"client.appartment.docTotalArea*entity.client.priceForSquare>entity.client.alreadyPayed or entity.id",null,InequalityConstants.IS_NULL_SINGLE,false));
		filters.add(new FilterExample(true,"client.appartment.docTotalArea",null,InequalityConstants.IS_NULL_SINGLE,false));
		
		total=service.sumByExample("amountToPay-alreadyPayed",filters);
		
		model = new ScheduleModel(filters, service);
		saveState();
	}
	public void clearFilter(){
		dateFrom=null;
		dateTo=null;
		filterPaymentTypes=null;
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("filterPaymentTypes",null);	
		session.setAttribute("filterDateFromSchedules",null);	
		session.setAttribute("filterDateToSchedules",null);	
		
		super.clearFilter();
		filter();
	}
	
	public ScheduleModel getModel() {
		return model;
	}
	
	public void setModel(ScheduleModel model) {
		this.model = model;
	}
	
	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<Boolean> getFilterPaymentTypes() {
		return filterPaymentTypes;
	}

	public void setFilterPaymentTypes(List<Boolean> filterPaymentTypes) {
		this.filterPaymentTypes = filterPaymentTypes;
	}
	
}
