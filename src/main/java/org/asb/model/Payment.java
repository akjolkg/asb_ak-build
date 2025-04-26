package org.asb.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.asb.enums.PaymentType;

/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="payment")
public class Payment extends AbstractEntity<Integer> {

	


	private static final long serialVersionUID = 7182171865249674881L;
	private Boolean active;
	private BigDecimal paymentAmount;
	private Date datePayment;
	private Integer checkNumber;	
	private Client client; 
	private String note;
	private Boolean firstPayment=false;
	private User user;
	private Boolean check;
	private PaymentType paymentType;
	private BigDecimal reserveAmount;
	
	
	private Boolean withoutClient;
	
	public Payment() {}
	
		
	@ManyToOne()
	@JoinColumn(name="client_id")
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client=client;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}

	

	



	@Column(name="date_payment")
	public Date getDatePayment() {
		return datePayment;
	}


	public void setDatePayment(Date datePayment) {
		this.datePayment = datePayment;
	}


	

	@Column(name="payment_amount")
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}


	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	@Column(name="check_number_integer")
	public Integer getCheckNumber() {
		return checkNumber;
	}


	public void setCheckNumber(Integer checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Column(name="first_payment")
	public Boolean getFirstPayment() {
		return firstPayment;
	}


	public void setFirstPayment(Boolean firstPayment) {
		this.firstPayment = firstPayment;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}

	@ManyToOne()
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	
	
	public String toLog() {
		String buffer=" Платёж (";
		buffer=buffer+"Клиент (ID:"+client.getId()+", ФИО:"+client.getFio()+", Номер объекта:"+client.getAppartment().getTitle()+", Строительный объект:"+client.getAppartment().getConstruction().getTitle()+")";
		
		if(paymentAmount!=null)
			buffer=buffer+", Сумма оплаты:"+paymentAmount;
		if(datePayment!=null)
			buffer=buffer+", Дата оплаты:"+datePayment;
		if(checkNumber!=null)
			buffer=buffer+", Номер ПКО:"+checkNumber;
		if(note!=null)
			buffer=buffer+", Примечание:"+note;
		if(firstPayment!=null)
			buffer=buffer+", Первый взнос:"+firstPayment;
		if(user!=null)
			buffer=buffer+", Пользователь:"+user.getFio();
		
		return buffer;
	}

	@Column(name="checked")
	public Boolean getCheck() {
		return check;
	}


	public void setCheck(Boolean check) {
		this.check = check;
	}


	@Transient
	public Boolean getWithoutClient() {
		return withoutClient;
	}


	public void setWithoutClient(Boolean withoutClient) {
		this.withoutClient = withoutClient;
	}


	public PaymentType getPaymentType() {
		return paymentType;
	}


	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	
	@Column(name="reserve_amount")
	public BigDecimal getReserveAmount() {
		return reserveAmount;
	}


	public void setReserveAmount(BigDecimal reserveAmount) {
		this.reserveAmount = reserveAmount;
	}


}
