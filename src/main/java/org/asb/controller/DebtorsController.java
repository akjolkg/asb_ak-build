package org.asb.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.enums.ClientType;
import org.asb.model.Client;
import org.asb.model.Payment;
import org.asb.model.Schedule;
import org.asb.service.ClientService;
import org.asb.service.PaymentService;
import org.asb.service.ScheduleService;
import org.primefaces.event.SelectEvent;


/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles=0)
public class DebtorsController implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private Client client;
	private Payment payment;
	private String comeBackUrl;
	@EJB
	private PaymentService service;
	@EJB
	private ClientService clientService;
	@EJB
	private ScheduleService scheduleService;
	@Inject
	private Conversation conversation;
	
	public DebtorsController() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
    
    public String addPayment(Client client, String comeBackUrl ) {
		this.comeBackUrl=comeBackUrl;
		this.client=client;
		payment=new Payment();
		payment.setClient(client);
		payment.setDatePayment(new Date());
		List<FilterExample> examples=new ArrayList<>();
	    examples.add(new FilterExample("client.appartment.construction",client.getAppartment().getConstruction(),InequalityConstants.EQUAL));
	    Integer max=service.findMaxByPropertyWithExamples("checkNumber", examples);
	        if(max==null) max=0;
	        max=max+1;
	    payment.setCheckNumber(max);
		return "/view/debtors/form.xhtml?faces-redirect=true";		
	}
	public String delete(Payment payment) {
		this.payment = payment;
		return "deleteDebtors.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		closeConversation();
		return comeBackUrl+"?faces-redirect=true";
	}
	
	public String doSave() {
		payment.setActive(true);
    	if(service.findByProperty("client", client).isEmpty())
			payment.setFirstPayment(true);
		else
			payment.setFirstPayment(false);
		payment=service.persist(payment);
    	
		List<FilterExample> examples=new ArrayList<>();
    	examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
    	examples.add(new FilterExample("active",true,InequalityConstants.NOT_EQUAL));
    	List<Schedule> list=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
    	BigDecimal paymentAmount=payment.getPaymentAmount();
    	
    	for(Schedule schedule:list){
    		if(paymentAmount.compareTo(schedule.getAmountToPay().subtract(schedule.getAlreadyPayed()))>=0){
    			paymentAmount=paymentAmount.subtract(schedule.getAmountToPay().subtract(schedule.getAlreadyPayed()));
    			schedule.setAlreadyPayed((schedule.getAmountToPay()));
    			schedule.setActive(true);
    			schedule.setFactPayment(payment.getDatePayment());
    			schedule.setPkoNumber(payment.getCheckNumber());
    			scheduleService.merge(schedule);
    			
    		}else if(paymentAmount.compareTo(BigDecimal.ZERO)>0){
    			schedule.setAlreadyPayed(schedule.getAlreadyPayed().add(paymentAmount));
    			schedule.setPkoNumber(payment.getCheckNumber());
    			schedule.setFactPayment(payment.getDatePayment());
    			schedule.setActive(false);
    			scheduleService.merge(schedule);
    			paymentAmount=BigDecimal.ZERO;
    			
    		}else break;	
    	}
    	client.setAlreadyPayed(client.getAlreadyPayed().add(payment.getPaymentAmount()));
    	client.setNotPayedYet(client.getNotPayedYet().subtract(payment.getPaymentAmount()));
    	clientService.merge(client);
		
		
		conversation.end();
		return comeBackUrl+"?faces-redirect=true";
	}
	public void onRowSelect(SelectEvent event) throws IOException {
		client=(Client) event.getObject();
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		System.out.println("get her to client view");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/client/card.xhtml?cid="+conversation.getId());
	}
	
	
	public String doDelete() {
	
		service.remove(payment);
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
		
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public ClientType[] getClientTypes(){
		return ClientType.values();
	}

	public String getComeBackUrl() {
		return comeBackUrl;
	}

	public void setComeBackUrl(String comeBackUrl) {
		this.comeBackUrl = comeBackUrl;
	}
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
}
