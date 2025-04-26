package org.asb.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.conversiation.ConversationClient;
import org.asb.enums.ClientStatus;
import org.asb.enums.ClientType;
import org.asb.enums.LogType;
import org.asb.enums.ObjectType;
import org.asb.enums.PaymentType;
import org.asb.enums.Role;
import org.asb.model.Client;
import org.asb.model.Log;
import org.asb.model.Payment;
import org.asb.model.Schedule;
import org.asb.service.ClientService;
import org.asb.service.LogService;
import org.asb.service.PaymentService;
import org.asb.service.ScheduleService;
import org.asb.util.web.LoginUtil;
import org.primefaces.event.SelectEvent;

import net.sf.jasperreports.engine.JasperPrint;


/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
@Logged
@RolesAllowed(roles= {1,4,11,13})
public class PaymentController  extends BaseReportController implements Serializable{

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
	private ConversationClient conversation;
	@Inject
	private JasperViewerController jasperViewerController; 
	@EJB
	private LogService logService;
    private Date minDate=new Date();
    private Date maxDate=new Date();
    private Payment lastPayment=null;
	
	private String password;
	
	@Inject
	private LoginUtil loginUtil;
	
	public PaymentController() {}
	
	@PostConstruct
	public void initialize() {
		payment=conversation.getPayment();
		client=conversation.getClient();
		comeBackUrl=conversation.getComeBackUrl();
		lastPayment=conversation.getLastPayment();
	}
	
