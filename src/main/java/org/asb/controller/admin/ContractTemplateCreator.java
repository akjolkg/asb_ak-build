package org.asb.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.asb.enums.ContractTemplateType;
import org.asb.model.Attachment;
import org.asb.model.ContractTemplate;

import org.asb.service.ContractTemplateService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles = 0)
public class ContractTemplateCreator implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private ContractTemplate contractTemplate;

	@EJB
	private ContractTemplateService service;

	@Inject
	private Conversation conversation;
	@Inject
	private FileUploadController fileUploadController;

	public ContractTemplateCreator() {
	}

	@PostConstruct
	public void initialize() {
		if (conversation.isTransient())
			conversation.begin();
	}

	public void closeConversation() {
		if (!conversation.isTransient())
			conversation.end();
	}

	public String add() {

		contractTemplate = new ContractTemplate();

		return "form.xhtml?faces-redirect=true";

	}

	public String edit(ContractTemplate contractTemplate) {
		this.contractTemplate = contractTemplate;
		List<Attachment> attachments = new ArrayList<Attachment>();
		attachments.add(contractTemplate.getAttachment());
		fileUploadController.setFiles(attachments);
		return "form.xhtml?faces-redirect=true";
	}

	public String delete(ContractTemplate contractTemplate) {
		this.contractTemplate = contractTemplate;
		return "deleteContractTemplate.xhtml?faces-redirect=true";
	}

	public String cancel() {
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}

	public String doSave() {

		Calendar calendar = new GregorianCalendar();

		contractTemplate.setDate(calendar.getTime());

		contractTemplate = contractTemplate.getId() == null ? service.persist(contractTemplate)
				: service.merge(contractTemplate);

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
		if (!attachments.isEmpty() && attachments.size() > 0) {
			contractTemplate.setAttachment((Attachment) attachments.toArray()[0]);
			service.merge(contractTemplate);
		}
		return "list.xhtml?faces-redirect=true";
	}

	public String doDelete() {

		service.remove(contractTemplate);
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}

	public ContractTemplate getContractTemplate() {
		return contractTemplate;
	}

	public void setContractTemplate(ContractTemplate contractTemplate) {
		this.contractTemplate = contractTemplate;
	}

	public ContractTemplateType[] getAllContractTemplateTypes() {
		return ContractTemplateType.values();
	}

}
