package org.asb.controller.admin;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.model.Appartment;
import org.asb.service.AppartmentService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles=0)
public class GaragesCreator implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private Appartment appartment;
	
	@EJB
	private AppartmentService service;
	
	
	@Inject
	private Conversation conversation;
	
	
	public GaragesCreator() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String add() {
		appartment=new Appartment();
		return "form.xhtml?faces-redirect=true";
		
	}
	
	public String edit(Appartment appartment) {
		this.appartment = appartment;
		return "form.xhtml?faces-redirect=true";
	}
	
	public String delete(Appartment appartment) {
		this.appartment = appartment;
		return "deleteGarages.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doSave() {
		appartment = appartment .getId() == null ? service.persist(appartment) : service.merge(appartment);
		
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
