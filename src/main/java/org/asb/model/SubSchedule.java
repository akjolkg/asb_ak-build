package org.asb.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="sub_schedule")
public class SubSchedule extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private BigDecimal amountToPay;
	private Date datePayment;
	private String note;
	private ScheduleTemplate scheduleTemplate;
	
	public SubSchedule() {}
	

	@Column(name="amount_to_pay")
	public BigDecimal getAmountToPay() {
		return amountToPay;
	}


	public void setAmountToPay(BigDecimal amountToPay) {
		this.amountToPay = amountToPay;
	}

	
	@Column(name="date_payment")
	public Date getDatePayment() {
		return datePayment;
	}


	public void setDatePayment(Date datePayment) {
		this.datePayment = datePayment;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}

	@ManyToOne
	@JoinColumn(name="schedule_template_id")
	public ScheduleTemplate getScheduleTemplate() {
		return scheduleTemplate;
	}


	public void setScheduleTemplate(ScheduleTemplate scheduleTemplate) {
		this.scheduleTemplate = scheduleTemplate;
	}

	
	



}
