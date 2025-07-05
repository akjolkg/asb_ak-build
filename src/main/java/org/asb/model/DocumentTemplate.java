package org.asb.model;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.asb.enums.DocumentTemplateType;

/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="document_template")
public class DocumentTemplate extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private DocumentTemplateType type;
	private Attachment attachment;
	private Date date;	
	
	public DocumentTemplate() {}	
	
		
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
	
	@Enumerated(EnumType.ORDINAL)
	public DocumentTemplateType getType() {
		return type;
	}


	public void setType(DocumentTemplateType type) {
		this.type = type;
	}

}
