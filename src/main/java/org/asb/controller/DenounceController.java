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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.enums.ClientFrom;
import org.asb.enums.ClientStatus;
import org.asb.enums.LogType;
import org.asb.enums.ObjectType;
import org.asb.model.Appartment;
import org.asb.model.Client;
import org.asb.model.ClientDocument;
import org.asb.model.Denounce;
import org.asb.model.Log;
import org.asb.model.Payment;
import org.asb.model.Schedule;
import org.asb.model.ScheduleTemplate;
import org.asb.service.ClientDocumentService;
import org.asb.service.ClientService;
import org.asb.service.DenounceService;
import org.asb.service.LogService;
import org.asb.service.PaymentService;
import org.asb.service.ScheduleService;
import org.asb.service.ScheduleTemplateService;
import org.asb.service.UserService;
import org.asb.util.web.LoginUtil;
import org.asb.util.web.Messages;
import org.bouncycastle.crypto.tls.ClientAuthenticationType;
import org.primefaces.event.SelectEvent;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles= {0,2,4,7,10,12})
public class DenounceController implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private String sendBackReason;
	private Denounce denounce;
	private List<ClientDocument> documents;
	
	@EJB
	private LogService logService;
	@EJB
	private ClientService service;
	@EJB
	private ScheduleService scheduleService;
	@EJB
	private ScheduleTemplateService stService;
	@EJB
	private ClientDocumentService documentService;
	@EJB
	private DenounceService ddService;
	@EJB
	private PaymentService paymentService;
	@EJB
	private UserService userService;
	
	
	@Inject
	private Conversation conversation;
	@Inject
	private LoginUtil loginUtil;
	
	
	
	public DenounceController() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String sendToDecline(){
		denounce=ddService.findById(denounce.getId(), false);
		if(!denounce.getStatus().equals(ClientStatus.NEW)&&!denounce.getStatus().equals(ClientStatus.APPROVED)){
			denounce.setSendBackReason(sendBackReason);
			denounce.setStatus(ClientStatus.NEW);
			denounce=ddService.merge(denounce);
			
			Log log=new Log();
			log.setObjectType(ObjectType.DENOUNCE);
			log.setType(LogType.STATUS_CHANGE);
			log.setDescription("Отправление на редактирование: "+denounce.toLog());
			log.setUser(loginUtil.getCurrentUser());
			log.setObjectId(denounce.getId());
			logService.persist(log);	
			
			
			
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Расторжение успешно отправлено на редактирование!", null) );
		}
		return "list.xhtml";
		
	}
	
	public String sendToFinDirectorApprove(){
		denounce=ddService.findById(denounce.getId(), false);
		if(denounce.getStatus().equals(ClientStatus.NEW)){
			denounce.setSendBackReason(null);
			denounce.setStatus(ClientStatus.WAITING_DIRECTOR_APPROVAL);
			denounce=ddService.merge(denounce);
			Log log=new Log();
			log.setObjectType(ObjectType.DENOUNCE);
			log.setType(LogType.STATUS_CHANGE);
			log.setDescription("Отправление Фин.Директору: "+denounce.toLog());
			log.setUser(loginUtil.getCurrentUser());
			log.setObjectId(denounce.getId());
			logService.persist(log);	
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Расторжение успешно отправлено на подтверждение!", null) );
		}
		return "list.xhtml";
		
	}
	
	public String approve(){
		denounce=ddService.findById(denounce.getId(), false);
		if(denounce.getStatus().equals(ClientStatus.WAITING_DIRECTOR_APPROVAL)){
			String history="";
			Appartment appartment=denounce.getAppartment();
			Client client=denounce.getAppartment().getClient();
			
			// client history
			history+="<table class=\"form-table f-left m-top m-left\" style=\"width:400px;\">"+
					"<thead><tr><th colspan=\"2\" scope=\"colgroup\"><span class=\"disp-blc m-top m-bottom\" style=\"text-align: center; font-weight: 600;\">Клиент</span></th></tr>"+
					"</thead><tbody><tr><td><span class=\"form-table-label disp-blc\">Ф.И.О.</span></td>"+
					"<td>"+client.getFio()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Паспортные данные</span></td>"+
					"<td>"+client.getPassportNumber()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Адрес по паспорту</span></td>"+
					"<td>"+client.getPassportAddress()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Фактический адрес</span></td>"+
					"<td>"+client.getFactAddress()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Whatsapp</span></td>"+
					"<td>"+client.getWhatsappNumber()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Телефон</span></td>"+
					"<td>"+client.getContacts()+"</td>";
			if(client.getMaritalStatus()){
				history+="</tr><tr><td><span class=\"form-table-label disp-blc\">Ф.И.О. супруга(и)</span></td>"+
						"<td>"+client.getSpouseFio()+"</td>";				
			}
			history+="</tr><tr><td><span class=\"form-table-label disp-blc\">КП|Оф.</span></td>"+
					"<td>"+client.getKeys()+"|"+client.getSigned()+"</td>";
			history+="</tr><tr><td><span class=\"form-table-label disp-blc\">Ниже сетки</span></td>"+
					"<td>"+client.getExtralow()+"</td>";
			
			history+="</tr></tbody></table>";			
			
			if(client.getNote()==null)
				client.setNote("");
			//object history
			
			String clientFrom="";
			if(client.getClientFrom()!=null) {
				clientFrom=clientFrom+Messages.getEnumMessage(client.getClientFrom().toString());
				if(client.getClientFrom().equals(ClientFrom.OTHERS))
					clientFrom=clientFrom+" ("+client.getFromPerson().getFio()+")";
				
			}
			
			history+="<table class=\"form-table m-top m-left\" style=\"width:400px;\">"+
					"<thead><tr><th colspan=\"2\" scope=\"colgroup\"><span class=\"disp-blc m-top m-bottom\" style=\"text-align: center; font-weight: 600;\">Объект</span></th></tr>"+
					"</thead><tbody><tr><td><span class=\"form-table-label disp-blc\">Строительный объект</span></td>"+
					"<td>"+appartment.getConstruction().getTitle()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Предварительный номер</span></td>"+
					"<td>"+appartment.getTitle()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Постоянный номер квартиры, согласно техпаспорту</span></td>"+
					"<td>"+appartment.getDocNumber()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Этаж (от земли, начиная с первого)</span></td>"+
					"<td>"+appartment.getFlat()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Общая площадь ориентировочно</span></td>"+
					"<td>"+appartment.getTotalArea()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Общая площадь по техпаспорту</span></td>"+
					"<td>"+appartment.getDocTotalArea()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Тип договора</span></td>"+
					"<td>"+Messages.getEnumMessage(client.getContractType().toString())+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Тип сделки</span></td>"+
					"<td>"+Messages.getEnumMessage(client.getType().toString())+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Тип клиента</span></td>"+
					"<td>"+clientFrom+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Планировка</span></td>"+
					"<td>"+(client.getAppartment().getPtype()==null?"" : Messages.getEnumMessage(client.getAppartment().getPtype().toString()))+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Лояльность клиента</span></td>"+
					"<td>"+(client.getLoyality()==null?"" : Messages.getEnumMessage(client.getLoyality().toString()))+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Впечатление клиента ОРК</span></td>"+
					"<td>"+(client.getLoyalityOrk()==null ? "" : Messages.getEnumMessage(client.getLoyalityOrk().toString()))+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Примечание</span></td>"+
					"<td>"+client.getNote()+"</td>"+
					"</tr></tbody></table> <div class=\"clear\"> </div>";
					
			List<Integer> types=new ArrayList<>();
			types.add(1);
			types.add(2);
			types.add(5);
			types.add(6);
			types.add(7);
			if(types.contains(client.getType().ordinal())||(client.getType().ordinal()==0 && client.getPrepay()!=null && client.getPrepay()==true)){
				//contract history
				history+="<table class=\"form-table m-top m-left\" style=\"width:400px;\">"+
						"<thead><tr><th colspan=\"2\" scope=\"colgroup\"><span class=\"disp-blc m-top m-bottom\" style=\"text-align: center; font-weight: 600;\">Договор</span></th></tr>"+
						"</thead><tbody><tr><td><span class=\"form-table-label disp-blc\">Номер договора</span></td>"+
						"<td>"+client.getContractNumber()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Дата договора</span></td>"+
						"<td>"+client.getDateContract()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Цена за 1 м.кв.</span></td>"+
						"<td>"+client.getPriceForSquare()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Сумма договора</span></td>"+
						"<td>"+client.getTotalSum()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Первоначальный взнос</span></td>"+
						"<td>"+client.getFirstPayment()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Оплачено</span></td>"+
						"<td>"+client.getAlreadyPayed();
				if(client.getAlreadyPayed()!=null&&client.getTotalSum()!=null&&client.getTotalSum().compareTo(BigDecimal.ZERO)>0){
					history+="<b> "+(client.getAlreadyPayed().multiply(new BigDecimal(100)).divide(client.getTotalSum(), 2, BigDecimal.ROUND_HALF_UP))+"%</b>";
				}
				history+="</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Остаток на оплату</span></td>"+
						"<td>"+client.getNotPayedYet();
				if(client.getNotPayedYet()!=null&&client.getTotalSum()!=null&&client.getTotalSum().compareTo(BigDecimal.ZERO)>0){
					history+="<b> "+(client.getNotPayedYet().multiply(new BigDecimal(100)).divide(client.getTotalSum(), 2, BigDecimal.ROUND_HALF_UP))+"%</b>";
				}	
				
				
				
				
				if(client.getAppartment().getDocTotalArea()!=null && client.getPriceForSquare()!=null) {
					history+="</td>"+
							"</tr><tr><td><span class=\"form-table-label disp-blc\">Остаток после инвентеризации</span></td>"+
							"<td>"+client.getAppartment().getDocTotalArea().multiply(client.getPriceForSquare()).subtract(client.getAlreadyPayed());
					if(client.getAlreadyPayed()!=null&&client.getTotalSum()!=null&&client.getTotalSum().compareTo(BigDecimal.ZERO)>0){
						history+="<b> "+((client.getAppartment().getDocTotalArea().multiply(client.getPriceForSquare()).subtract(client.getAlreadyPayed())).multiply(new BigDecimal(100)).divide(client.getTotalSum(), 2, BigDecimal.ROUND_HALF_UP))+"%</b>";
					}	
				}
				
				
				history+="</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Куратор</span></td>"+
						"<td>"+client.getCurator().getFio();
				history+="</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Куратор ОРК</span></td>"+
						"<td>"+client.getCuratorOrk().getFio()+"</td>"+
						"</tr></tbody></table>";
				
				
				//schedule history
				List<FilterExample> examples=new ArrayList<>();
				examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
				List<Schedule> schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
				List<Payment> payments=paymentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
				history+="<div class=\"clear\"> </div>";
				history+="<table class=\"form-table f-left m-top m-left\" style=\"width:800px;\">"
						+"<thead><tr><th colspan=\"1\" scope=\"colgroup\"><span class=\"disp-blc m-top m-bottom\" style=\"text-align: center; font-weight: 600;\">График оплаты</span></th></tr>"+
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
				
				history+="<table class=\"form-table f-left m-top m-left\" style=\"width:800px;\">"
						+"<thead><tr><th colspan=\"1\" scope=\"colgroup\"><span class=\"disp-blc m-top m-bottom\" style=\"text-align: center; font-weight: 600;\">История денежных поступлений</span></th></tr>"+
						"</thead><tbody><tr><td><div id=\"table\" class=\"ui-datatable ui-widget\"><div class=\"ui-datatable-tablewrapper\"><table role=\"grid\" class=\"display-table\">"+
						"<thead id=\"table_head\"><tr role=\"row\">"
						+ "<th id=\"table:j_idt98\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"№\" scope=\"col\"><span class=\"ui-column-title\">№</span></th>"
						+ "<th id=\"table:j_idt100\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Дата оплаты\" scope=\"col\"><span class=\"ui-column-title\">Номер ПКО</span></th>"
						+ "<th id=\"table:j_idt102\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Сумма оплаты\" scope=\"col\"><span class=\"ui-column-title\">Дата оплаты</span></th>"
						+ "<th id=\"table:j_idt104\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Оплачено\" scope=\"col\"><span class=\"ui-column-title\">Сумма оплаты</span></th>"
						+ "<th id=\"table:j_idt106\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Завершён\" scope=\"col\"><span class=\"ui-column-title\">Тип платежа</span></th>"
						+ "<th id=\"table:j_idt108\" class=\"ui-state-default\" role=\"columnheader\" aria-label=\"Примечание\" scope=\"col\"><span class=\"ui-column-title\">Примечание</span></th></tr></thead>"
						+ "<tbody id=\"table_data\" class=\"ui-datatable-data ui-widget-content\">";
				k=1;
				for(Payment s:payments){
					String firstPayment="По графику";
					if(s.getFirstPayment())firstPayment="Первоначальный взнос";
					history+="<tr data-ri=\"0\" class=\"ui-widget-content\" role=\"row\">"
							+ "<td role=\"gridcell\">"+k+"</td>"
							+ "<td role=\"gridcell\">"+s.getCheckNumber()+"</td>"
							+ "<td role=\"gridcell\">"+s.getDatePayment()+"</td>"
							+ "<td role=\"gridcell\">"+s.getPaymentAmount()+"</td>"
							+ "<td role=\"gridcell\">"+firstPayment+"</td>"
							+ "<td role=\"gridcell\">"+s.getNote()+"</td></tr>";
				}
				history+="</tbody></table></div></div></td></tr></tbody></table>";
				
				
				
				
				
				
				
			}
					
			System.out.println("history==="+history);
			denounce.setHistory(history);
			denounce.setTotalSum(client.getTotalSum());
			denounce.setContractDate(client.getDateContract());
			denounce.setContractType(client.getClientFrom());
			denounce.setClientType(client.getType());
			denounce.setPayedAmount(client.getAlreadyPayed());
			denounce.setStatus(ClientStatus.APPROVED);
			denounce.setFactDenounce(new Date());
			denounce=ddService.merge(denounce);
			Log log=new Log();
			log.setObjectType(ObjectType.DENOUNCE);
			log.setType(LogType.STATUS_CHANGE);
			log.setDescription("Подтвержден: "+denounce.toLog());
			log.setUser(loginUtil.getCurrentUser());
			log.setObjectId(denounce.getId());
			logService.persist(log);	
			
			
			for(Payment payment:paymentService.findByProperty("client", client)){
				paymentService.remove(payment);
			}
			for(Schedule schedule:scheduleService.findByProperty("client", client)){
				scheduleService.remove(schedule);
			}
			for(ClientDocument document:documentService.findByProperty("client", client)){
				document.setClient(null);
				document.setDenounce(denounce);
				documentService.merge(document);
			}
			
			for(ScheduleTemplate st:stService.findByProperty("client", client)){
				st.setClient(null);
				stService.merge(st);
			}
			
			service.remove(client);
			
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Расторжение успешно подтверждёно!", null) );
		}
		return "list.xhtml";
	}
	
	public String goToDelete() {
		return "delete.xhtml?faces-redirect=true";
	}
	
	public String delete() {
		Log log=new Log();
		log.setObjectType(ObjectType.DENOUNCE);
		log.setType(LogType.DELETE);
		log.setDescription("Удаление: "+denounce.toLog());
		log.setUser(loginUtil.getCurrentUser());
		log.setObjectId(denounce.getId());
		logService.persist(log);	
		ddService.remove(denounce);		
		return "list.xhtml?faces-redirect=true";
	}
	
	
	public String cancel() {
		closeConversation();
		return "list.xhtml"+"?faces-redirect=true";
	}
	
	public void onRowSelect(SelectEvent event) throws IOException {
		denounce=(Denounce) event.getObject();
		
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("denounce",denounce,InequalityConstants.EQUAL));
		setDocuments(documentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "date"));
		
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/denouncement/card.xhtml?cid="+conversation.getId());
	}
	
	public String getSendBackReason() {
		return sendBackReason;
	}

	public void setSendBackReason(String sendBackReason) {
		this.sendBackReason = sendBackReason;
	}

	public Denounce getDenounce() {
		return denounce;
	}

	public void setDenounce(Denounce denounce) {
		this.denounce = denounce;
	}

	public List<ClientDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<ClientDocument> documents) {
		this.documents = documents;
	}
	
}
