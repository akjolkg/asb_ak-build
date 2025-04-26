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
@Table(name="schedule_template")
public class ScheduleTemplate extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private Client client;
	private ClientStatus status=ClientStatus.NEW;
	private String note;
	private Date dateCreated;
	private String sendBackReason;
	private User user;
	private String history;
	
	public ScheduleTemplate() {}
	
	
	
	@Column(length=200000)
	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	@Column(name="send_back_reason",length=15000)
	public String getSendBackReason() {
		return sendBackReason;
	}


	public void setSendBackReason(String sendBackReason) {
		this.sendBackReason = sendBackReason;
	}
	
	public ClientStatus getStatus() {
		return status;
	}


	public void setStatus(ClientStatus status) {
		this.status = status;
	}

	public Date getDateCreated() {
		return dateCreated;
	}


	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@OneToOne
	@JoinColumn(name="client_id")
	public Client getClient() {
		return client;
	}


	public void setClient(Client client) {
		this.client = client;
	}


	@Column(length=2000000)
	public String getHistory() {
		return history;
	}



	public void setHistory(String history) {
		this.history = history;
	}

}
