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
import org.asb.enums.DocumentTemplateType;
import org.asb.model.Attachment;
import org.asb.model.DocumentTemplate;

import org.asb.service.DocumentTemplateService;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles = 0)
public class DocumentTemplateCreator implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private DocumentTemplate documentTemplate;

	@EJB
	private DocumentTemplateService service;

	@Inject
	private Conversation conversation;
	@Inject
	private FileUploadController fileUploadController;

	public DocumentTemplateCreator() {
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

		documentTemplate = new DocumentTemplate();

		return "doc-form.xhtml?faces-redirect=true";

	}

	public String edit(DocumentTemplate documentTemplate) {
		this.documentTemplate = documentTemplate;
		List<Attachment> attachments = new ArrayList<Attachment>();
		attachments.add(documentTemplate.getAttachment());
		fileUploadController.setFiles(attachments);
		return "doc-form.xhtml?faces-redirect=true";
	}

	public String delete(DocumentTemplate documentTemplate) {
		this.documentTemplate = documentTemplate;
		return "deleteDocumentTemplate.xhtml?faces-redirect=true";
	}

	public String cancel() {
		closeConversation();
		return "doc-list.xhtml?faces-redirect=true";
	}

	public String doSave() {

		Calendar calendar = new GregorianCalendar();

		documentTemplate.setDate(calendar.getTime());

		documentTemplate = documentTemplate.getId() == null ? service.persist(documentTemplate)
				: service.merge(documentTemplate);

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
			documentTemplate.setAttachment((Attachment) attachments.toArray()[0]);
			service.merge(documentTemplate);
		}
		return "doc-list.xhtml?faces-redirect=true";
	}

	public String doDelete(DocumentTemplate documentTemplate) {
		this.documentTemplate = documentTemplate;
		service.remove(documentTemplate);
		closeConversation();
		return "doc-list.xhtml?faces-redirect=true";
	}

	public DocumentTemplate getDocumentTemplate() {
		return documentTemplate;
	}

	public void setDocumentTemplate(DocumentTemplate documentTemplate) {
		this.documentTemplate = documentTemplate;
	}

	public DocumentTemplateType[] getAllDocumentTemplateTypes() {
		return DocumentTemplateType.values();
	}

}
