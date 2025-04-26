package org.asb.controller;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.asb.annotation.RolesAllowed;
import org.asb.controller.user.UserController;
import org.asb.enums.ScopeConstants;
import org.asb.model.User;
import org.asb.service.UserService;
import org.asb.util.web.FacesScopeQualifier;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@RolesAllowed(roles={0,1,2,3,4,5,6,7,8,9,10})
public class PasswordChanger implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private String oldPassword;
	private String newPassword;
	private String newPassword2;
	
	@EJB
	private UserService service;
	
	@Inject
	private Conversation conversation;
	
	public PasswordChanger() {}
	
	@PostConstruct
	public void initialize() {
		if(conversation.isTransient()) conversation.begin();
	}
	
    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String change() {
		User user = new FacesScopeQualifier().getValue(UserController.CURRENT_USER_SESSION_KEY, ScopeConstants.SESSION_SCOPE);
		return "/view/user/change_password.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		closeConversation();
		return "/view/main.xhtml?faces-redirect=true";
	}
	
	public String doChange() throws NoSuchAlgorithmException {
		User user = new FacesScopeQualifier().getValue(UserController.CURRENT_USER_SESSION_KEY, ScopeConstants.SESSION_SCOPE);
		
		if(oldPassword == null || newPassword == null || !user.getPassword().equals(oldPassword)){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Старый пароль неверный!!!","Старый пароль неверный!!!" ));
			return null;
		}
		
		user.setPassword(newPassword);
		user = service.merge(user);
		
		closeConversation();
		
		return "/view/main.xhtml?faces-redirect=true";
	}
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}
	
}
