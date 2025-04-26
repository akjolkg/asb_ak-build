package org.asb.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.asb.enums.Role;
import org.asb.enums.UserStatus;
import org.asb.model.Construction;
import org.asb.model.User;
import org.asb.service.ConstructionService;
import org.asb.service.UserService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles=0)
public class UserCreator implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private User user; 
	private List<Role> roles;
	private List<Construction>constructions;
	private List<Construction> selectedConstructions;
	
	@EJB
	private UserService service;
	@EJB
	private ConstructionService constructionService;
	
	
	@Inject
	private Conversation conversation;
	
	
	public UserCreator() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String add() {
		user = new User();
		return "form.xhtml?faces-redirect=true";
		
	}
	
	public String edit(User user) {
		this.user = user;
		selectedConstructions=new ArrayList<>();
		selectedConstructions.addAll(user.getConstructions());
		
		System.out.println("user constructions="+user.getConstructions());
		
		return "form.xhtml?faces-redirect=true";
	}
	
	public String delete(User user) {
		service.remove(user);
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	
	public String block(User user) {
		user.setStatus(UserStatus.INACTIVE);
		service.merge(user);		
		return "list.xhtml?faces-redirect=true";
	}
	public String unblock(User user) {
		user.setStatus(UserStatus.ACTIVE);
		service.merge(user);
		return "list.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doSave() {
		if(user == null) user = new User();
		
		Calendar calendar = Calendar.getInstance();
		user.setDateCreated(calendar.getTime());
	
		user.setStatus(UserStatus.ACTIVE);	
		if(selectedConstructions!=null && !selectedConstructions.isEmpty()) {
			user.setConstructions(new HashSet());
			user.getConstructions().addAll(selectedConstructions);
		}else {
			user.setConstructions(null);
		}
		
		System.out.println("user constructions22222="+user.getConstructions());
		user = user .getId() == null ? service.persist(user) : service.merge(user);
		
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public List<User> findUser(String query) {
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("id",user.getId(),InequalityConstants.NOT_EQUAL));
		examples.add(new FilterExample("role",user.getRole(),InequalityConstants.EQUAL));
        examples.add(new FilterExample(true,"fio","%"+query+"%",InequalityConstants.LIKE,true));
        examples.add(new FilterExample(true,"pin","%"+query+"%",InequalityConstants.LIKE,true));
        return service.findByExample(0, 20, SortEnum.ASCENDING, examples, "fio");
    }

	public List<Role> getRoles() {
		if(roles==null){
			roles=new ArrayList<>();			
			for(Role r:Role.values()) {
				if(	!r.equals(Role.ADMIN_FILIAL)&&
					!r.equals(Role.FILIAL)&&
					!r.equals(Role.KASSA_FILIAL))
					roles.add(r);
			}			
		}
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Construction> getConstructions() {
		if(constructions==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			constructions=constructionService.findByExample(0, 10000, SortEnum.ASCENDING, examples, "title");
		}
		return constructions;
	}

	public void setConstructions(List<Construction> constructions) {
		this.constructions = constructions;
	}

	public List<Construction> getSelectedConstructions() {
		return selectedConstructions;
	}

	public void setSelectedConstructions(List<Construction> selectedConstructions) {
		this.selectedConstructions = selectedConstructions;
	}
	
}
