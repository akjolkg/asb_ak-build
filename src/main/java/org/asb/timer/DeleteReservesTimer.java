package org.asb.timer;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.enums.ClientFrom;
import org.asb.enums.ClientStatus;
import org.asb.enums.ClientType;
import org.asb.model.Appartment;
import org.asb.model.Attachment;
import org.asb.model.Client;
import org.asb.model.ClientDocument;
import org.asb.model.ContractTemplate;
import org.asb.model.DeletedReserve;
import org.asb.model.Payment;
import org.asb.model.ScheduleTemplate;
import org.asb.service.AttachmentService;
import org.asb.service.ClientDocumentService;
import org.asb.service.ClientService;
import org.asb.service.ContractTemplateService;
import org.asb.service.DeletedReserveService;
import org.asb.service.DenounceService;
import org.asb.service.PaymentService;
import org.asb.service.ScheduleService;
import org.asb.service.ScheduleTemplateService;
import org.asb.util.web.Messages;


/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Startup
@Singleton
@Lock(LockType.READ)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class DeleteReservesTimer {
	
	@EJB
	private ClientService service;
	@EJB
	private ScheduleService scheduleService;
	@EJB
	private ScheduleTemplateService stService;
	@EJB
	private ClientDocumentService documentService;
	@EJB
	private DeletedReserveService drService;
	@EJB
	private PaymentService paymentService;
	@EJB
	private AttachmentService aService;
	@EJB
	private ContractTemplateService ctService;
	
	
	@Schedule(hour="*/5", minute="10")
	private void daily() {
		List<ClientType> types=new ArrayList<>(); 
		types.add(ClientType.RESERVE);
		types.add(ClientType.RESERVE_FOR_EXCHANGE);
		types.add(ClientType.RESERVE_FOR_PRESENT);
		List<FilterExample>examples=new ArrayList<FilterExample>();
		examples.add(new FilterExample("type",types,InequalityConstants.IN));
		
		for(Client cl:service.findByExample(0, 100000, examples)) {			
			if(cl.getDateCreated()==null) {
				cl.setDateCreated(new Date());
			}
			else {				
				long diff = new Date().getTime() - cl.getDateCreated().getTime();
			    long d=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			    
			    if (d>7) {
					System.out.println("difference="+d);
					List<FilterExample> exs=new ArrayList<>();
					exs.add(new FilterExample("client",cl, InequalityConstants.EQUAL));
					if(paymentService.countByExample(exs)>0 && paymentService.sumByExample("paymentAmount", exs).compareTo(new BigDecimal(99999))>-1) {
						if(d>30) {
							deleteClient(cl);
						}
					}else{						
						deleteClient(cl); 
					}
				}else if (d>3) {
					System.out.println("difference="+d);
					List<FilterExample> exs=new ArrayList<>();
					exs.add(new FilterExample("client",cl, InequalityConstants.EQUAL));
					if(paymentService.countByExample(exs)<1 || paymentService.sumByExample("paymentAmount", exs).compareTo(new BigDecimal(1000))<0) {
						deleteClient(cl);
					}
				}
			    
			    
			}
		}
	}
	
	@Schedule(hour="2", minute="20")
	private void deleteUnusedAttachments() {
		Set<Attachment> usedAttachments = new HashSet<>();
		usedAttachments.addAll(documentService.findAll().stream()
		    .map(ClientDocument::getAttachment)
		    .collect(Collectors.toList()));
		usedAttachments.addAll(ctService.findAll().stream()
		    .map(ContractTemplate::getAttachment)
		    .collect(Collectors.toList()));

		aService.findAll().stream()
		    .filter(a -> !usedAttachments.contains(a))
		    .forEach(aService::remove);
		
		
	}
	
	@Schedule(hour="*/1", minute="10")
	private void hourly() {
		List<ClientStatus> st=new ArrayList<>(); 
		st.add(ClientStatus.NEW);
		st.add(ClientStatus.WAITING_DIRECTOR_APPROVAL);
		st.add(ClientStatus.WAITING_HEAD_APPROVAL);
		List<FilterExample>examples=new ArrayList<FilterExample>();
		examples.add(new FilterExample("status",st,InequalityConstants.IN));
		
		for(Client cl:service.findByExample(0, 100000, examples)) {			
			if(cl.getDateCreated()==null) {
				cl.setDateCreated(new Date());
			}
			else {				
				long diff = new Date().getTime() - cl.getDateCreated().getTime();
			    long d=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				
				if (d>0) {
						deleteClient(cl);
					
				}
			}
		}			
	}
	
	
	public void deleteClient(Client client) {
		DeletedReserve dr=new DeletedReserve();
		dr.setDateCreated(new Date());
		dr.setAppartment(client.getAppartment());
		dr.setFio(client.getFio());
		dr.setHeadInfo(client.getAppartment().getConstruction().getTitle()+" : "+client.getAppartment().getTitle());
			
			String history="";
			Appartment appartment=dr.getAppartment();
			
			// client history
			history+="<table class=\"form-table f-left m-top m-left\" style=\"width:400px;\">"+
					"<thead><tr><th colspan=\"2\" scope=\"colgroup\"><span class=\"disp-blc m-top m-bottom\" style=\"text-align: center; font-weight: 600;\">Клиент</span></th></tr>"+
					"</thead><tbody>"+
					
					"<tr><td><span class=\"form-table-label disp-blc\">ПИН (Персональный идентификационный номер)</span></td>"+
					"<td>"+client.getPin()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Дата создания</span></td>"+
					"<td>"+client.getDateCreated()+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Ф.И.О.</span></td>"+
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
				if(client.getClientFrom().equals(ClientFrom.OTHERS) && client.getFromPerson()!=null)
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
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Тип сделки</span></td>"+
					"<td>"+Messages.getEnumMessage(client.getType().toString())+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Тип клиента</span></td>"+
					"<td>"+clientFrom+"</td>"+
					"</tr><tr><td><span class=\"form-table-label disp-blc\">Примечание</span></td>"+
					"<td>"+client.getNote()+"</td>"+
					"</tr></tbody></table> <div class=\"clear\"> </div>";
					
			
			if(client.getPrepay()!=null && client.getPrepay()){
				//contract history
				history+="<table class=\"form-table m-top m-left\" style=\"width:400px;\">"+
						"<thead><tr><th colspan=\"2\" scope=\"colgroup\"><span class=\"disp-blc m-top m-bottom\" style=\"text-align: center; font-weight: 600;\">Договор</span></th></tr>"+
						"</thead><tbody><tr><td><span class=\"form-table-label disp-blc\">Номер договора</span></td>"+
						"<td>"+client.getContractNumber()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Дата договора</span></td>"+
						"<td>"+client.getDateContract()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Дата брони</span></td>"+
						"<td>"+client.getReserveDate()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Цена за 1 м.кв.</span></td>"+
						"<td>"+client.getPriceForSquare()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Сумма договора</span></td>"+
						"<td>"+client.getTotalSum()+"</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Сумма задатка</span></td>"+
						"<td>"+client.getReserveAmount()+"</td>"+
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
				history+="</td>"+
						"</tr><tr><td><span class=\"form-table-label disp-blc\">Куратор</span></td>"+
						"<td>"+client.getCurator().getFio()+"</td>"+
						"</tr></tbody></table>";
				
				
				//schedule history
				List<FilterExample> examples=new ArrayList<>();
				examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
				List<org.asb.model.Schedule> schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
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
				for(org.asb.model.Schedule s:schedules){ 
					history+="<tr data-ri=\"0\" class=\"ui-widget-content\" role=\"row\">"
							+ "<td role=\"gridcell\">"+k+"</td>"+s
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
			dr.setHistory(history);
			dr=drService.persist(dr);
			
			
			for(Payment payment:paymentService.findByProperty("client", client)){
				paymentService.remove(payment);
			}
			for(org.asb.model.Schedule schedule:scheduleService.findByProperty("client", client)){
				scheduleService.remove(schedule);
			}
			
			for(ScheduleTemplate st:stService.findByProperty("client", client)){
				st.setClient(null);
				stService.merge(st);
			}
			
			service.remove(client);
	}
	
}

