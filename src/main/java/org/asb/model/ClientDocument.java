package org.asb.model;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.asb.enums.DocumentType;

/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="client_document")
public class ClientDocument extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private DocumentType documentType;
	private Attachment attachment;
	private Date date;
	private Denounce denounce;
	private Boolean global;
	private Client client;
	private String headerText;
	
	public ClientDocument() {}
	
		
	@ManyToOne
	@JoinColumn(name="client_id")
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client=client;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name="document_type")
	public DocumentType getDocumentType() {
		return documentType;
	}


	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
	
	@ManyToOne
	@JoinColumn(name="attachment_id")
	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@ManyToOne
	@JoinColumn(name="denounce_id")
	public Denounce getDenounce() {
		return denounce;
	}


	public void setDenounce(Denounce denounce) {
		this.denounce = denounce;
	}


	public Boolean getGlobal() {
		return global;
	}


	public void setGlobal(Boolean global) {
		this.global = global;
	}


	public String getHeaderText() {
		return headerText;
	}


	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

}
