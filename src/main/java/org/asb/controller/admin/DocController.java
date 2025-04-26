package org.asb.controller.admin;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.transaction.SystemException;

import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.controller.FileUploadController;
import org.asb.enums.DocumentType;
import org.asb.model.Attachment;
import org.asb.model.Client;
import org.asb.model.ClientDocument;
import org.asb.model.CommentLog;
import org.asb.model.Denounce;
import org.asb.model.Garant;
import org.asb.model.Payment;
import org.asb.model.ResponsiblePerson;
import org.asb.model.Schedule;
import org.asb.model.ScheduleTemplate;
import org.asb.model.User;
import org.asb.service.ClientDocumentService;
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
import org.asb.util.web.LoginUtil;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles= {0,2,4,7,10,9,12})
public class DocController implements Serializable {

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
	
	@Inject
	private FileUploadController fileUploadController;
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
	
	
	
	public DocController() {}
	
	@PostConstruct
	public void initialize() {
		if(conversation.isTransient()) { 
			conversation.begin();
			conversation.setTimeout(1800000L);
		}
		if(document == null) {
			document = new ClientDocument();
		}
		
		goToDocuments();
		 
	}
	
	

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
    
    
    
	
	
	
	
	
	public String cancel() {
		closeConversation();
		return comeBackUrl+"?faces-redirect=true";
	}
	
	
	public String doSaveDocument() {
		
		Calendar calendar = new GregorianCalendar();		
		document.setDate(calendar.getTime());		
		document.setGlobal(true);
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
		
		return goToDocuments();
	}
	
	
	
	
	
	
	public String goToDocuments() {	
		documents=documentService.findByProperty("global", true);
		return "background-images.xhtml?faces-redirect=true";
	}
	
	public String goToAddDocuments() {
		document = new ClientDocument();
		return "document_form.xhtml?faces-redirect=true";
	}
	
	
	public String deleteDoc(ClientDocument cd) {
		documentService.remove(cd);
		documents=documentService.findByProperty("global", true);
		return "documents.xhtml?faces-redirect=true";
	}
		
	public List<DocumentType> getAllDocumentType() {
		return Arrays.asList(DocumentType.values());
	}
	
	public String getComeBackUrl() {
		return comeBackUrl;
	}

	public void setComeBackUrl(String comeBackUrl) {
		this.comeBackUrl = comeBackUrl;
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

	
}
