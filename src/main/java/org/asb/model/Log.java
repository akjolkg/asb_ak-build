package org.asb.model;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.asb.enums.LogType;
import org.asb.enums.ObjectType;

/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="log")
public class Log extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private LogType type;
	private ObjectType objectType;
	private String description;
	private Date dateCreated=new Date();
	private User user;
	private Integer objectId;
	
	public Log() {}
	
	@ManyToOne()
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	@Enumerated(EnumType.ORDINAL)
	public LogType getType() {
		return type;
	}


	public void setType(LogType type) {
		this.type = type;
	}

	@Enumerated(EnumType.ORDINAL)
	public ObjectType getObjectType() {
		return objectType;
	}


	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}


	@Column(length=25000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name="date_created")
	public Date getDateCreated() {
		return dateCreated;
	}


	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name="object_id")
	public Integer getObjectId() {
		return objectId;
	}

	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}

	
	



}
