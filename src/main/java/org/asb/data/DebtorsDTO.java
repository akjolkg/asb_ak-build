package org.asb.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.asb.enums.ClientStatus;



public class DebtorsDTO implements Serializable {

	private static final long serialVersionUID = -4913620347572155981L;
	private Integer id;
	private String object;
	private String fio;
	private String contacts;
	private String whatsapp;
	private Date date;
	private BigDecimal sum;
	private String note;
	private String curator;
	private String garant;
	private Integer curatorId;
	private Integer days;
	private BigDecimal sumInvent;
	private String curatorNote;
	private Date contractDate;
	private ClientStatus status;
	
	public DebtorsDTO(Integer id,String object, String fio, String contacts, String whatsapp, Date date, BigDecimal sum,
			String note, String curator, Integer curatorId, Integer days, String garant,BigDecimal sumInvent, String curatorNote, Date contractDate, Integer status) {
		super();
		this.id=id;
		this.object = object;
		this.fio = fio;
		this.contacts = contacts;
		this.whatsapp = whatsapp;
		this.date = date;
		this.sum = sum;
		this.note = note;
		this.curator = curator;
		this.curatorId = curatorId;
		this.days = days;
		this.garant=garant;
		this.sumInvent=sumInvent;
		this.curatorNote=curatorNote;
		this.contractDate=contractDate;
		this.status=ClientStatus.values()[status];
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getFio() {
		return fio;
	}
	public void setFio(String fio) {
		this.fio = fio;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getWhatsapp() {
		return whatsapp;
	}
	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public BigDecimal getSum() {
		return sum;
	}
	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCurator() {
		return curator;
	}
	public void setCurator(String curator) {
		this.curator = curator;
	}
	public Integer getCuratorId() {
		return curatorId;
	}
	public void setCuratorId(Integer curatorId) {
		this.curatorId = curatorId;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGarant() {
		return garant;
	}
	public void setGarant(String garant) {
		this.garant = garant;
	}
	public BigDecimal getSumInvent() {
		return sumInvent;
	}
	public void setSumInvent(BigDecimal sumInvent) {
		this.sumInvent = sumInvent;
	}
	public String getCuratorNote() {
		return curatorNote;
	}
	public void setCuratorNote(String curatorNote) {
		this.curatorNote = curatorNote;
	}
	public Date getContractDate() {
		return contractDate;
	}
	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
	public ClientStatus getStatus() {
		return status;
	}
	public void setStatus(ClientStatus status) {
		this.status = status;
	}
	
	
	
	
	
	
	
	
}
