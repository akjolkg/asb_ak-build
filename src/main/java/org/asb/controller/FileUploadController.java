package org.asb.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext;  
  
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.transaction.SystemException;

import org.apache.commons.io.IOUtils;
import org.asb.model.Attachment;
import org.asb.service.AttachmentService;
import org.asb.util.web.Messages;
import org.primefaces.event.FileUploadEvent;  
import org.primefaces.model.UploadedFile;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
public class FileUploadController implements Serializable { 
	
	private static final long serialVersionUID = -388416034659956860L;
	private List<Attachment> files = new ArrayList<Attachment>();
	private List<Attachment> removedFiles = new ArrayList<Attachment>();
	@Inject
	private Conversation conversation;
	@EJB
	private AttachmentService service;
	private int max = 20;
    
    public void initialize() {
		if(conversation.isTransient()) conversation.begin();
	}
	
    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
    
    public void handleFileUpload(FileUploadEvent event) throws IOException { 
    	if(conversation.isTransient()) conversation.begin();
    	
    	if(files.size() >= max) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Messages.getMessage("fileLimitMessage")));  
            return;
    	}
    	
    	System.out.println("uploaded file name is " + event.getFile().getFileName());
    	String fileName = event.getFile().getFileName();
    	files.add(createFileBinary(event.getFile()));
    	System.out.println("uploaded file name is 2 " + fileName);

        FacesMessage msg = new FacesMessage(Messages.getMessage("fileSuccessfullyUploaded").replaceAll("\\{0\\}", fileName));  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
    
	public Set<Attachment> convert() throws NamingException, SystemException {
		Set<Attachment> attachments = new HashSet<Attachment>();
		
		for (Attachment binary : files) {
			if(binary == null) continue;
			binary = binary.getId() == null ? service.persist(binary) : binary;
			attachments.add(binary);
		}
    	files.clear();
		return attachments;
	}
	
	
	@SuppressWarnings("unchecked")
	public void assertRemovedFiles() {
		if(removedFiles.isEmpty()) return;
		for (Attachment attachment : removedFiles) {
			service.remove(attachment);
		}
		
		removedFiles.clear();
	}
	
	private Attachment createFileBinary(UploadedFile file) throws IOException {
		System.out.println(file);
    	Attachment binary = new Attachment();
		binary.setFileName(file.getFileName());
		binary.setContentType(file.getContentType());
		binary.setData(IOUtils.toByteArray(file.getInputstream()));
		
		return binary;
	}
	
	public void remove(Attachment attachment) {
		
		synchronized(files){
			files.remove(attachment);
		}
		removedFiles.add(attachment);
	}

	public List<Attachment> getFiles() {
		return files;
	}
    
    public void setFiles(List<Attachment> files) {
		this.files = files;
	}
    
    public int getMax() {
		return max;
	}
    
    public void setMax(int max) {
		this.max = max;
	}
}  
