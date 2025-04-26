package org.asb.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.asb.controller.admin.ClassifierController;
import org.asb.data.DebtorsDTO;
import org.asb.data.DebtorsDTOModel;
import org.asb.data.PaymentModel;
import org.asb.enums.Role;
import org.asb.model.AbstractEntity;
import org.asb.model.Company;
import org.asb.model.Construction;
import org.asb.model.Payment;
import org.asb.model.User;
import org.asb.service.CompanyService;
import org.asb.service.PaymentService;
import org.asb.service.ScheduleService;
import org.asb.util.Serializer;
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
public class DebtorsListController extends ClassifierController{
	
	private static final long serialVersionUID = 8475958315897562353L;
	private DebtorsDTOModel model; 
	private DebtorsDTO dto;
	
	private List<String> expressionsList;
	private Map<Integer, Object> parametersMap;
	
	@Inject
    private Serializer serializer;
	@EJB
	private ScheduleService service;
	public DebtorsListController() {}
	
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
		Integer parameterCount = 1;
		expressionsList = new ArrayList<String>(0);
		parametersMap = new HashMap<Integer, Object>();
		expressionsList.add(" c.id <> ?"+parameterCount.toString());
		parametersMap.put(parameterCount, 0);			
		parameterCount++;
		
		
		if(filterCompanies!=null && filterCompanies.size()>0){
			expressionsList.add(" con.company_id in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, getIds(filterCompanies.toArray()));			
			parameterCount++;
		
		}
		if(filterDevelopers!=null && filterDevelopers.size()>0){
			expressionsList.add(" con.developer_id in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, getIds(filterDevelopers.toArray()));			
			parameterCount++;
		
		}
		if(filterConstructions!=null && filterConstructions.size()>0){
			expressionsList.add(" con.id in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, getIds(filterConstructions.toArray()));			
			parameterCount++;
			
		}
		if(filterStatus!=null && filterStatus.size()>0){
			expressionsList.add(" c.status in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, getIds(filterStatus.toArray()));			
			parameterCount++;
			
		}
		
		
		if(loginUtil.getCurrentUser().getConstructions()!=null && !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			List<Integer> ids=new ArrayList<>();
			for (Construction c:loginUtil.getCurrentUser().getConstructions())
				ids.add(c.getId());
			
			expressionsList.add(" con.id in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, ids);			
			parameterCount++;
		}
			
		if(filterBlock!=null && filterBlock.length()>0){
			expressionsList.add(" ap.block_number like ?"+parameterCount.toString());
			parametersMap.put(parameterCount, "%"+filterBlock+"%");			
			parameterCount++;
			
		}
		
		if(filterBlocks!=null && filterBlocks.size()>0){
			expressionsList.add(" ap.block_number  in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount,filterBlocks);			
			parameterCount++;
		}
		if(filterEntrances!=null && filterEntrances.size()>0){
			expressionsList.add(" ap.entrance_number in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, filterEntrances);			
			parameterCount++;
		}
		
		if(filterEntrance!=null){
			expressionsList.add(" ap.entrance_number = ?"+parameterCount.toString()+"");
			parametersMap.put(parameterCount, filterEntrance);			
			parameterCount++;		
			
		}
		if(filterTitle!=null && filterTitle.length()>0){
					
			expressionsList.add(" ap.title ilike ?"+parameterCount.toString());
			parametersMap.put(parameterCount, "%"+filterTitle+"%");			
			parameterCount++;	
			
		}		
		if(filterFlats!=null && filterFlats.size()>0){
			expressionsList.add(" ap.flat in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, filterFlats);			
			parameterCount++;		
			
		}
		if(filterTypes!=null && filterTypes.size()>0) {
			expressionsList.add(" c.type in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, getIds(filterTypes.toArray()));			
			parameterCount++;
			
			
			
		}
		
		if(filterAppartmentTypes!=null && filterAppartmentTypes.size()>0){
			
			expressionsList.add(" ap.type in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, getIds(filterAppartmentTypes.toArray()));			
			parameterCount++;
		}
		
		if(filterFroms!=null && filterFroms.size()>0) {
			expressionsList.add(" c.client_from in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, getIds(filterFroms.toArray()));			
			parameterCount++;
			
			
			
		}
		if(filterRoomQuantities!=null && filterRoomQuantities.size()>0){
			expressionsList.add(" ap.room_quantity in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, filterRoomQuantities);			
			parameterCount++;	
			
			
			
		}
		if(searchString!=null && searchString.length()>0){
			
			expressionsList.add(" (c.fio ilike ?"+parameterCount.toString()+" or c.contacts ilike ?"+
					parameterCount.toString()+" or c.note ilike ?"+
					parameterCount.toString()+" or c.contract_number ilike ?"+
					parameterCount.toString()+" or c.whatsapp_number ilike ?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, "%"+searchString+"%");			
			parameterCount++;	
			
			
			
			
			
		}
		if(filterKeys!=null && filterKeys.size()>0){
			expressionsList.add(" c.got_keys in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, filterKeys);			
			parameterCount++;	
			
			
			
		}
		if(filterExtralow!=null && filterExtralow.size()>0){
			expressionsList.add(" c.extralow in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, filterExtralow);			
			parameterCount++;	
			
			
			
		}
		if(filterSigned!=null && filterSigned.size()>0){
			//filters.add(new FilterExample("client.signed",filterSigned,InequalityConstants.IN));
			expressionsList.add(" c.signed in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, filterSigned);			
			parameterCount++;	
			
			
		}
		if(filterCurators!=null && filterCurators.size()>0){
			//filters.add(new FilterExample("client.curator",filterCurators,InequalityConstants.IN));
			expressionsList.add(" c.curator_id in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, filterCurators);			
			parameterCount++;
		}
		if(filterCuratorOrks!=null && filterCuratorOrks.size()>0){
			//filters.add(new FilterExample("client.curator",filterCurators,InequalityConstants.IN));
			expressionsList.add(" c.curator_ork_id in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, filterCuratorOrks);			
			parameterCount++;
		}
		
		if(filterGarants!=null && filterGarants.size()>0){
			//filters.add(new FilterExample("client.curator",filterCurators,InequalityConstants.IN));
			expressionsList.add(" c.garant_id in (?"+parameterCount.toString()+")");
			parametersMap.put(parameterCount, filterGarants);			
			parameterCount++;
		}
		
		if(filterDateFrom!=null){
			//filters.add(new FilterExample("datePayment",filterDateFrom,InequalityConstants.GREATER_OR_EQUAL));
			expressionsList.add(" s.date_payment >=?"+parameterCount.toString());
			parametersMap.put(parameterCount, filterDateFrom);			
			parameterCount++;	
		
		}
		if(filterDateTo!=null){
			//filters.add(new FilterExample("datePayment",filterDateTo,InequalityConstants.LESSER_OR_EQUAL));
			expressionsList.add(" s.date_payment <=?"+parameterCount.toString());
			parametersMap.put(parameterCount, filterDateTo);			
			parameterCount++;	
			
		}
		
		if(loginUtil.getCurrentUser().getRole().equals(Role.JUNIOR)||loginUtil.getCurrentUser().getRole().equals(Role.FILIAL)){
			expressionsList.add(" cu.id =?"+parameterCount.toString());
			parametersMap.put(parameterCount, loginUtil.getCurrentUser().getId());			
			parameterCount++;	
		}
		model = new DebtorsDTOModel(expressionsList, parametersMap, serializer,service);
		saveState();
		
	}
	
	public void clearFilter(){
		super.clearFilter();
		filter();
	}
	
	
	private List<Integer> getIds(Object[] objects){
		List<Integer> ids=new ArrayList<>();
		for(Object object:objects){
			if(object instanceof AbstractEntity) 
				ids.add((Integer)((AbstractEntity)object).getId());
		
			if(object instanceof Enum)
				ids.add(((Enum)object).ordinal());
			
		}
		System.out.println("-----"+ids);
		return ids;
	}
	
	
	public DebtorsDTOModel getModel() {
		return model;
	}
	
	public void setModel(DebtorsDTOModel model) {
		this.model = model;
	}
	
	public DebtorsDTO getDto() {
		return dto;
	}

	public void setDto(DebtorsDTO dto) {
		this.dto = dto;
	} 

}
