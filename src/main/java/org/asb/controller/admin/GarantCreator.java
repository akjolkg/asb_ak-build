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
import org.asb.model.Garant;
import org.asb.service.GarantService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles=5)
public class GarantCreator implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private Garant garant; 
	@EJB
	private GarantService service;
	@Inject
	private Conversation conversation;
	
	
	public GarantCreator() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String add() {
		garant = new Garant();
		return "form.xhtml?faces-redirect=true";
		
	}
	
	public String edit(Garant garant) {
		this.garant = garant;
		return "form.xhtml?faces-redirect=true";
	}
	
	public String delete(Garant garant) {
		service.remove(garant);
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	
	public String cancel() {
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doSave() {
		if(garant == null) garant = new Garant();
		
		Calendar calendar = Calendar.getInstance();
		garant.setDateCreated(calendar.getTime());
		garant = garant .getId() == null ? service.persist(garant) : service.merge(garant);
		
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public List<Garant> findGarant(String query) {
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("id",garant.getId(),InequalityConstants.NOT_EQUAL));
	    examples.add(new FilterExample(true,"fio","%"+query+"%",InequalityConstants.LIKE,true));
        examples.add(new FilterExample(true,"pin","%"+query+"%",InequalityConstants.LIKE,true));
        return service.findByExample(0, 20, SortEnum.ASCENDING, examples, "fio");
    }

	
	public Garant getGarant() {
		return garant;
	}

	public void setGarant(Garant garant) {
		this.garant = garant;
	}
	
}
