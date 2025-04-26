package org.asb.beans;

public class UserDto {
	private Integer id;
	private Integer objectId;
	private String fio;
	private String pin;
	private String password;
	private Integer role;
	private String username;
	private Integer status;
	private Integer parentId;
	private Integer parentObjectId;
	private Integer uikId;
	private Integer gtype;
	private String contacts;
	
	public UserDto(){
	
	}
	
	
	public UserDto(Integer id,Integer objectId, String fio, String pin, String contacts, String password, Integer role, String username,
			Integer status, Integer parentId,Integer parentObjectId, Integer uikId, Integer gtype) {
		super();
		this.id=id;
		this.objectId = objectId;
		this.fio = fio;
		this.pin = pin;
		this.contacts=contacts;
		this.password = password;
		this.role = role;
		this.username = username;
		this.status = status;
		this.parentId = parentId;
		this.parentObjectId = parentObjectId;
		this.uikId = uikId;
		this.gtype = gtype;
	}
	public Integer getObjectId() {
		return objectId;
	}
	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getUikId() {
		return uikId;
	}
	public void setUikId(Integer uikId) {
		this.uikId = uikId;
	}
	public Integer getGtype() {
		return gtype;
	}
	public void setGtype(Integer gtype) {
		this.gtype = gtype;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getContacts() {
		return contacts;
	}


	public void setContacts(String contacts) {
		this.contacts = contacts;
	}


	public Integer getParentObjectId() {
		return parentObjectId;
	}


	public void setParentObjectId(Integer parentObjectId) {
		this.parentObjectId = parentObjectId;
	}	
	
}
