package org.asb.model;

import java.util.Date;

import javax.persistence.*;

import org.asb.data.DebtorsDTO;
import org.asb.enums.ClientStatus;

import java.math.BigDecimal;

/**
 * 
 * @author Omorov Akzholbek
 *
 */

@SqlResultSetMapping(name = "DebtorsDTO", classes = {
	@ConstructorResult(targetClass = DebtorsDTO.class, 
			columns = {
					@ColumnResult(name="id", type = Integer.class),
					@ColumnResult(name="object", type = String.class),
			        @ColumnResult(name="fio", type = String.class),
			        @ColumnResult(name="contacts", type = String.class),
			        @ColumnResult(name="whatsapp", type = String.class),
			        @ColumnResult(name="date", type = Date.class),
			        @ColumnResult(name="sum", type = BigDecimal.class),
			        @ColumnResult(name="note", type = String.class),
			        @ColumnResult(name="curator", type = String.class),
			        @ColumnResult(name="curator_id", type = Integer.class),
			        @ColumnResult(name="days", type = Integer.class),
			        @ColumnResult(name="garant", type = String.class),
			        @ColumnResult(name="sum_invent", type = BigDecimal.class),
			        @ColumnResult(name="curator_note", type = String.class),
			        @ColumnResult(name="contract_date", type = Date.class),
			        @ColumnResult(name="status", type = Integer.class)
			        
			        
			                    
			 })
	})

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="schedule")
public class Schedule extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private Boolean active;
	private BigDecimal amountToPay;
	private BigDecimal leftAmount;
	private BigDecimal alreadyPayed;
	private Date datePayment;
	private Date factPayment;
	private Integer pkoNumber;
	private String note;
	
	private Client client;
	
	public Schedule() {}
	
		
	@ManyToOne
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

	

	@Column(name="already_payed")
	public BigDecimal getAlreadyPayed() {
		return alreadyPayed;
	}

	public void setAlreadyPayed(BigDecimal alreadyPayed) {
		this.alreadyPayed = alreadyPayed;
	}

	@Column(name="amount_to_pay")
	public BigDecimal getAmountToPay() {
		return amountToPay;
	}


	public void setAmountToPay(BigDecimal amountToPay) {
		this.amountToPay = amountToPay;
	}

	@Column(name="left_amount")
	public BigDecimal getLeftAmount() {
		return leftAmount;
	}


	public void setLeftAmount(BigDecimal leftAmount) {
		this.leftAmount = leftAmount;
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

	@Column(name="fact_payment")
	public Date getFactPayment() {
		return factPayment;
	}


	public void setFactPayment(Date factPayment) {
		this.factPayment = factPayment;
	}


	public Integer getPkoNumber() {
		return pkoNumber;
	}


	public void setPkoNumber(Integer pkoNumber) {
		this.pkoNumber = pkoNumber;
	}

	
	



}
