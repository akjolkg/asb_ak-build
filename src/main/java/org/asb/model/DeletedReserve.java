package org.asb.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.asb.enums.ClientFrom;
import org.asb.enums.ClientStatus;
import org.asb.enums.ClientType;
import org.asb.enums.DenounceType;


/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="deleted_reserve")
public class DeletedReserve extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private Appartment appartment;
	private String note;
	private String history;
	private String headInfo;
	private String fio;
	private Date dateCreated;
	
	public DeletedReserve() {}
	
		
	@OneToOne
	@JoinColumn(name="appartment_id")
	public Appartment getAppartment() {
		return appartment;
	}
	
	public void setAppartment(Appartment appartment) {
		this.appartment=appartment;
	}
	
	@Column(length=200000)
	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	@Column(length=2000000)
	public String getHistory() {
		return history;
	}


	public void setHistory(String history) {
		this.history = history;
	}


	public Date getDateCreated() {
		return dateCreated;
	}


	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name="head_info",length=15000)
	public String getHeadInfo() {
		return headInfo;
	}


	public void setHeadInfo(String headInfo) {
		this.headInfo = headInfo;
	}


	public String getFio() {
		return fio;
	}


	public void setFio(String fio) {
		this.fio = fio;
	}



}