	public void generatePaymentPrint(Payment payment){
		System.out.println("generateContract");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("payment_id", payment.getId());
		
		System.out.println("=== journalViewReport ===");
		System.out.println("payment_id"+payment);
		
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "pko.jasper");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(jasperPrint);				
		jasperViewerController.view(jasperPrint);
	}
	public void checkPayment(Payment payment){
		System.out.println("generateCheck");		
		payment.setPaymentAmount(payment.getReserveAmount());
		if(payment.getCheck()==null || payment.getCheck()==false)
			payment.setCheck(true);
		else 
			payment.setCheck(false);
		service.merge(payment);
		
		Log log=new Log();
		log.setObjectType(ObjectType.PAYMENT);
		log.setType(LogType.STATUS_CHANGE);
		log.setDescription("Платеж потвержден - "+payment.toLog());
		log.setUser(loginUtil.getCurrentUser());
		log.setObjectId(payment.getId());
		logService.persist(log);
		
		recaluclateClient(payment.getClient());
		
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
	    conversation.setComeBackUrl(comeBackUrl);
	    conversation.setPayment(payment);
	    conversation.setClient(client);    
	    
		return "/view/payment/form.xhtml?cid="+conversation.getId()+"&faces-redirect=true";		
	}
    public void searchPreviousPayments() {
    	List<FilterExample> examples=new ArrayList<>();
	    examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
	    Calendar c=Calendar.getInstance();
	    c.add(Calendar.DAY_OF_MONTH, -10);
	    examples.add(new FilterExample("datePayment",c.getTime(),InequalityConstants.GREATER));
	    System.out.println("payment amount="+payment.getPaymentAmount());
	    examples.add(new FilterExample("paymentAmount",payment.getPaymentAmount(),InequalityConstants.EQUAL));
	    if(service.countByExample(examples)>0)
	    	lastPayment=service.findByExample(0, 1, examples).get(0);
	    else
	    	lastPayment=null;
	    
    }
    
    
    public PaymentType[] getPaymentTypes() {
    	return PaymentType.values();
    }
    
    public String addPayment(String comeBackUrl ) {
		this.comeBackUrl=comeBackUrl;
		this.client=null;
		payment=new Payment();
		payment.setClient(client);
		payment.setDatePayment(new Date());
		payment.setCheckNumber(null);
		payment.setWithoutClient(true);
	    conversation.setComeBackUrl(comeBackUrl);
	    conversation.setPayment(payment);
	    conversation.setClient(client);
	    
		return "/view/payment/form.xhtml?cid="+conversation.getId()+"&faces-redirect=true";		
	}
    
    public List<Client> findClient(String query) {
		List<FilterExample> examples=new ArrayList<>();
		
		examples.add(new FilterExample("status",ClientStatus.APPROVED,InequalityConstants.EQUAL));
		examples.add(new FilterExample(true,"fio","%"+query+"%",InequalityConstants.LIKE,true));
		examples.add(new FilterExample(true,"appartment.title","%"+query+"%",InequalityConstants.LIKE,true));
		examples.add(new FilterExample(true,"contractNumber","%"+query+"%",InequalityConstants.LIKE,true));
        return clientService.findByExample(0, 20, SortEnum.ASCENDING, examples, "fio");
    }
	
    public void clientFound(AjaxBehaviorEvent event) {
    	System.out.println("clientIs==="+event.getSource()+"-----"+client);
    	List<FilterExample> examples=new ArrayList<>();
	    examples.add(new FilterExample("client.appartment.construction",client.getAppartment().getConstruction(),InequalityConstants.EQUAL));
	    Integer max=service.findMaxByPropertyWithExamples("checkNumber", examples);
	        if(max==null) max=0;
	        max=max+1;
	    payment.setCheckNumber(max);
    	
    	
    	
    	
    	
    }
    
    
	public String delete(Payment payment) {
		this.payment = payment;
		client=payment.getClient();
		conversation.setClient(client);
		conversation.setPayment(payment);
		return "delete.xhtml?faces-redirect=true";
	}
	public String switchPT(Payment payment) {
		payment.setFirstPayment(payment.getFirstPayment()!=true);
		service.merge(payment);
		
		Log log=new Log();
		log.setObjectType(ObjectType.PAYMENT);
		log.setType(LogType.EDIT);
		log.setDescription(payment.toLog());
		log.setUser(loginUtil.getCurrentUser());
		log.setObjectId(payment.getId());
		logService.persist(log);
		
		
		
		
		return cancelDelete();
	}
	
	public String cancel() {
		return comeBackUrl+"?faces-redirect=true";
	}
	public String cancelDelete() {
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doSave() throws IOException {
		
		if(payment.getClient()==null && client==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Клиент не найден!!!", "Клиент не найден!!!") );
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(payment.getDatePayment());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 1);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);		
		Date persisted=calendar.getTime();
		
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 1);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);		
		Date current=calendar.getTime();
		
		calendar.setTime(getMinDate());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 1);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);		
		Date minimum=calendar.getTime();
		
		
		if(persisted.before(minimum) || persisted.after(current)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Дата платежа не корректна!!!", "Дата платежа не корректна!!!") );
			return null;
		}
		
		if(persisted.after(minimum)) {
			payment.setDatePayment(new Date());
		}
		
		
		payment.setActive(true);
		
		if(payment.getClient()==null)
			payment.setClient(client);
    	if(service.findByProperty("client", client).isEmpty())
			payment.setFirstPayment(true);
		else
		{
			boolean b=true;
			List<Payment> ps=service.findByProperty("client", client);
			Date dp=ps.get(0).getDatePayment();
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(dp);
			for(Payment p:ps) {
				Calendar cal2=Calendar.getInstance();
				cal2.setTime(p.getDatePayment());
				if((cal1.get(Calendar.YEAR)!=cal2.get(Calendar.YEAR))
				    ||
				    (cal1.get(Calendar.MONTH)!=cal2.get(Calendar.MONTH)))
					b=false;					   
			}
			
			Calendar cal2=Calendar.getInstance();
			if((cal1.get(Calendar.YEAR)!=cal2.get(Calendar.YEAR))
				    ||
				    (cal1.get(Calendar.MONTH)!=cal2.get(Calendar.MONTH)))
					b=false;					   
			
			payment.setFirstPayment(b);
    	
		}
    	
    	
    	if(ClientType.STANDART.equals(client.getType())) {
    		List<PaymentType> ptypes=new ArrayList<>();
    		ptypes.add(PaymentType.DISCOUNT);
    		ptypes.add(PaymentType.BUILDING_MATERIAL);
    		ptypes.add(PaymentType.PRESENT);    		
    		if(ptypes.contains(payment.getPaymentType())) {
    			payment.setReserveAmount(payment.getPaymentAmount());
    			payment.setPaymentAmount(BigDecimal.ZERO);
    		}
    	}else if(ClientType.EXCHANGE.equals(client.getType())) {
    		List<PaymentType> ptypes=new ArrayList<>();
    		ptypes.add(PaymentType.DISCOUNT);
    		ptypes.add(PaymentType.PRESENT); 
    		if(ptypes.contains(payment.getPaymentType())) {
    			payment.setReserveAmount(payment.getPaymentAmount());
    			payment.setPaymentAmount(BigDecimal.ZERO);
    		}
    	}else if(ClientType.PRESENT.equals(client.getType())) {
    		payment.setReserveAmount(payment.getPaymentAmount());
    		payment.setPaymentAmount(BigDecimal.ZERO);    		
    	}else {
    		List<PaymentType> ptypes=new ArrayList<>();
    		ptypes.add(PaymentType.DISCOUNT);
    		ptypes.add(PaymentType.PRESENT); 
    		if(ptypes.contains(payment.getPaymentType())) {
    			payment.setReserveAmount(payment.getPaymentAmount());
    			payment.setPaymentAmount(BigDecimal.ZERO);
    		}
    	}
    	
    	
    	
    	
		payment.setUser(loginUtil.getCurrentUser());
		payment=service.persist(payment);
		
		Log log=new Log();
		log.setObjectType(ObjectType.PAYMENT);
		log.setType(LogType.CREATE);
		log.setDescription(payment.toLog());
		log.setUser(loginUtil.getCurrentUser());
		log.setObjectId(payment.getId());
		logService.persist(log);
		
		
		recaluclateClient(client);
		
    	//generatePaymentPrint(payment);    	
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("first", null);
    	return "/view/payment/list.xhtml?faces-redirect=true";
		
	}
	public void onRowSelect(SelectEvent event) throws IOException {
		client=(Client) event.getObject();
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		System.out.println("get her to client view");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/client/card.xhtml?cid="+conversation.getId());
	}
	
	
	public String doDelete() {
		if(!loginUtil.getCurrentUser().getRole().equals(Role.FIN_DIRECTOR)) return null;
		
		if(!loginUtil.getCurrentUser().getPassword().equals(password)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Пароль неверный!!!", "Пароль неверный!!!") );
			return null;
		}
		
		
		Log log=new Log();
		log.setObjectType(ObjectType.PAYMENT);
		log.setType(LogType.DELETE);
		log.setDescription(payment.toLog());
		log.setUser(loginUtil.getCurrentUser());
		log.setObjectId(payment.getId());
		logService.persist(log);
		
		
		service.remove(payment);
		recaluclateClient(client);
		conversation.closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	
	private void recaluclateClient(Client client) {  
		
		client=clientService.findById(client.getId(),false);
		
    	System.out.println("I got hererere");
    	List<FilterExample> examples=new ArrayList<>();
    	examples.add(new FilterExample("id",0,InequalityConstants.NOT_EQUAL));
    	BigDecimal total=BigDecimal.ZERO;
		for(Payment payment:service.findByProperty("client", client)){
			total=total.add(payment.getPaymentAmount());
		}
			client.setAlreadyPayed(total);
			client.setNotPayedYet(client.getTotalSum().subtract(total));
			clientService.merge(client);
			
			List<FilterExample> examples2=new ArrayList<>();
			examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
			examples2.add(new FilterExample("client",client,InequalityConstants.EQUAL));
			List<Payment> payds=new ArrayList<>();
        	payds.addAll(service.findByExample(0,1000, SortEnum.ASCENDING, examples2, "datePayment"));
        	
			
        	int i=0;
        	BigDecimal amoDouble=BigDecimal.ZERO;
        	
        	if(payds.size()>0){
        		amoDouble=payds.get(0).getPaymentAmount();
        		}
        	
        	List<Schedule> schedules=new ArrayList<>();
        	schedules.addAll(scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment"));
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getMinDate() {
		
		Calendar c=Calendar.getInstance();
		if(c.get(Calendar.DAY_OF_MONTH)>1) {
			c.add(Calendar.DAY_OF_MONTH, -1);
		}
		minDate=c.getTime();
		
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public Payment getLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(Payment lastPayment) {
		this.lastPayment = lastPayment;
	}
	
}
