package org.asb.controller.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.asb.enums.AppartmentType;
import org.asb.enums.LogType;
import org.asb.enums.ObjectType;
import org.asb.enums.Role;
import org.asb.enums.ScopeConstants;
import org.asb.model.Appartment;
import org.asb.model.Log;
import org.asb.service.AppartmentService;
import org.asb.service.LogService;
import org.asb.util.web.FacesScopeQualifier;
import org.asb.util.web.LoginUtil;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles=0)
public class GarageCreator implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private Appartment appartment;
	
	@EJB
	private AppartmentService service;
	
	
	@Inject
	private Conversation conversation;
	@EJB
	private LogService logService;
	@Inject
	private LoginUtil loginUtil;
	
	public GarageCreator() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String add() {
		appartment=new Appartment();
		appartment.setType(AppartmentType.PARKING);
		return "form.xhtml?faces-redirect=true";
		
	}
	
	public String edit(Appartment appartment) {
		this.appartment = appartment;
		return "form.xhtml?faces-redirect=true";
	}
	
	public String delete(Appartment appartment) {
		this.appartment = appartment;
		return "deleteAppartment.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doSave() {
		
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("construction",appartment.getConstruction(),InequalityConstants.EQUAL));
		examples.add(new FilterExample("title",appartment.getTitle(),InequalityConstants.EQUAL));
		if(appartment.getId()!=null)
			examples.add(new FilterExample("id",appartment.getId(),InequalityConstants.NOT_EQUAL));
		if(service.countByExample(examples)>0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Такой номер уже зарегистрирован!!!", "Такой номер уже зарегистрирован!!!") );
			return null;
		}
		
		
		Log log=new Log();
		String descOld="";
		if(appartment.getId()==null) {
			log.setObjectType(ObjectType.APPARTMENT);
			log.setType(LogType.CREATE);
			log.setDescription(appartment.toLog());
		}else {
			log.setObjectType(ObjectType.APPARTMENT);
			log.setType(LogType.EDIT);
			
			Appartment old=service.findById(appartment.getId(), false);
			descOld=old.toLog();
		}
		appartment = appartment .getId() == null ? service.persist(appartment) : service.merge(appartment);
		if(descOld.length()>1) {
			appartment=service.findById(appartment.getId(), false);
			System.out.println("--"+descOld+"--");
			System.out.println("--"+appartment.toLog()+"--");
			System.out.println(descOld.equals(appartment.toLog())+"----");
			if(descOld.equals(appartment.toLog()))
				log.setDescription(null);
			else
				log.setDescription("Предыдущее значение : "+descOld+"<br/> Новое значение : "   +appartment.toLog());
			
			
		}
		
		log.setObjectId(appartment.getId());
		log.setUser(loginUtil.getCurrentUser());
		if(log.getDescription()!=null)
			logService.persist(log);
		
		conversation.end();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doDelete() {
	
		service.remove(appartment);
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
		
	public Appartment getAppartment() {
		return appartment;
	}

	public void setAppartment(Appartment appartment) {
		this.appartment = appartment;
	}
	
}
