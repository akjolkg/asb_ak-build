package org.asb.model;

import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="comment_log")
public class CommentLog extends AbstractEntity<Integer> {

	private static final long serialVersionUID = -5693210060698452367L;
	private Appartment appartment;
	private String note;
	private String previousNote;
	private String fio;
	private Date dateCreated;
	private User user;
	
	public CommentLog() {}
	
		
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


	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}




	public Date getDateCreated() {
		return dateCreated;
	}


	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}


	

	public String getFio() {
		return fio;
	}


	public void setFio(String fio) {
		this.fio = fio;
	}


	@Column(length=200000)
	public String getPreviousNote() {
		return previousNote;
	}


	public void setPreviousNote(String previousNote) {
		this.previousNote = previousNote;
	}


	

}
