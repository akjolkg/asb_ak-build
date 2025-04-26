package org.asb.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.model.ResponsiblePerson;
import org.asb.service.ResponsiblePersonService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
public class ResponsiblePersonCreator implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private ResponsiblePerson responsiblePerson; 
	@EJB
	private ResponsiblePersonService service;
	@Inject
	private Conversation conversation;
	
	
	public ResponsiblePersonCreator() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String add() {
		responsiblePerson = new ResponsiblePerson();
		return "form.xhtml?faces-redirect=true";
		
	}
	
	public String edit(ResponsiblePerson responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
		return "form.xhtml?faces-redirect=true";
	}
	
	public String delete(ResponsiblePerson responsiblePerson) {
		service.remove(responsiblePerson);
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	
	public String cancel() {
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doSave() {
		if(responsiblePerson == null) responsiblePerson = new ResponsiblePerson();
		
		Calendar calendar = Calendar.getInstance();
		responsiblePerson.setDateCreated(calendar.getTime());
		responsiblePerson = responsiblePerson .getId() == null ? service.persist(responsiblePerson) : service.merge(responsiblePerson);
		
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public List<ResponsiblePerson> findResponsiblePerson(String query) {
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("id",responsiblePerson.getId(),InequalityConstants.NOT_EQUAL));
	    examples.add(new FilterExample(true,"fio","%"+query+"%",InequalityConstants.LIKE,true));
        examples.add(new FilterExample(true,"pin","%"+query+"%",InequalityConstants.LIKE,true));
        return service.findByExample(0, 20, SortEnum.ASCENDING, examples, "fio");
    }

	
	public ResponsiblePerson getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(ResponsiblePerson responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}
	
}
