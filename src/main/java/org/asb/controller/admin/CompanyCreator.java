package org.asb.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.asb.enums.Role;
import org.asb.enums.ScopeConstants;
import org.asb.model.Company;

import org.asb.service.CompanyService;

import org.asb.util.web.FacesScopeQualifier;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles=0)
public class CompanyCreator implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private Company company;
	
	@EJB
	private CompanyService service;
	
	
	@Inject
	private Conversation conversation;
	
	
	public CompanyCreator() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String add() {
		
		company=new Company();
		
		return "form.xhtml?faces-redirect=true";
		
	}
	
	public String edit(Company company) {
		this.company = company;
		return "form.xhtml?faces-redirect=true";
	}
	
	public String delete(Company company) {
		this.company = company;
		return "deleteCompany.xhtml?faces-redirect=true";
	}
	
	
	
	public String cancel() {
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doSave() {
		company = company .getId() == null ? service.persist(company) : service.merge(company);
		
		
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doDelete() {
	
		service.remove(company);
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
		
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
}
