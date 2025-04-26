package org.asb.model;

import java.math.BigDecimal;
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
import org.asb.enums.ContractType;
import org.asb.enums.DenounceType;


/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="denounce")
public class Denounce extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private Appartment appartment;
	private DenounceType type;
	private ClientStatus status=ClientStatus.NEW;
	private String note;
	private String history;
	private String headInfo;
	private String fio;
	private Date dateCreated;
	private Date dateDenounce;
	private Date factDenounce;
	private String sendBackReason;
	private User user;
	private BigDecimal totalSum;
	private Date contractDate;
	private ClientFrom contractType;
	private ClientType clientType;
	private BigDecimal payedAmount;
	
	
	public Denounce() {}
	
		
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


	@Column(name="send_back_reason",length=15000)
	public String getSendBackReason() {
		return sendBackReason;
	}


	public void setSendBackReason(String sendBackReason) {
		this.sendBackReason = sendBackReason;
	}


	public DenounceType getType() {
		return type;
	}


	public void setType(DenounceType type) {
		this.type = type;
	}


	
	public ClientStatus getStatus() {
		return status;
	}


	public void setStatus(ClientStatus status) {
		this.status = status;
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


	public Date getDateDenounce() {
		return dateDenounce;
	}


	public void setDateDenounce(Date dateDenounce) {
		this.dateDenounce = dateDenounce;
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


	public Date getFactDenounce() {
		return factDenounce;
	}


	public void setFactDenounce(Date factDenounce) {
		this.factDenounce = factDenounce;
	}


	
	public BigDecimal getTotalSum() {
		return totalSum;
	}


	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}


	public Date getContractDate() {
		return contractDate;
	}


	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}


	public ClientFrom getContractType() {
		return contractType;
	}


	public void setContractType(ClientFrom contractType) {
		this.contractType = contractType;
	}


	public ClientType getClientType() {
		return clientType;
	}


	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}


	public BigDecimal getPayedAmount() {
		return payedAmount;
	}


	public void setPayedAmount(BigDecimal payedAmount) {
		this.payedAmount = payedAmount;
	}

	public String toLog() {
		String s="";
		s=s+getFio()+" "+getNote();
		return s;
	}
	
}
