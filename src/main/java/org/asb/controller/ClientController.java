package org.asb.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.SystemException;

import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.data.DebtorsDTO;
import org.asb.enums.AppartmentType;
import org.asb.enums.ClientFizYur;
import org.asb.enums.ClientFrom;
import org.asb.enums.ClientStatus;
import org.asb.enums.ClientType;
import org.asb.enums.ContractType;
import org.asb.enums.DenounceType;
import org.asb.enums.DocumentType;
import org.asb.enums.LogType;
import org.asb.enums.Loyality;
import org.asb.enums.ObjectType;
import org.asb.enums.OfficeType;
import org.asb.enums.Role;
import org.asb.enums.UserStatus;
import org.asb.model.Appartment;
import org.asb.model.Attachment;
import org.asb.model.Client;
import org.asb.model.ClientDocument;
import org.asb.model.CommentLog;
import org.asb.model.Construction;
import org.asb.model.Denounce;
import org.asb.model.Garant;
import org.asb.model.Log;
import org.asb.model.Payment;
import org.asb.model.ResponsiblePerson;
import org.asb.model.Schedule;
import org.asb.model.ScheduleTemplate;
import org.asb.model.SubSchedule;
import org.asb.model.User;
import org.asb.service.ClientDocumentService;
import org.asb.service.ClientService;
import org.asb.service.CommentLogService;
import org.asb.service.ConstructionService;
import org.asb.service.DenounceService;
import org.asb.service.GarantService;
import org.asb.service.LogService;
import org.asb.service.PaymentService;
import org.asb.service.ResponsiblePersonService;
import org.asb.service.ScheduleService;
import org.asb.service.ScheduleTemplateService;
import org.asb.service.SubScheduleService;
import org.asb.service.UserService;
import org.asb.util.Serializer;
import org.asb.util.web.HttpUtil;
import org.asb.util.web.LoginUtil;
import org.asb.util.web.Messages;
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
public class ClientController implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private Client client;
	private List<User> curators;
	private List<User> curatorOrks;
	private List<Garant> garants;
	private List<ResponsiblePerson> responsiblePersons;
	private String comeBackUrl;
	private List<Schedule> schedules;
	private List<Payment> payments;
	private List<ClientDocument> documents;
	private List<CommentLog> comments;
	private Set<Schedule> modifiedSchedules;
	private ClientDocument document;
	private String sendBackReason;
	private Denounce denounce;
	private Integer debtDays;
	private ScheduleTemplate template;
	private CommentLog cl=new CommentLog();
	private Integer clientId;
	private Date contractMaxDate;
	private BigDecimal difference;
	
	@Inject
	private FileUploadController fileUploadController;
	@EJB
	private ClientService service;
	@EJB
	private ConstructionService conService;
	@EJB
	private ScheduleService scheduleService;
	@EJB
	private SubScheduleService ssService;
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
	@EJB
	private GarantService garantService;
	@EJB
	private ResponsiblePersonService rpService;
	@EJB
	private LogService logService;
	@EJB
	private CommentLogService clService;
	
	
	
	
	@Inject
	private Conversation conversation;
	@Inject
	private LoginUtil loginUtil;
	
	
	
	public ClientController() {}
	
	@PostConstruct
	public void initialize() {
		if(conversation.isTransient()) { 
			conversation.begin();
			conversation.setTimeout(1800000L);
		}
		if(document == null) {
			document = new ClientDocument();
		}
		
		clientId=null;
		FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        String projectId = paramMap.get("clientId");
        if(projectId!=null) {
        	clientId=new Integer(projectId);
        }
		
	}
	
	public String getClientCardUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
		if(client!=null)
			return HttpUtil.getContextUrl(request) + "/view/client/cardView.xhtml?clientId=" + client.getId();
		else return "";
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
    
    
    public void calculateSum() {
    	client.setTotalSum(client.getAppartment().getTotalArea().multiply(client.getPriceForSquare()).setScale(2, RoundingMode.UP));
    	
    }
    
	public String addClient(Appartment appartment, String comeBackUrl ) {
		
		if(appartment.getConstruction().getBlockText()!=null && appartment.getConstruction().getBlockText().length()>0) {
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "На данный объект нельзя оформлять договор! <br/><br/><br/> "+ appartment.getConstruction().getBlockText(),null) );
			
			return null;
		}
		
		fileUploadController.initialize();
		this.comeBackUrl=comeBackUrl;
		client=appartment.getClient();
		if(client==null) {
			client=new Client();
			client.setStatus(ClientStatus.NEW);
			client.setAlreadyPayed(BigDecimal.valueOf(0));
			client.setDateContract(new Date());
			client.setReserveDate(new Date());
			client.setAppartment(appartment);
			client.setContractNumber(appartment.getConstruction().getCompany().getShortName()+"-"+appartment.getConstruction().getShortName()+"/номер сгенерируется автоматически после подтверждения");
		}
		if(client.getId()!=null){
			fileUploadController.initialize();
			client=service.getByIdWithFields(client.getId(), new String[]{"contract"});
			if(client.getContract()!=null)
				fileUploadController.getFiles().add(client.getContract());
			System.out.println("clientCotroller====="+client.getContract());
			System.out.println("------"+fileUploadController.getFiles());			
		}		
		return "/view/client/form.xhtml?faces-redirect=true";		
	}
	
	
	public void saveNote(){
		Client nc=service.findById(client.getId(), false);
		nc.setDateLegalSign(client.getDateLegalSign());
		nc.setNote(client.getNote());
		nc.setCuratorNote(client.getCuratorNote());
		nc.setFinDirNote(client.getFinDirNote());
		nc.setControllerNote(client.getControllerNote());
		nc.setLoyalityOrk(client.getLoyalityOrk());
		client=service.merge(nc);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Данные успешно сохранены!", null) );
	}
	
	public void saveOrkNote(){
		Client oldClient=service.findById(client.getId(), false);
		cl.setPreviousNote(oldClient.getCuratorOrkNote());
		cl.setAppartment(client.getAppartment());
		cl.setFio(client.getFio());
		cl.setDateCreated(new Date());
		cl.setUser(loginUtil.getCurrentUser());
		cl.setNote(client.getCuratorOrkNote());
		if(cl.getPreviousNote()==null || cl.getNote()==null || !cl.getNote().equals(cl.getPreviousNote()))
		clService.persist(cl);
		cl=new CommentLog();
		oldClient.setCuratorOrkNote(client.getCuratorOrkNote());
		oldClient.setLoyalityOrk(client.getLoyalityOrk());
		client=service.merge(oldClient);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Данные успешно сохранены!", null) );
	}
	
	
	public String edit(Client client) {
		
		if(client.getAppartment().getConstruction().getBlockText()!=null && client.getAppartment().getConstruction().getBlockText().length()>0) {
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "На данный объект нельзя оформлять договор! <br/><br/><br/> "+ client.getAppartment().getConstruction().getBlockText(),null) );
			
			return null;
		}
		
		fileUploadController.initialize();
		client=service.getByIdWithFields(client.getId(), new String[]{"contract"});
		if(client.getContract()!=null)
			fileUploadController.getFiles().add(client.getContract());
		System.out.println("clientCotroller====="+client.getContract());
		System.out.println("------"+fileUploadController.getFiles());
		this.client = client;
		return "form.xhtml?faces-redirect=true";
	}
	
	public String delete() {
		if(client.getStatus().equals(ClientStatus.APPROVED))
			return null;
		
		
		Log log=new Log();
		log.setObjectType(ObjectType.CLIENT);
		log.setType(LogType.DELETE);
		log.setDescription("Удаление: "+client.toLog());
		log.setUser(loginUtil.getCurrentUser());
		log.setObjectId(client.getId());
		logService.persist(log);	
		
		
		for(Schedule schedule:scheduleService.findByProperty("client", client)){
			scheduleService.remove(schedule);
		}
		for(ClientDocument document:documentService.findByProperty("client", client)){
			documentService.remove(document);
		}
		service.remove(client);
		
		return "/view/appartment/list.xhtml?faces-redirect=true";
	}
	
	
	public String doModifySchedule() {
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		examples.add(new FilterExample("status",ClientStatus.APPROVED,InequalityConstants.NOT_EQUAL));
		if(stService.countByExample(examples)>0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "График клиента уже находится на стадии внесения изменений!", null) );
			return null;
		}
		template=stService.persist(template);
		for(Schedule schedule:scheduleService.findByProperty("client",client)) {
			SubSchedule ss=new SubSchedule();
			ss.setAmountToPay(schedule.getAmountToPay());
			ss.setDatePayment(schedule.getDatePayment());
			ss.setNote(schedule.getNote());
			ss.setScheduleTemplate(template);
			ssService.persist(ss);
			
		}
		
		
		
		
		
		return "/view/scheduleTemplate/list.xhtml?faces-redirect=true";
	}
	
	public String doDenounce() {
		
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("appartment",client.getAppartment(),InequalityConstants.EQUAL));
		examples.add(new FilterExample("status",ClientStatus.APPROVED,InequalityConstants.NOT_EQUAL));
		if(ddService.countByExample(examples)>0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Клиент уже находится на стадии расторжения договора!", null) );
			return null;
		}
		denounce=ddService.persist(denounce);
		
		Log log=new Log();
		log.setObjectType(ObjectType.DENOUNCE);
		log.setType(LogType.CREATE);
		log.setDescription("Создание рассторжения: "+denounce.toLog());
		log.setUser(loginUtil.getCurrentUser());
		log.setObjectId(denounce.getId());
		logService.persist(log);	
		
		
		
		
		return "/view/denouncement/list.xhtml?faces-redirect=true";
	}
	
	
	
	
	
	public String sendToDecline(){
		client=service.getByIdWithFields(client.getId(), new String[]{"contract"});
		if(!client.getStatus().equals(ClientStatus.NEW)&&!client.getStatus().equals(ClientStatus.APPROVED)){
			client.setSendBackReason(sendBackReason);
			client.setStatus(ClientStatus.NEW);
			client=service.merge(client);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Договор успешно отправлен на редактирование!", null) );
			Log log=new Log();
			log.setObjectType(ObjectType.CLIENT);
			log.setType(LogType.STATUS_CHANGE);
			log.setDescription("Отправлен на редактирование <br/> Клиент (ID:"+client.getId()+", ФИО:"+client.getFio()+", Номер объекта:"+client.getAppartment().getTitle()+", Строительный объект:"+client.getAppartment().getConstruction().getTitle()+")");
			log.setUser(loginUtil.getCurrentUser());
			log.setObjectId(client.getId());
			logService.persist(log);
		}
		return "list.xhtml";
		
	}
	
	public String sendToAdminApprove(){
		List<Role> adminRoles=new ArrayList<>();
		adminRoles.add(Role.ADMIN);
		adminRoles.add(Role.SUBADMIN);
		adminRoles.add(Role.FIN_DIRECTOR);
		
		client=service.getByIdWithFields(client.getId(), new String[]{"contract"});
		
		if(client.getPriceForSquare()!=null|| client.getTotalSum()!=null) {
			if(!client.getStatus().equals(ClientStatus.APPROVED)) {
				client.setExtralow(false);
				System.out.println("get Herererer");
				if(scheduleService.countByProperty("client", client)<=1) {
					System.out.println("get Herererer2222---"+client.getAppartment().getConstruction().getMinApPrice().compareTo(client.getPriceForSquare()));
					if(client.getAppartment().getType().equals(AppartmentType.APPARTMENT)
							&&client.getAppartment().getConstruction().getMinApPrice()!=null
							&&client.getAppartment().getConstruction().getMinApPrice().compareTo(client.getPriceForSquare())>0) {						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
					if(client.getAppartment().getType().equals(AppartmentType.OFFICE)
							&&(client.getAppartment().getFlat()!=2)
							&&client.getAppartment().getOfficeType().equals(OfficeType.OFFICE)
							&&client.getAppartment().getConstruction().getMinOfPrice()!=null
							&&client.getAppartment().getConstruction().getMinOfPrice().compareTo(client.getPriceForSquare())>0)
					{						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
					if(client.getAppartment().getType().equals(AppartmentType.OFFICE)
							&&(client.getAppartment().getFlat()==2)
							&&client.getAppartment().getOfficeType().equals(OfficeType.OFFICE)
							&&client.getAppartment().getConstruction().getMinOfPriced()!=null
							&&client.getAppartment().getConstruction().getMinOfPriced().compareTo(client.getPriceForSquare())>0)
					{						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
					if(client.getAppartment().getType().equals(AppartmentType.OFFICE)
							&&!client.getAppartment().getOfficeType().equals(OfficeType.OFFICE)
							&&client.getAppartment().getConstruction().getMinBaPrice()!=null
							&&client.getAppartment().getConstruction().getMinBaPrice().compareTo(client.getPriceForSquare())>0)
					{						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
					if(client.getAppartment().getType().equals(AppartmentType.PARKING)
							&&client.getAppartment().getConstruction().getMinPaPrice()!=null
							&&client.getAppartment().getConstruction().getMinPaPrice().compareTo(client.getTotalSum())>0)
					{						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
				}else	{
					if(client.getAppartment().getType().equals(AppartmentType.APPARTMENT)
							&&client.getAppartment().getConstruction().getMinApPrice2()!=null
							&&client.getAppartment().getConstruction().getMinApPrice2().compareTo(client.getPriceForSquare())>0)
					{						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
					if(client.getAppartment().getType().equals(AppartmentType.OFFICE)
							&&(client.getAppartment().getFlat()!=2)
							&&client.getAppartment().getOfficeType().equals(OfficeType.OFFICE)
							&&client.getAppartment().getConstruction().getMinOfPrice2()!=null
							&&client.getAppartment().getConstruction().getMinOfPrice2().compareTo(client.getPriceForSquare())>0)
					{						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
					if(client.getAppartment().getType().equals(AppartmentType.OFFICE)
							&&(client.getAppartment().getFlat()==2)
							&&client.getAppartment().getOfficeType().equals(OfficeType.OFFICE)
							&&client.getAppartment().getConstruction().getMinOfPriced2()!=null
							&&client.getAppartment().getConstruction().getMinOfPriced2().compareTo(client.getPriceForSquare())>0)
					{						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
					if(client.getAppartment().getType().equals(AppartmentType.OFFICE)
							&&!client.getAppartment().getOfficeType().equals(OfficeType.OFFICE)
							&&client.getAppartment().getConstruction().getMinBaPrice2()!=null
							&&client.getAppartment().getConstruction().getMinBaPrice2().compareTo(client.getPriceForSquare())>0)
					{						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
					if(client.getAppartment().getType().equals(AppartmentType.PARKING)
							&&client.getAppartment().getConstruction().getMinPaPrice2()!=null
							&&client.getAppartment().getConstruction().getMinPaPrice2().compareTo(client.getTotalSum())>0)
					{						
						client.setExtralow(true);
						if (!(client.getClientFrom().equals(ClientFrom.REISSUANCE) && adminRoles.contains(loginUtil.getCurrentUser().getRole()))) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Цена ниже сетки!", null) );
							return null;
						}
					}
				}
			}
		}
		
		
		if(client.getStatus().equals(ClientStatus.NEW)){
			client.setSendBackReason(null);
			client.setStatus(ClientStatus.WAITING_HEAD_APPROVAL);
			client=service.merge(client);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Договор успешно отправлен на подтверждение!", null) );
			Log log=new Log();
			log.setObjectType(ObjectType.CLIENT);
			log.setType(LogType.STATUS_CHANGE);
			log.setDescription("Ожидает подтверждение Нач. Отдела <br/> Клиент (ID:"+client.getId()+", ФИО:"+client.getFio()+", Номер объекта:"+client.getAppartment().getTitle()+", Строительный объект:"+client.getAppartment().getConstruction().getTitle()+")");
			log.setUser(loginUtil.getCurrentUser());
			log.setObjectId(client.getId());
			logService.persist(log);
		}
		return "list.xhtml";
		
	}
	
	public String sendToFinDirectorApprove(){
		client=service.getByIdWithFields(client.getId(), new String[]{"contract"});
		if(client.getStatus().equals(ClientStatus.WAITING_HEAD_APPROVAL)){
			client.setStatus(ClientStatus.WAITING_DIRECTOR_APPROVAL);
			client=service.merge(client);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Договор успешно отправлен на подтверждение!", null) );
			Log log=new Log();
			log.setObjectType(ObjectType.CLIENT);
			log.setType(LogType.STATUS_CHANGE);
			log.setDescription("Ожидает подтверждение Фин. Директора <br/> Клиент (ID:"+client.getId()+", ФИО:"+client.getFio()+", Номер объекта:"+client.getAppartment().getTitle()+", Строительный объект:"+client.getAppartment().getConstruction().getTitle()+")");
			log.setUser(loginUtil.getCurrentUser());
			log.setObjectId(client.getId());
			logService.persist(log);
			
		}
		return "list.xhtml";
		
	}
	
	public String approve(){
		client=service.getByIdWithFields(client.getId(), new String[]{"contract"});
		if(client.getStatus().equals(ClientStatus.WAITING_DIRECTOR_APPROVAL)){
			client.setStatus(ClientStatus.APPROVED);
			Construction c=client.getAppartment().getConstruction();
			if(c.getPosition()==null) c.setPosition(1);
			c.setPosition(c.getPosition()+1);
			c=conService.merge(c);
			Date d = new Date();
	    	String year=String.format("%ty",d);
			
			client.setContractNumber(client.getAppartment().getConstruction().getCompany().getShortName()+"-"+client.getAppartment().getConstruction().getShortName()+"/"+year+"-"+(c.getPosition()));
			
			client.setPlannedDate(c.getRealDate());
			client=service.merge(client);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,  "Договор успешно подтверждён!", null) );
			
			Log log=new Log();
			log.setObjectType(ObjectType.CLIENT);
			log.setType(LogType.STATUS_CHANGE);
			log.setDescription("ПОТВЕРЖДЁН <br/> Клиент (ID:"+client.getId()+", ФИО:"+client.getFio()+", Номер объекта:"+client.getAppartment().getTitle()+", Строительный объект:"+client.getAppartment().getConstruction().getTitle()+")");
			log.setUser(loginUtil.getCurrentUser());
			log.setObjectId(client.getId());
			logService.persist(log);
			
		}
		return "list.xhtml";
		
	}
	
	
	
	public String cancel() {
		closeConversation();
		return comeBackUrl+"?faces-redirect=true";
	}
	
	public String doSave() {
		
		
		if(client.getPrepay()!=null && client.getPrepay() && client.getReserveAmount()!=null && client.getReserveAmount().compareTo(new BigDecimal(50000))<0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Сумма задатка должна быть не менее 50 000!!!", "Сумма задатка должна быть не менее 50 000!!!") );
			return null;
		}
		
		if(client.getTotalSum()!=null && (client.getTotalSum().compareTo(client.getFirstPayment())==-1)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Первоначальный взнос больше суммы договора!!!", "Первоначальный взнос больше суммы договора!!!") );
			return null;
		}
		if(client.getTotalSum()!=null)
			client.setNotPayedYet(client.getTotalSum().subtract(client.getAlreadyPayed()));
		
			if(loginUtil.getCurrentUser().getReserveAmount()==null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "У вас не установлен лимит брони, обратитесь к начальнику отдела продаж!!!", "У вас не установлен лимит брони, обратитесь к начальнику отдела продаж!!!") );
				return null;
			}
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("curator",loginUtil.getCurrentUser(),InequalityConstants.EQUAL));
			examples.add(new FilterExample(true,"status",ClientStatus.NEW,InequalityConstants.EQUAL,false));
			examples.add(new FilterExample(true,"status",ClientStatus.WAITING_HEAD_APPROVAL,InequalityConstants.EQUAL,false));
			examples.add(new FilterExample(true,"status",ClientStatus.WAITING_DIRECTOR_APPROVAL,InequalityConstants.EQUAL,false));
			if(client.getId()!=null)
				examples.add(new FilterExample("id",client.getId(),InequalityConstants.NOT_EQUAL));
			
			if(loginUtil.getCurrentUser().getReserveAmount()<=service.countByExample(examples)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Вы превышаете лимит новых клиентов, проведите инвентаризацию клиентов!!!", "Вы превышаете лимит новых клиентов, проведите инвентаризацию клиентов!!!") );
				return null;
			}
			List<FilterExample> exs=new ArrayList<>();
		if(client.getContractNumber()!=null) {	
		
		}
		
		
		
		
		if(client.getStatus()==null)
			client.setStatus(ClientStatus.NEW);
		
		exs=new ArrayList<>();
		exs.add(new FilterExample("appartment",client.getAppartment(),InequalityConstants.EQUAL));
		
		
		if(client.getId()!=null)
			exs.add(new FilterExample("id",client.getId(),InequalityConstants.NOT_EQUAL));
		if(service.countByExample(exs)>0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Данная квартира/помещение/подземное машиноместо уже занято!!!", "Данная квартира/помещение/подземное машиноместо уже занято!!!") );
			return null;
		}
		if(client.getPriceForSquare()==null) {
			client.setPriceForSquare(client.getTotalSum());
		}
		if(client.getAppartment()!=null && client.getAppartment().getType().equals(AppartmentType.PARKING)) {
			client.setPriceForSquare(client.getTotalSum());
		}
		
		if(!client.getType().equals(ClientType.RESERVE) && 
				!client.getType().equals(ClientType.RESERVE_FOR_EXCHANGE) && 
				!client.getType().equals(ClientType.RESERVE_FOR_PRESENT)) {
			client.setPrepay(null);			
		}
		if(client.getPrepay()==null || client.getPrepay()==false){
			client.setReserveDate(null);
			client.setReserveAmount(null);
		}
		
		Client old=null;
		Log log=new Log();
		log.setObjectType(ObjectType.CLIENT);
		if(client.getId()==null) {
			if(client.getType().equals(ClientType.RESERVE)||client.getType().equals(ClientType.RESERVE_FOR_EXCHANGE)||client.getType().equals(ClientType.RESERVE_FOR_PRESENT))
				if(client.getPrepay()==null || client.getPrepay()==false)
				client.setDateContract(new Date());
			client = service.persist(client);
			log.setType(LogType.CREATE);
			log.setDescription(client.toLog());
		}else {
			old=service.findById(client.getId(), false);
			client = service.merge(client);
			log.setType(LogType.EDIT);
		}
		
		
		schedules=scheduleService.findByProperty("client", client);
		if(client.getTotalSum()==null) 
			client.setTotalSum(BigDecimal.ZERO);
		if(client.getAlreadyPayed()==null)
			client.setAlreadyPayed(BigDecimal.ZERO);
		client.setNotPayedYet(client.getTotalSum().subtract(client.getAlreadyPayed()));
		if(client.getTotalSum()!=null && client.getTotalSum().compareTo(BigDecimal.ZERO) > 0 && (schedules.isEmpty()||schedules.size()<1)) {
			BigDecimal totalSum=client.getTotalSum();
			Schedule schedule=new Schedule();
			schedule.setActive(false);
			schedule.setAlreadyPayed(BigDecimal.valueOf(0));
			schedule.setClient(client);
			schedule.setDatePayment(client.getDateContract());
			schedule.setAmountToPay(client.getFirstPayment());
			schedule.setLeftAmount(client.getFirstPayment());
			schedule.setNote("Первый взнос");
			scheduleService.persist(schedule);
			totalSum=totalSum.subtract(schedule.getAmountToPay());
			
			if (totalSum.compareTo(BigDecimal.ZERO) > 0) {
				schedule=new Schedule();
				schedule.setActive(false);
				schedule.setAlreadyPayed(BigDecimal.ZERO);
				schedule.setClient(client);
				schedule.setDatePayment(client.getDateContract());
				schedule.setAmountToPay(totalSum);
				schedule.setLeftAmount(totalSum);
				scheduleService.persist(schedule);
				totalSum=totalSum.subtract(schedule.getAmountToPay());				
			}		
		}
		Set<Attachment> attachments = null;
		try {
			attachments = fileUploadController.convert();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!attachments.isEmpty()&&attachments.size()>0){
			client.setContract((Attachment)attachments.toArray()[0]);
			client=service.merge(client);
		}
		if(old!=null)
			if(old.toLog().equals(service.findById(client.getId(), false).toLog())) 
				log.setDescription(null);
			else
				log.setDescription("Предыдущее значение : "+old.toLog()+"<br/> Новое значение : "   +service.findById(client.getId(), false).toLog());

		log.setUser(loginUtil.getCurrentUser());
		log.setObjectId(client.getId());
		if(log.getDescription()!=null)
			logService.persist(log);	
		
		examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		payments=paymentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		documents=documentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "date");

		List<FilterExample> exs2=new ArrayList<>();
		exs2.add(new FilterExample("appartment",client.getAppartment(),InequalityConstants.EQUAL));
		comments=clService.findByExample(0, 15, SortEnum.DESCENDING, exs2, "id");
		System.out.println("get her to client view");
		return "card.xhtml?cid="+conversation.getId()+"&faces-redirect=true";
	}
	
	public String doSaveDocument() {
		
		Calendar calendar = new GregorianCalendar();
		
		document.setDate(calendar.getTime());
		document.setClient(client);
		
		document = document .getId() == null ? documentService.persist(document) : documentService.merge(document);
		documents=documentService.findByProperty("client", client);
		
		Set<Attachment> attachments = null;
		try {
			attachments = fileUploadController.convert();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!attachments.isEmpty()&&attachments.size()>0){
			document.setAttachment((Attachment)attachments.toArray()[0]);
			documentService.merge(document);
		}
		if(document.getAttachment()==null) {
			documentService.remove(document);
		}
		
		return formDoc();
	}
	
	public String recalculateSchedule() {
		Date date=client.getAppartment().getConstruction().getInstallmentDate();
		if(client.getTotalSum()!=null && client.getTotalSum().compareTo(BigDecimal.ZERO)>0) {
			BigDecimal leftSum=client.getTotalSum().subtract(client.getFirstPayment());
			Calendar installmentDate=Calendar.getInstance();
			installmentDate.setTime(client.getDateInstallment());
			while(leftSum.compareTo(client.getPerMonthPayment())>0) {
				installmentDate.add(Calendar.MONTH, client.getMonthInstallment());
				leftSum=leftSum.subtract(client.getPerMonthPayment());
			}
			
			/*installmentDate.add(Calendar.MONTH,-client.getMonthInstallment());
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(client.getAppartment().getConstruction().getInstallmentDate());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			if(installmentDate.getTime().after(calendar.getTime())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Вы не укладываетесь в график!!!", null) );
				return null;
			}
			*/
			schedules=scheduleService.findByProperty("client", client);
			for(Schedule schedule:schedules) {
				scheduleService.remove(schedule);
			}
			
			BigDecimal totalSum=client.getTotalSum();
			Schedule schedule=new Schedule();
			schedule.setActive(false);
			schedule.setAlreadyPayed(BigDecimal.ZERO);
			schedule.setClient(client);
			schedule.setDatePayment(client.getDateContract());
			schedule.setAmountToPay(client.getFirstPayment());
			schedule.setLeftAmount(client.getFirstPayment());
			schedule.setNote("Первый взнос");
			scheduleService.persist(schedule);
			totalSum=totalSum.subtract(schedule.getAmountToPay());
			installmentDate.setTime(client.getDateInstallment());
			Integer k=1;
			while (totalSum.compareTo(BigDecimal.ZERO)>0) {
				schedule=new Schedule();
				schedule.setActive(false);
				schedule.setAlreadyPayed(BigDecimal.ZERO);
				schedule.setClient(client);
				schedule.setDatePayment(installmentDate.getTime());
				installmentDate.setTime(client.getDateInstallment());
				installmentDate.add(Calendar.MONTH, k*client.getMonthInstallment());
				k++;
				schedule.setAmountToPay(client.getPerMonthPayment());
				schedule.setLeftAmount(client.getPerMonthPayment());
				if(totalSum.compareTo(client.getPerMonthPayment())<0){
					schedule.setAmountToPay(totalSum);
					schedule.setLeftAmount(totalSum);
				}
				
				System.out.println("totalSum="+totalSum+"   ---  installmentDate="+installmentDate.getTime()+" -------"+client.getAppartment().getConstruction().getInstallmentDate());
				if(installmentDate.getTime().after(client.getAppartment().getConstruction().getInstallmentDate())) {
					schedule.setAmountToPay(totalSum);
					schedule.setLeftAmount(totalSum);
				}
				scheduleService.persist(schedule);
				
				totalSum=totalSum.subtract(schedule.getAmountToPay());				
			}		
		}
		service.merge(client);
		recalculateClient();
		client.getAppartment().getConstruction().setInstallmentDate(date);
		return "schedule.xhtml?faces-redirect=true";
	}
	
	public String recalculateClient() {                                            
    	System.out.println("I got hererere");
    	if(client==null) return null;
    	client=service.findById(client.getId(), false);
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
        	
        	schedules=new ArrayList<>();
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
        	modifiedSchedules=new HashSet<>();
    		modifiedSchedules.addAll(schedules);
    		
    		return null;
    }                                     
	
	
	
	
	public String saveChangesSchedule() {
		BigDecimal totalSum=BigDecimal.ZERO;
		Boolean correct=true;
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(client.getAppartment().getConstruction().getInstallmentDate());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		for(Schedule schedule:modifiedSchedules){
			totalSum=totalSum.add(schedule.getAmountToPay());
			if(schedule.getDatePayment().after(calendar.getTime()))
				correct=false;
			
		}
		if(!correct){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Вы не укладываетесь в график!!!", null) );
			return null;
		}
		System.out.println("totalSum="+totalSum);
		System.out.println("client.totalSum="+client.getTotalSum());
		if(totalSum.compareTo(client.getTotalSum())!=0){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Сумма договора не равна сумме рассрочки!!!", null) );
			return null;
		}
		
		for(Schedule schedule:modifiedSchedules){
			scheduleService.merge(schedule);
			if(schedule.getAmountToPay().equals(BigDecimal.ZERO))
				scheduleService.remove(schedule);
			
			
		}
		recalculateClient();
		return "card.xhtml?faces-redirect=true";
	}
	
	
	
	
	public String goToSchedules() {
		if(doSave() == null) return null;
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		if(client.getDateInstallment()==null){
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(client.getDateContract());
			calendar.add(Calendar.MONTH, 1);
			client.setDateInstallment(calendar.getTime());
		}
		
		
		
		schedules=scheduleService.findByExample(0, 10000,SortEnum.ASCENDING, examples, "datePayment");
		modifiedSchedules=new HashSet<>();
		modifiedSchedules.addAll(schedules);
		return "schedule.xhtml?faces-redirect=true";
	}
	
	
	public String goToDelete() {
		return "delete.xhtml?faces-redirect=true";
	}
	
	public String goToDenounce() {
		return "denounce.xhtml?faces-redirect=true";
	}
	
	public String goToModifySchedule() {
		return "modifySchedule.xhtml?faces-redirect=true";
	}
	
	
	
	
	public String goToDocuments() {	
		System.out.println("");
		if (client.getStatus()!=null && client.getStatus().equals(ClientStatus.APPROVED) && !loginUtil.getCurrentUser().getRole().equals(Role.ADMIN)) {
			documents=documentService.findByProperty("client", client);
			return "documents.xhtml?faces-redirect=true";
		}else {
			if(doSave() == null) return null;
			documents=documentService.findByProperty("client", client);
			return "documents.xhtml?faces-redirect=true";
		}
	}
	
	public String goToAddDocuments() {
		document = new ClientDocument();
		return "document_form.xhtml?faces-redirect=true";
	}
	
	public void onRowEdit(RowEditEvent event) {
		modifiedSchedules.add((Schedule)event.getObject());
    }
	
	public void onRowSelect(SelectEvent event) throws IOException {
		debtDays=null;
		client=(Client) event.getObject();
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		if(schedules!=null&&!schedules.isEmpty()){
			for(Schedule s:schedules){
				if(s.getActive()==false){
					debtDays=(int) ((new Date().getTime()-s.getDatePayment().getTime())/ (1000*60*60*24));
					break;
				}
			}
			BigDecimal total=BigDecimal.ZERO;
			for(Schedule s:schedules){
				total=total.add(s.getAmountToPay());
			}
			difference=client.getTotalSum().subtract(total);
			if (debtDays!=null&&debtDays<=0) debtDays=null;			
		}
		payments=paymentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		documents=documentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "date");

		List<FilterExample> exs=new ArrayList<>();
		exs.add(new FilterExample("appartment",client.getAppartment(),InequalityConstants.EQUAL));
		comments=clService.findByExample(0, 15, SortEnum.DESCENDING, exs, "id");
		System.out.println("get her to client view");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/client/card.xhtml?cid="+conversation.getId());
	}
	
	public void onRowSelectSchedule(SelectEvent event) throws IOException {
		debtDays=null;
		client=((Schedule) event.getObject()).getClient();
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		if(schedules!=null&&!schedules.isEmpty()){
			for(Schedule s:schedules){
				if(s.getActive()==false){
					debtDays=(int) ((new Date().getTime()-s.getDatePayment().getTime())/ (1000*60*60*24));
					break;
				}
			}
			BigDecimal total=BigDecimal.ZERO;
			for(Schedule s:schedules){
				total=total.add(s.getAmountToPay());
			}
			difference=client.getTotalSum().subtract(total);
			if (debtDays!=null&&debtDays<=0) debtDays=null;			
		}
		payments=paymentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		documents=documentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "date");
		
		List<FilterExample> exs=new ArrayList<>();
		exs.add(new FilterExample("appartment",client.getAppartment(),InequalityConstants.EQUAL));
		comments=clService.findByExample(0, 15, SortEnum.DESCENDING, exs, "id");
		
		System.out.println("get her to client view");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/client/card.xhtml?cid="+conversation.getId());
	}
	public void onRowSelectPayment(SelectEvent event) throws IOException {
		debtDays=null;
		client=((Payment) event.getObject()).getClient();
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		if(schedules!=null&&!schedules.isEmpty()){
			for(Schedule s:schedules){
				if(s.getActive()==false){
					debtDays=(int) ((new Date().getTime()-s.getDatePayment().getTime())/ (1000*60*60*24));
					break;
				}
			}
			BigDecimal total=BigDecimal.ZERO;
			for(Schedule s:schedules){
				total=total.add(s.getAmountToPay());
			}
			difference=client.getTotalSum().subtract(total);
			if (debtDays!=null&&debtDays<=0) debtDays=null;			
		}
		
		payments=paymentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		documents=documentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "date");

		List<FilterExample> exs=new ArrayList<>();
		exs.add(new FilterExample("appartment",client.getAppartment(),InequalityConstants.EQUAL));
		comments=clService.findByExample(0, 15, SortEnum.DESCENDING, exs, "id");
		System.out.println("get her to client view");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/client/card.xhtml?cid="+conversation.getId());
	}
	public void redirectToCard(Integer id) throws IOException {
		debtDays=null;
		client=service.findById(id,false);
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		if(schedules!=null&&!schedules.isEmpty()){
			for(Schedule s:schedules){
				if(s.getActive()==false){
					debtDays=(int) ((new Date().getTime()-s.getDatePayment().getTime())/ (1000*60*60*24));
					break;
				}
			}
			BigDecimal total=BigDecimal.ZERO;
			for(Schedule s:schedules){
				total=total.add(s.getAmountToPay());
			}
			difference=client.getTotalSum().subtract(total);
			if (debtDays!=null&&debtDays<=0) debtDays=null;			
		}
		payments=paymentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		documents=documentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "date");

		List<FilterExample> exs=new ArrayList<>();
		exs.add(new FilterExample("appartment",client.getAppartment(),InequalityConstants.EQUAL));
		comments=clService.findByExample(0, 15, SortEnum.DESCENDING, exs, "id");
		System.out.println("get her to client view");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/client/card.xhtml?cid="+conversation.getId());
	}
	public void onRowSelectAppartment(SelectEvent event) throws IOException {
		debtDays=null;
		client=((Appartment ) event.getObject()).getClient();
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		if(schedules!=null&&!schedules.isEmpty()){
			for(Schedule s:schedules){
				if(s.getActive()==false){
					debtDays=(int) ((new Date().getTime()-s.getDatePayment().getTime())/ (1000*60*60*24));
					break;
				}
			}
			BigDecimal total=BigDecimal.ZERO;
			for(Schedule s:schedules){
				total=total.add(s.getAmountToPay());
			}
			difference=client.getTotalSum().subtract(total);
			if (debtDays!=null&&debtDays<=0) debtDays=null;			
		}
		payments=paymentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		documents=documentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "date");

		List<FilterExample> exs=new ArrayList<>();
		exs.add(new FilterExample("appartment",client.getAppartment(),InequalityConstants.EQUAL));
		comments=clService.findByExample(0, 15, SortEnum.DESCENDING, exs, "id");
		System.out.println("get her to client view");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/client/card.xhtml?cid="+conversation.getId());
	}
	public void onRowSelectDebtor(SelectEvent event) throws IOException, ClassNotFoundException {
		debtDays=null;
		client=service.findById(((DebtorsDTO)event.getObject()).getId(),false);
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("client",client,InequalityConstants.EQUAL));
		schedules=scheduleService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		if(schedules!=null&&!schedules.isEmpty()){
			for(Schedule s:schedules){
				if(s.getActive()==false){
					debtDays=(int) ((new Date().getTime()-s.getDatePayment().getTime())/ (1000*60*60*24));
					break;
				}
			}
			BigDecimal total=BigDecimal.ZERO;
			for(Schedule s:schedules){
				total=total.add(s.getAmountToPay());
			}
			difference=client.getTotalSum().subtract(total);
			if (debtDays!=null&&debtDays<=0) debtDays=null;			
		}	
		
		payments=paymentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "datePayment");
		documents=documentService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "date");

		List<FilterExample> exs=new ArrayList<>();
		exs.add(new FilterExample("appartment",client.getAppartment(),InequalityConstants.EQUAL));
		comments=clService.findByExample(0, 15, SortEnum.DESCENDING, exs, "id");
		System.out.println("get her to client view");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/client/card.xhtml?cid="+conversation.getId());
	}
	
	
	public String formDoc() {
		documents=documentService.findByProperty("client", client);
		return "documents.xhtml?faces-redirect=true";
	}
	
	
	public String doDelete() {
		
		if(client.getStatus().equals(ClientStatus.APPROVED))
			return null;
		service.remove(client);
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String deleteDoc(ClientDocument cd) {
		documentService.remove(cd);
		documents=documentService.findByProperty("client", client);
		return "documents.xhtml?faces-redirect=true";
	}
		
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public ContractType[] getContractTypes(){
		return ContractType.values();
	}
	
	
	public List<ClientType> getClientTypes() {
		List<ClientType> types2=Arrays.asList(ClientType.values());
		List<ClientType> types=new ArrayList<>();
		types.addAll(types2);				
		types.remove(9);
		Collections.sort(types , new Comparator<ClientType>() {
            @Override
			public int compare(ClientType o1, ClientType o2) {
            	return Messages.getEnumMessage(o1.toString()).compareTo(Messages.getEnumMessage(o2.toString()));
			}
        });
		return types;
	}
	public Loyality[] getClientLoyalities() {
		
		return Loyality.values();
	}
	
	public ClientFrom[] getClientFroms(){
		return ClientFrom.values();
	}
	
	
	public ClientFizYur[] getFizYurs(){
		return ClientFizYur.values();
	}
	
	
	public DenounceType[] getDenounceTypes(){
		return DenounceType.values();
	}
	

	public List<User> getCurators() {
		if(curators==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("status",UserStatus.ACTIVE, InequalityConstants.EQUAL));
			if(loginUtil.getCurrentUser().getRole().equals(Role.JUNIOR)||loginUtil.getCurrentUser().getRole().equals(Role.FILIAL))
				examples.add(new FilterExample("id",loginUtil.getCurrentUser().getId(), InequalityConstants.EQUAL));
			examples.add(new FilterExample(true,"role",Role.ADMIN, InequalityConstants.EQUAL,false));
			examples.add(new FilterExample(true,"role",Role.FILIAL, InequalityConstants.EQUAL,false));			
			examples.add(new FilterExample(true,"role",Role.JUNIOR, InequalityConstants.EQUAL,false));
			examples.add(new FilterExample(true,"role",Role.SUBADMIN, InequalityConstants.EQUAL,false));
			
			curators=userService.findByExample(0, 1000, SortEnum.ASCENDING, examples,  "fio");
		}
		return curators;
	}
	
	
	
	
	public List<DocumentType> getAllDocumentType() {
		return Arrays.asList(DocumentType.values());
	}

	public void setCurators(List<User> curators) {
		this.curators = curators;
	}

	public String getComeBackUrl() {
		return comeBackUrl;
	}

	public void setComeBackUrl(String comeBackUrl) {
		this.comeBackUrl = comeBackUrl;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	public List<ClientDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<ClientDocument> documents) {
		this.documents = documents;
	}

	public ClientDocument getDocument() {
		return document;
	}

	public void setDocument(ClientDocument document) {
		this.document = document;
	}

	public String getSendBackReason() {
		return sendBackReason;
	}

	public void setSendBackReason(String sendBackReason) {
		this.sendBackReason = sendBackReason;
	}

	public Denounce getDenounce() {
		if(denounce==null){
			denounce=new Denounce();
			denounce.setDateCreated(new Date());
			denounce.setDateDenounce(new Date());
			denounce.setUser(loginUtil.getCurrentUser());
			denounce.setAppartment(client.getAppartment());
			denounce.setHeadInfo(client.getAppartment().getConstruction().getTitle()+" : "+client.getAppartment().getTitle());
			denounce.setFio(client.getFio());
		}
		return denounce;
	}

	public void setDenounce(Denounce denounce) {
		this.denounce = denounce;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public List<Garant> getGarants() {
		if(garants==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null, InequalityConstants.IS_NOT_NULL_SINGLE));
			garants=garantService.findByExample(0, 1000, SortEnum.ASCENDING, examples, "id");
		}
		return garants;
	}

	public void setGarants(List<Garant> garants) {
		this.garants = garants;
	}

	public Integer getDebtDays() {
		return debtDays;
	}

	public void setDebtDays(Integer debtDays) {
		this.debtDays = debtDays;
	}

	public List<ResponsiblePerson> getResponsiblePersons() {
		if(responsiblePersons==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null, InequalityConstants.IS_NOT_NULL_SINGLE));
			responsiblePersons=rpService.findByExample(0, 10000, SortEnum.ASCENDING, examples, "fio");
		}
		return responsiblePersons;
	}
	
	public List<ResponsiblePerson> findResponsiblePerson(String query) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null, InequalityConstants.IS_NOT_NULL_SINGLE));
			examples.add(new FilterExample("fio","%"+query+"%", InequalityConstants.LIKE,true));
			return rpService.findByExample(0, 10000, SortEnum.ASCENDING, examples, "fio");
	}

	public void setResponsiblePersons(List<ResponsiblePerson> responsiblePersons) {
		this.responsiblePersons = responsiblePersons;
	}

	public List<User> getCuratorOrks() {
		if(curatorOrks==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("status",UserStatus.ACTIVE, InequalityConstants.EQUAL));
			examples.add(new FilterExample("role",Role.ORK, InequalityConstants.EQUAL));
			curatorOrks=userService.findByExample(0, 1000, SortEnum.ASCENDING, examples,  "fio");
		}
		return curatorOrks;
	}

	public void setCuratorOrks(List<User> curatorOrks) {
		this.curatorOrks = curatorOrks;
	}

	public ScheduleTemplate getTemplate() {
		if(template==null) {
			template=new ScheduleTemplate();
			template.setDateCreated(new Date());
			template.setUser(loginUtil.getCurrentUser());
			template.setClient(client);
			
		}
		return template;
	}

	public void setTemplate(ScheduleTemplate template) {
		this.template = template;
	}

	public List<CommentLog> getComments() {
		return comments;
	}

	public void setComments(List<CommentLog> comments) {
		this.comments = comments;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Date getContractMaxDate() {
		if(contractMaxDate==null) {
			Calendar c=Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, 30);
			contractMaxDate=c.getTime();
		}
		return contractMaxDate;
	}

	public void setContractMaxDate(Date contractMaxDate) {
		this.contractMaxDate = contractMaxDate;
	}
	
	public boolean getToday() {
		if(client!=null && client.getDateContract()!=null) {
			Calendar c=Calendar.getInstance();
			c.setTime(client.getDateContract());
			c.add(Calendar.DAY_OF_MONTH, 1);
			if(c.getTime().after(new Date()))
				return true;
			
		}
		
		return false;
		
	}

	public BigDecimal getDifference() {
		return difference;
	}

	public void setDifference(BigDecimal difference) {
		this.difference = difference;
	}
	public String refresh() {
    	return null;
    }
	
}
