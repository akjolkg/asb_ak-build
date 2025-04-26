package org.asb.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.asb.annotation.Logged;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.model.ClientDocument;
import org.asb.model.DeletedReserve;
import org.asb.service.ClientDocumentService;
import org.asb.service.DeletedReserveService;
import org.primefaces.event.SelectEvent;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
public class DeletedReserveController implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private DeletedReserve deletedReserve;
	private List<ClientDocument> documents;
	
	@EJB
	private ClientDocumentService documentService;
	@EJB
	private DeletedReserveService ddService;
	
	@Inject
	private Conversation conversation;
	
	
	public DeletedReserveController() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String cancel() {
		closeConversation();
		return "list.xhtml"+"?faces-redirect=true";
	}
	
	public void onRowSelect(SelectEvent event) throws IOException {
		deletedReserve=(DeletedReserve) event.getObject();
		
		List<FilterExample> examples=new ArrayList<>();
		examples.add(new FilterExample("deletedReserve",deletedReserve,InequalityConstants.EQUAL));
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/deletedReserve/card.xhtml?cid="+conversation.getId());
	}
	
	
	public DeletedReserve getDeletedReserve() {
		return deletedReserve;
	}

	public void setDeletedReserve(DeletedReserve deletedReserve) {
		this.deletedReserve = deletedReserve;
	}

	public List<ClientDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<ClientDocument> documents) {
		this.documents = documents;
	}
	
}
