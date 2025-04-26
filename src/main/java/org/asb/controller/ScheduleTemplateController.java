package org.asb.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.enums.ClientStatus;
import org.asb.model.Appartment;
import org.asb.model.Client;
import org.asb.model.ClientDocument;
import org.asb.model.Payment;
import org.asb.model.Schedule;
import org.asb.model.ScheduleTemplate;
import org.asb.model.SubSchedule;
import org.asb.service.ClientDocumentService;
import org.asb.service.ClientService;
import org.asb.service.PaymentService;
import org.asb.service.ScheduleService;
import org.asb.service.ScheduleTemplateService;
import org.asb.service.SubScheduleService;
import org.asb.service.UserService;
import org.asb.util.web.LoginUtil;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles= {0,2,4,7,10,9,12})
public class ScheduleTemplateController implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private String sendBackReason;
	private ScheduleTemplate scheduleTemplate;
	private List<ClientDocument> documents;
	private List<SubSchedule> subSchedules;
	
	@EJB
	private ClientService service;
	@EJB
	private ScheduleService scheduleService;
	@EJB
	private ClientDocumentService documentService;
	@EJB
	private ScheduleTemplateService ddService;
	@EJB
	private SubScheduleService ssService;
	@EJB
	private PaymentService paymentService;
	@EJB
	private UserService userService;
	
	
	@Inject
	private Conversation conversation;
	@Inject
	private LoginUtil loginUtil;
	
	
	
	public ScheduleTemplateController() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String sendToDecline(){
		if(!scheduleTemplate.getStatus().equals(ClientStatus.NEW)&&!scheduleTemplate.getStatus().equals(ClientStatus.APPROVED)){
			scheduleTemplate.setSendBackReason(sendBackReason);
			scheduleTemplate.setStatus(ClientStatus.NEW);
			scheduleTemplate=ddService.merge(scheduleTemplate);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Изменения графика успешно отправлено на редактирование!", null) );
		}
		return "list.xhtml";
		
	}
	
	public String sendToFinDirectorApprove(){
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("scheduleTemplate",scheduleTemplate,InequalityConstants.EQUAL));
		subSchedules=ssService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		
		BigDecimal total=BigDecimal.ZERO;
		
		for(SubSchedule ss:subSchedules) {
			total=total.add(ss.getAmountToPay());
			System.out.println("ssssss==="+ss.getAmountToPay());
			if(ss.getAmountToPay().compareTo(BigDecimal.ZERO)<1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Сумма графика оплаты не может быть равна и меньше 0 !", null) );
				return null;
			}
		}
		if(scheduleTemplate.getClient().getTotalSum().compareTo(total)!=0) {
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Сумма графика оплаты = "+total+", Сумма договора = "+scheduleTemplate.getClient().getTotalSum()+ " !", null) );
			return null;
		}
		if(scheduleTemplate.getStatus().equals(ClientStatus.NEW)){
			scheduleTemplate.setSendBackReason(null);
			scheduleTemplate.setStatus(ClientStatus.WAITING_DIRECTOR_APPROVAL);
			scheduleTemplate=ddService.merge(scheduleTemplate);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Изменения графика успешно отправлено на подтверждение!", null) );
		}
		return "list.xhtml";
		
	}
	
	public String deleteRow(SubSchedule ss) {
		ssService.remove(ss);
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("scheduleTemplate",scheduleTemplate,InequalityConstants.EQUAL));
		subSchedules=ssService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		
		return null;
	}
	public String addRow() {
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("scheduleTemplate",scheduleTemplate,InequalityConstants.EQUAL));
		subSchedules=ssService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		SubSchedule ss=new SubSchedule();
		ss.setAmountToPay(BigDecimal.ZERO);
		if(!subSchedules.isEmpty())
			ss.setDatePayment(subSchedules.get(subSchedules.size()-1).getDatePayment());
		else 
			ss.setDatePayment(new Date());
		ss.setScheduleTemplate(scheduleTemplate);
		ssService.persist(ss);
		subSchedules=ssService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		
		return null;
	}
	public String approve(){
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("scheduleTemplate",scheduleTemplate,InequalityConstants.EQUAL));
		subSchedules=ssService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		BigDecimal total=BigDecimal.ZERO;
		
		for(SubSchedule ss:subSchedules) {
			total=total.add(ss.getAmountToPay());
			System.out.println("ssssss==="+ss.getAmountToPay());
			if(ss.getAmountToPay().compareTo(BigDecimal.ZERO)<1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Сумма графика оплаты не может быть равна и меньше 0 !", null) );
				return null;
			}
		}
		if(scheduleTemplate.getClient().getTotalSum().compareTo(total)!=0) {
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Сумма графика оплаты = "+total+", Сумма договора = "+scheduleTemplate.getClient().getTotalSum()+ " !", null) );
			return null;
		}
		
		
		if(scheduleTemplate.getStatus().equals(ClientStatus.WAITING_DIRECTOR_APPROVAL)){
			String history="";
			Client client=scheduleTemplate.getClient();	
				//schedule history
				examples=new ArrayList<>();
				examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
				List<Schedule> schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
				history+="<div class=\"clear\"> </div>";
				history+="<table class=\"form-table f-left m-top m-left\" style=\"width:800px;\">"
						+"<thead><tr><th colspan=\"1\" scope=\"colgroup\"><span class=\"disp-blc m-top m-bottom\" style=\"text-align: center; font-weight: 600;\">График оплаты (История)</span></th></tr>"+
						"</thead><tbody><tr><td><div id=\"table\" class=\"ui-datatable ui-widget\"><div class=\"ui-datatable-tablewrapper\"><table role=\"grid\" class=\"display-table\">"+
						"<thead id=\"table_head\"><tr role=\"row\">"
						+ "<th id=\"table:j_idt98\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"№\" scope=\"col\"><span class=\"ui-column-title\">№</span></th>"
						+ "<th id=\"table:j_idt100\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Дата оплаты\" scope=\"col\"><span class=\"ui-column-title\">Дата оплаты</span></th>"
						+ "<th id=\"table:j_idt102\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Сумма оплаты\" scope=\"col\"><span class=\"ui-column-title\">Сумма оплаты</span></th>"
						+ "<th id=\"table:j_idt104\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Оплачено\" scope=\"col\"><span class=\"ui-column-title\">Оплачено</span></th>"
						+ "<th id=\"table:j_idt106\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Завершён\" scope=\"col\"><span class=\"ui-column-title\">Завершён</span></th>"
						+ "<th id=\"table:j_idt108\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Примечание\" scope=\"col\"><span class=\"ui-column-title\">Примечание</span></th></tr></thead>"
						+ "<tbody id=\"table_data\" class=\"ui-datatable-data ui-widget-content\">";
				Integer k=0;
				for(Schedule s:schedules){
					history+="<tr data-ri=\"0\" class=\"ui-widget-content\" role=\"row\">"
							+ "<td role=\"gridcell\">"+k+"</td>"
							+ "<td role=\"gridcell\">"+s.getDatePayment()+"</td>"
							+ "<td role=\"gridcell\">"+s.getAmountToPay()+"</td>"
							+ "<td role=\"gridcell\">"+s.getAlreadyPayed()+"</td>"
							+ "<td role=\"gridcell\">"+s.getActive()+"</td>"
							+ "<td role=\"gridcell\">"+s.getNote()+"</td></tr>";
				}
				history+="</tbody></table></div></div></td></tr></tbody></table>";
				
				history+="<div class=\"clear\"> </div>";
				
				k=1;
			
					
			
			scheduleTemplate.setHistory(history);
			scheduleTemplate.setStatus(ClientStatus.APPROVED);
			scheduleTemplate=ddService.merge(scheduleTemplate);
			
			for(Schedule s:scheduleService.findByProperty("client", scheduleTemplate.getClient())) {
				scheduleService.remove(s);
			}
			Boolean first=true;
			for(SubSchedule ss:subSchedules) {
				
				Schedule s=new Schedule();
				s.setClient(scheduleTemplate.getClient());
				s.setAmountToPay(ss.getAmountToPay());
				s.setNote(ss.getNote());
				s.setDatePayment(ss.getDatePayment());
				scheduleService.persist(s);
				first=false;
			}
			
			recalculateClient(scheduleTemplate.getClient());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Изменения графика успешно подтверждёно!", null) );
		}
		return "list.xhtml";
	}
	
	public String goToDelete() {
		return "delete.xhtml?faces-redirect=true";
	}
	
	public String delete() {
		
		for(SubSchedule ss:ssService.findByProperty("scheduleTemplate", scheduleTemplate))
			ssService.remove(ss);
		ddService.remove(scheduleTemplate);		
		return "list.xhtml?faces-redirect=true";
	}
	
	
	public String recalculateClient(Client client) {                                            
    	System.out.println("I got hererere");
    	if(client==null) return null;
    	List<FilterExample> examples=new ArrayList<>();
    	examples.add(new FilterExample("id",0,InequalityConstants.NOT_EQUAL));
    	BigDecimal total=BigDecimal.ZERO;
		for(Payment payment:paymentService.findByProperty("client", client)){
			total=total.add(payment.getPaymentAmount());
		}
			if (client.getPriceForSquare()!=null && client.getAppartment().getTotalArea()!=null)
				client.setTotalSum(client.getAppartment().getTotalArea().multiply(client.getPriceForSquare()));
			client.setAlreadyPayed(total);
			Appartment ap=client.getAppartment();
			client.setNotPayedYet(client.getTotalSum().subtract(total));
			System.out.println("before="+client.getTotalSum());
			client.setUpdatedPlannedDate(client.getAppartment().getConstruction().getRealDate());
			client=service.merge(client);
			client=service.findById(client.getId(),false);
			client.setAppartment(ap);
			System.out.println("after="+client.getTotalSum());
			List<FilterExample> examples2=new ArrayList<>();
			examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
			examples2.add(new FilterExample("client",client,InequalityConstants.EQUAL));
			List<Payment> payds=new ArrayList<>();
        	payds.addAll(paymentService.findByExample(0,1000, SortEnum.ASCENDING, examples2, "datePayment"));
        	
			
        	int i=0;
        	BigDecimal amoDouble=BigDecimal.ZERO;
        	
        	if(payds.size()>0){
        		amoDouble=payds.get(0).getPaymentAmount();
        		}
        	
        	List<Schedule> schedules=new ArrayList<>();
        	schedules.addAll(scheduleService.findByExample(0, 100, SortEnum.ASCENDING, examples, "datePayment"));
        	for(Schedule schedule:schedules){
        		schedule.setActive(false);
        		schedule.setAlreadyPayed(BigDecimal.ZERO);
        		schedule.setFactPayment(null);
        		schedule.setLeftAmount(schedule.getAmountToPay());
        		schedule.setPkoNumber(null); 
        		scheduleService.merge(schedule);
        	}
        	
        	int j=0;
        	while (j<schedules.size()){
        		Schedule schedule=schedules.get(j);
			
        		System.out.println("amoudouble="+amoDouble+"   amount topay="+schedule.getAmountToPay()+"  already payed="+schedule.getAlreadyPayed());
        		if(amoDouble.compareTo(schedule.getAmountToPay().subtract(schedule.getAlreadyPayed()))>=0){
        			amoDouble=amoDouble.subtract(schedule.getAmountToPay().subtract(schedule.getAlreadyPayed()));
        			schedule.setAlreadyPayed((schedule.getAmountToPay()));
        			schedule.setActive(true);
        			schedule.setFactPayment(payds.get(i).getDatePayment());
        			schedule.setPkoNumber(payds.get(i).getCheckNumber());
        			scheduleService.merge(schedule);
        			
        		}else if(amoDouble.compareTo(BigDecimal.ZERO)!=0){
        			schedule.setAlreadyPayed(schedule.getAlreadyPayed().add(amoDouble));
        			schedule.setPkoNumber(payds.get(i).getCheckNumber());
        			schedule.setFactPayment(payds.get(i).getDatePayment());
        			schedule.setActive(false);
        			scheduleService.merge(schedule);
        			System.out.println("sch.id="+schedule.getDatePayment()+"   already payed= "+schedule.getAlreadyPayed());
        			amoDouble=BigDecimal.ZERO;
        			if(i<(payds.size()-1)){
        				i++;
        				amoDouble=payds.get(i).getPaymentAmount();
        				j--;
        			}
        			
        			
        		}else {
        			if(i<(payds.size()-1)){
        				i++;
        				amoDouble=payds.get(i).getPaymentAmount();
        				j--;
        			}
        			
        				
        		}
        		
        	scheduleService.merge(schedule);
        		j++;
        	}
        	
    		return null;
    }                                     
	
	
	
	public String cancel() {
		closeConversation();
		return "list.xhtml"+"?faces-redirect=true";
	}
	
	public void onRowSelect(SelectEvent event) throws IOException {
		scheduleTemplate=(ScheduleTemplate) event.getObject();
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("scheduleTemplate",scheduleTemplate,InequalityConstants.EQUAL));
		subSchedules=ssService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/scheduleTemplate/card.xhtml?cid="+conversation.getId());
	}
	
	public void onRowEdit(RowEditEvent event) {
		ssService.merge((SubSchedule)event.getObject());
    }
	
	
	
	
	public String getSendBackReason() {
		return sendBackReason;
	}

	public void setSendBackReason(String sendBackReason) {
		this.sendBackReason = sendBackReason;
	}

	public ScheduleTemplate getScheduleTemplate() {
		return scheduleTemplate;
	}

	public void setScheduleTemplate(ScheduleTemplate scheduleTemplate) {
		this.scheduleTemplate = scheduleTemplate;
	}

	public List<ClientDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<ClientDocument> documents) {
		this.documents = documents;
	}

	public List<SubSchedule> getSubSchedules() {
		return subSchedules;
	}

	public void setSubSchedules(List<SubSchedule> subSchedules) {
		this.subSchedules = subSchedules;
	}
	
}
