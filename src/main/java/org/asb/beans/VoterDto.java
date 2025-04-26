package org.asb.beans;

import java.util.Date;

public class VoterDto {
	
	private Integer id;
	private Integer objectId;
	private Integer number;
	private Integer uikId;
	private Integer status;
	private String pin;
	private String firstname;
	private String surname;
	private String patronymic;
	private String passportNumber;
	private String address;
	private String contacts;
	private Date birthdate;
	private Integer vtype;
	private Integer agitatorId;
	private Integer agitatorObjectId;
	private Integer etype;
	
	
	public VoterDto() {
		
	}
	
	public VoterDto(Integer id,Integer objectId, Integer number, Integer uikId, Integer status, String pin,
			String firstname, String surname, String patronymic, String passportNumber, String address, String contacts,
			Date birthdate, Integer vtype,Integer agitatorId,Integer agitatorObjectId,Integer etype) {
		super();
		this.id=id;
		this.objectId = objectId;
		this.number = number;
		this.uikId = uikId;
		this.status = status;
		this.pin = pin;
		this.firstname = firstname;
		this.surname = surname;
		this.patronymic = patronymic;
		this.passportNumber = passportNumber;
		this.address = address;
		this.contacts = contacts;
		this.birthdate = birthdate;
		this.vtype = vtype;
		this.agitatorId=agitatorId;
		this.agitatorObjectId=agitatorObjectId;
		this.etype=etype;
		
	}



	public Integer getObjectId() {
		return objectId;
	}
	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getUikId() {
		return uikId;
	}
	public void setUikId(Integer uikId) {
		this.uikId = uikId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPatronymic() {
		return patronymic;
	}
	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}
	public String getPassportNumber() {
		return passportNumber;
	}
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public Integer getVtype() {
		return vtype;
	}
	public void setVtype(Integer vtype) {
		this.vtype = vtype;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAgitatorId() {
		return agitatorId;
	}

	public void setAgitatorId(Integer agitatorId) {
		this.agitatorId = agitatorId;
	}

	public Integer getAgitatorObjectId() {
		return agitatorObjectId;
	}

	public void setAgitatorObjectId(Integer agitatorObjectId) {
		this.agitatorObjectId = agitatorObjectId;
	}

	public Integer getEtype() {
		return etype;
	}

	public void setEtype(Integer etype) {
		this.etype = etype;
	}
	
	
}
