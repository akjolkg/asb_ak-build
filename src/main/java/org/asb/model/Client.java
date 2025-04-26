package org.asb.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jms.Message;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.asb.enums.AppartmentType;
import org.asb.enums.ClientFizYur;
import org.asb.enums.ClientFrom;
import org.asb.enums.ClientStatus;
import org.asb.enums.ClientType;
import org.asb.enums.ContractType;
import org.asb.enums.Loyality;
import org.asb.util.web.Messages;


/**
 * 
 * @author Omorov Akzholbek
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="client")
public class Client extends AbstractEntity<Integer> {

	

	private static final long serialVersionUID = 7182171865249674881L;
	private String fio;	
	private Boolean active;
	private BigDecimal priceForSquare;
	private BigDecimal totalSum;
	private BigDecimal alreadyPayed;
	private BigDecimal notPayedYet;
	private BigDecimal reserveAmount;
	private Date dateContract;
	private Date dateInstallment;
	private Date dateLegalSign;
	private String sendBackReason;
	private String uptFio;
	private Loyality loyality;
	private Loyality loyalityOrk;

	
	private Date dateFirstPayment;
	private String contractNumber;
	private String passportNumber;
	private String passportAddress;
	private String factAddress;
	private String contacts;
	private String whatsappNumber;
	private Boolean maritalStatus=false;
	private Boolean uptStatus=false;
	private String spouseFio;
	private String crmNumber;
	
	private Appartment appartment;
	private Boolean installment;
	private Integer monthInstallment;
	private BigDecimal perMonthPayment;
	private BigDecimal firstPayment;
	private Attachment contract;
	private String note;
	private String curatorNote;
	private String curatorOrkNote;
	private String finDirNote;
	private String controllerNote;
	private Set<Schedule> schedules = new HashSet<Schedule>();
	private Set<ClientDocument> documents = new HashSet<ClientDocument>();
	private Set<Payment> payments = new HashSet<Payment>();
	private ClientType type;
	private ContractType contractType;
	private ClientStatus status;
	private ClientFrom clientFrom;
	private Boolean extralow;
	private ResponsiblePerson fromPerson;
	
	private Attachment attachment;
	private String creator;
	private User curator;
	private User curatorOrk;
	private Boolean keys=false;
	private Boolean signed=false;
	private User user;
	private String basis;
	private Garant garant;
	private String pin;
	private Boolean prepay;
	private String extraContractText;
	
	private Date reserveDate;
	
	private Date dateReserveExpire;
	private Date plannedDate;
	private Date birthdate;
	private Date dateCreated=new Date();
	private Date updatedPlannedDate;
	
	private ClientFizYur fizYur=ClientFizYur.FIZ;
	
	
	
	public Client() {}
	
		
	@OneToOne
	@JoinColumn(name="appartment_id")
	public Appartment getAppartment() {
		return appartment;
	}
	
	public void setAppartment(Appartment appartment) {
		this.appartment=appartment;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}

	
	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	@Column(name="price_for_square")
	public BigDecimal getPriceForSquare() {
		return priceForSquare;
	}

	public void setPriceForSquare(BigDecimal priceForSquare) {
		this.priceForSquare = priceForSquare;
	}
	
	@Column(name="total_sum")
	public BigDecimal getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}
	@Column(name="already_payed")
	public BigDecimal getAlreadyPayed() {
		return alreadyPayed;
	}

	public void setAlreadyPayed(BigDecimal alreadyPayed) {
		this.alreadyPayed = alreadyPayed;
	}
	@Column(name="not_payed_yet")
	public BigDecimal getNotPayedYet() {
		return notPayedYet;
	}

	public void setNotPayedYet(BigDecimal notPayedYet) {
		this.notPayedYet = notPayedYet;
	}
	@Column(name="date_contract")
	public Date getDateContract() {
		return dateContract;
	}

	public void setDateContract(Date dateContract) {
		this.dateContract = dateContract;
	}
	@Column(name="contract_number")
	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}


	public Boolean getInstallment() {
		return installment;
	}


	public void setInstallment(Boolean installment) {
		this.installment = installment;
	}

	@Column(name="mont_installment")
	public Integer getMonthInstallment() {
		return monthInstallment;
	}


	public void setMonthInstallment(Integer monthInstallment) {
		this.monthInstallment = monthInstallment;
	}

	@Column(name="per_month_payment")
	public BigDecimal getPerMonthPayment() {
		return perMonthPayment;
	}


	public void setPerMonthPayment(BigDecimal perMonthPayment) {
		this.perMonthPayment = perMonthPayment;
	}

	@Column(name="date_first_payment")
	public Date getDateFirstPayment() {
		return dateFirstPayment;
	}


	public void setDateFirstPayment(Date dateFirstPayment) {
		this.dateFirstPayment = dateFirstPayment;
	}

	@Column(name="passport_number")
	public String getPassportNumber() {
		return passportNumber;
	}


	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	@Column(name="passport_address")
	public String getPassportAddress() {
		return passportAddress;
	}


	public void setPassportAddress(String passportAddress) {
		this.passportAddress = passportAddress;
	}

	@Column(name="fact_address")
	public String getFactAddress() {
		return factAddress;
	}


	public void setFactAddress(String factAddress) {
		this.factAddress = factAddress;
	}
	
	@OneToMany(mappedBy="client", cascade={CascadeType.REMOVE}, orphanRemoval=true)
	public Set<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(Set<Schedule> schedules) {
		this.schedules = schedules;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}

	@OneToMany(mappedBy="client", cascade={CascadeType.REMOVE}, orphanRemoval=true)
	public Set<Payment> getPayments() {
		return payments;
	}


	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	@Column(name="first_payment")
	public BigDecimal getFirstPayment() {
		return firstPayment;
	}


	public void setFirstPayment(BigDecimal firstPayment) {
		this.firstPayment = firstPayment;
	}

	
	public ClientType getType() {
		return type;
	}


	public void setType(ClientType type) {
		this.type = type;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="attachment_id")
	public Attachment getAttachment() {
		return attachment;
	}


	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}


	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}

	@ManyToOne
	@JoinColumn(name="curator_id")
	public User getCurator() {
		return curator;
	}


	public void setCurator(User curator) {
		this.curator = curator;
	}

	@Column(name="got_keys")
	public Boolean getKeys() {
		return keys;
	}


	public void setKeys(Boolean keys) {
		this.keys = keys;
	}


	public Boolean getSigned() {
		return signed;
	}


	public void setSigned(Boolean signed) {
		this.signed = signed;
	}

	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String getBasis() {
		return basis;
	}


	public void setBasis(String basis) {
		this.basis = basis;
	}

	@Column(name="whatsapp_number")
	public String getWhatsappNumber() {
		return whatsappNumber;
	}


	public void setWhatsappNumber(String whatsappNumber) {
		this.whatsappNumber = whatsappNumber;
	}

	@Column(name="marital_status")
	public Boolean getMaritalStatus() {
		return maritalStatus;
	}


	public void setMaritalStatus(Boolean maritalStatus) {
		this.maritalStatus = maritalStatus;
	}


	@Column(name="spouse_fio")
	public String getSpouseFio() {
		return spouseFio;
	}


	public void setSpouseFio(String spouseFio) {
		this.spouseFio = spouseFio;
	}

	@Column(name="date_installment")
	public Date getDateInstallment() {
		return dateInstallment;
	}


	public void setDateInstallment(Date dateInstallment) {
		this.dateInstallment = dateInstallment;
	}


	public ClientStatus getStatus() {
		return status;
	}


	public void setStatus(ClientStatus status) {
		this.status = status;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="contract_attachment_id")
	public Attachment getContract() {
		return contract;
	}


	public void setContract(Attachment contract) {
		this.contract = contract;
	}

	@OneToMany(mappedBy="client", cascade={CascadeType.REMOVE}, orphanRemoval=true)
	public Set<ClientDocument> getDocuments() {
		return documents;
	}


	public void setDocuments(Set<ClientDocument> documents) {
		this.documents = documents;
	}

	@Column(name="client_from")
	public ClientFrom getClientFrom() {
		return clientFrom;
	}


	public void setClientFrom(ClientFrom clientFrom) {
		this.clientFrom = clientFrom;
	}

	@Column(name="send_back_reason",length=15000)
	public String getSendBackReason() {
		return sendBackReason;
	}


	public void setSendBackReason(String sendBackReason) {
		this.sendBackReason = sendBackReason;
	}

	@Column(name="curator_note")
	public String getCuratorNote() {
		return curatorNote;
	}


	public void setCuratorNote(String curatorNote) {
		this.curatorNote = curatorNote;
	}

	@ManyToOne
	@JoinColumn(name="responsible_person_id")
	public ResponsiblePerson getFromPerson() {
		return fromPerson;
	}


	public void setFromPerson(ResponsiblePerson fromPerson) {
		this.fromPerson = fromPerson;
	}


	public String getCrmNumber() {
		return crmNumber;
	}


	public void setCrmNumber(String crmNumber) {
		this.crmNumber = crmNumber;
	}

	@ManyToOne
	@JoinColumn(name="garant_id")
	public Garant getGarant() {
		return garant;
	}


	public void setGarant(Garant garant) {
		this.garant = garant;
	}


	public String getControllerNote() {
		return controllerNote;
	}


	public void setControllerNote(String controllerNote) {
		this.controllerNote = controllerNote;
	}


	public String getFinDirNote() {
		return finDirNote;
	}


	public void setFinDirNote(String finDirNote) {
		this.finDirNote = finDirNote;
	}


	public Boolean getExtralow() {
		return extralow;
	}


	public void setExtralow(Boolean extralow) {
		this.extralow = extralow;
	}
	
	@Transient
	public String toLog() {
		String buffer="Клиент (";
		if(fio!=null)
			buffer=buffer+"ФИО: "+fio;
		if(priceForSquare!=null)
			buffer=buffer+", Цена за м.кв: "+priceForSquare;
		if(totalSum!=null)
			buffer=buffer+", Сумма договора: "+totalSum;
		if(dateContract!=null)
			buffer=buffer+", Дата договора: "+dateContract;
		if(contractNumber!=null)
			buffer=buffer+", Номер договора: "+contractNumber;
		if(note!=null)
			buffer=buffer+", Примечание: "+note;
		if(firstPayment!=null)
			buffer=buffer+", Первоначальный взнос: "+firstPayment;
		if(passportNumber!=null)
			buffer=buffer+", Паспортные данные: "+passportNumber;
		if(passportAddress!=null)
			buffer=buffer+", Адрес по паспорту: "+passportAddress;
		if(curator!=null)
			buffer=buffer+", Куратор: "+curator.getFio();
		if(loyality!=null)
			buffer=buffer+", Лояльность клиента: "+Messages.getEnumMessage(loyality.toString());
		if(type!=null)
			buffer=buffer+", Тип сделки: "+Messages.getEnumMessage(type.toString());
		
		if(contractType!=null)
			buffer=buffer+", Тип договора: "+Messages.getEnumMessage(contractType.toString());
		
		if(clientFrom!=null)
			buffer=buffer+", Тип клиента: "+Messages.getEnumMessage(clientFrom.toString());
		if(fromPerson!=null)
			buffer=buffer+", От кого: "+fromPerson;
		
		if(keys!=null)
			buffer=buffer+", Ключи получил: "+keys;
		if(signed!=null)
			buffer=buffer+", Оформился: "+signed;
		
		
		if(appartment!=null) {
			if(appartment.getType().equals(AppartmentType.APPARTMENT))
				buffer=buffer+", Квартира (";
			if(appartment.getType().equals(AppartmentType.OFFICE))
				buffer=buffer+", Помещение (";
			if(appartment.getType().equals(AppartmentType.PARKING))
				buffer=buffer+", Паркинг (";
			buffer=buffer+"ID:"+appartment.getId()+", Номер:"+appartment.getTitle()+ ", Строительный объект: (ID:"+appartment.getConstruction().getId()+", Наименование:"+appartment.getConstruction().getTitle()+"))";
		}
		buffer=buffer+")";
		return buffer;
		
		
	}

	@Column(name="contract_type")
	public ContractType getContractType() {
		return contractType;
	}


	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	@ManyToOne
	@JoinColumn(name="curator_ork_id")
	public User getCuratorOrk() {
		return curatorOrk;
	}


	public void setCuratorOrk(User curatorOrk) {
		this.curatorOrk = curatorOrk;
	}

	@Column(name="curator_ork_note", length=150000)
	public String getCuratorOrkNote() {
		return curatorOrkNote;
	}


	public void setCuratorOrkNote(String curatorOrkNote) {
		this.curatorOrkNote = curatorOrkNote;
	}


	public String getPin() {
		return pin;
	}


	public void setPin(String pin) {
		this.pin = pin;
	}


	@Column(name="date_legal_sign")
	public Date getDateLegalSign() {
		return dateLegalSign;
	}


	public void setDateLegalSign(Date dateLegalSign) {
		this.dateLegalSign = dateLegalSign;
	}


	@Column(name="date_reserve_expire")
	public Date getDateReserveExpire() {
		return dateReserveExpire;
	}


	public void setDateReserveExpire(Date dateReserveExpire) {
		this.dateReserveExpire = dateReserveExpire;
	}

	@Column(name="upt_status")
	public Boolean getUptStatus() {
		return uptStatus;
	}


	public void setUptStatus(Boolean uptStatus) {
		this.uptStatus = uptStatus;
	}

	@Column(name="upt_fio")
	public String getUptFio() {
		return uptFio;
	}


	public void setUptFio(String uptFio) {
		this.uptFio = uptFio;
	}


	public Boolean getPrepay() {
		return prepay;
	}


	public void setPrepay(Boolean prepay) {
		this.prepay = prepay;
	}


	@Column(name="reserve_date")
	public Date getReserveDate() {
		return reserveDate;
	}


	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}

	@Column(name="reserve_amount")
	public BigDecimal getReserveAmount() {
		return reserveAmount;
	}


	public void setReserveAmount(BigDecimal reserveAmount) {
		this.reserveAmount = reserveAmount;
	}


	@Column(name="planned_date")
	public Date getPlannedDate() {
		return plannedDate;
	}


	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}


	@Column(name="fiz_yur")
	@Enumerated(EnumType.ORDINAL)
	public ClientFizYur getFizYur() {
		return fizYur;
	}


	public void setFizYur(ClientFizYur fizYur) {
		this.fizYur = fizYur;
	}

	@Column(name="extra_contract_text")
	public String getExtraContractText() {
		return extraContractText;
	}


	public void setExtraContractText(String extraContractText) {
		this.extraContractText = extraContractText;
	}


	public Loyality getLoyality() {
		return loyality;
	}


	public void setLoyality(Loyality loyality) {
		this.loyality = loyality;
	}

	@Column(name="loyality_ork")
	public Loyality getLoyalityOrk() {
		return loyalityOrk;
	}


	public void setLoyalityOrk(Loyality loyalityOrk) {
		this.loyalityOrk = loyalityOrk;
	}


	public Date getDateCreated() {
		return dateCreated;
	}


	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}


	public Date getUpdatedPlannedDate() {
		return updatedPlannedDate;
	}


	public void setUpdatedPlannedDate(Date updatedPlannedDate) {
		this.updatedPlannedDate = updatedPlannedDate;
	}


	public Date getBirthdate() {
		return birthdate;
	}


	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

}
