package org.asb.controller;

import java.io.ByteArrayInputStream;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.asb.model.Attachment;
import org.asb.model.Client;
import org.asb.service.ClientService;
import org.asb.util.Translit;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


@RequestScoped
@ManagedBean
public class DownloadController {
	@EJB
	private ClientService service;
	
	public DefaultStreamedContent download(Attachment attachment){
		return new DefaultStreamedContent(new ByteArrayInputStream(attachment.getData()));
	}
	public StreamedContent downloadClientContract(Client client){
		client=service.getByIdWithFields(client.getId(), new String[]{"contract"});
		if(client.getContract()==null) return null;
		Attachment attachment=client.getContract();
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		StreamedContent file=new DefaultStreamedContent(new ByteArrayInputStream(attachment.getData()),
				externalContext.getMimeType(Translit.translit(attachment.getFileName())),
				Translit.translit(attachment.getFileName()));
		return file;
	}
	
	public StreamedContent downloadFile(Attachment attachment){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		StreamedContent file=new DefaultStreamedContent(new ByteArrayInputStream(attachment.getData()),
				externalContext.getMimeType(Translit.translit(attachment.getFileName())),
				Translit.translit(attachment.getFileName()));
		return file;
	}
	
	
	
}
