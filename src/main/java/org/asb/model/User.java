package org.asb.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.asb.enums.Role;
import org.asb.enums.UserStatus;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User extends AbstractEntity<Integer>  {
	private static final long serialVersionUID = 1L;
	private String fio;
	private String pin;
	private String password;
	private Role role;
	private String username;
	private UserStatus status;
	private String contacts;
	private Date dateCreated;
	private Integer reserveAmount;
	private Set<String> fields=new HashSet<>();
	private Set<Construction> constructions;
	private Integer red;
	private Integer green;
	private Integer blue;
	private String lastLogin;
	private String shortFio;
	
	public User() {
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Enumerated(EnumType.ORDINAL)
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Enumerated(EnumType.ORDINAL)
	public UserStatus getStatus() {
		return status;
	}
	
	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}	

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	@Column(name="date_created")
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_fields",
            joinColumns = @JoinColumn(name = "user_id"))
	public Set<String> getFields() {
		return fields;
	}

	public void setFields(Set<String> fields) {
		this.fields = fields;
	}

	public Integer getReserveAmount() {
		return reserveAmount;
	}

	public void setReserveAmount(Integer reserveAmount) {
		this.reserveAmount = reserveAmount;
	}

	@ManyToMany(cascade = {CascadeType.REMOVE},fetch = FetchType.EAGER)
	@JoinTable(name = "user_constructions",
    joinColumns = {@JoinColumn(name = "user_id")},
    		  inverseJoinColumns = {@JoinColumn(name = "construction_id")})
	public Set<Construction> getConstructions() {
		return constructions;
	}

	public void setConstructions(Set<Construction> constructions) {
		this.constructions = constructions;
	}

	@Transient
	public Integer getRed() {
		return red;
	}

	public void setRed(Integer red) {
		this.red = red;
	}

	@Transient
	public Integer getGreen() {
		return green;
	}

	public void setGreen(Integer green) {
		this.green = green;
	}

	@Transient
	public Integer getBlue() {
		return blue;
	}

	public void setBlue(Integer blue) {
		this.blue = blue;
	}

	@Transient
	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Transient
	public String getShortFio() {
		return shortFio;
	}

	public void setShortFio(String shortFio) {
		this.shortFio = shortFio;
	}
	
	
}