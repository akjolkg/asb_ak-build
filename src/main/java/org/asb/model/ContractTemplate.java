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

import org.asb.enums.ClientFizYur;
import org.asb.enums.ContractTemplateType;
import org.asb.enums.DocumentType;

/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="contract_template")
public class ContractTemplate extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private ContractTemplateType type;
	private Attachment attachment;
	private Date date;
	private Construction construction;
	private ClientFizYur fizYur;
	
	public ContractTemplate() {}	
	
		
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
	public ContractTemplateType getType() {
		return type;
	}


	public void setType(ContractTemplateType type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name="construction_id")
	public Construction getConstruction() {
		return construction;
	}


	public void setConstruction(Construction construction) {
		this.construction = construction;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="fiz_yur")
	public ClientFizYur getFizYur() {
		return fizYur;
	}


	public void setFizYur(ClientFizYur fizYur) {
		this.fizYur = fizYur;
	}

}
