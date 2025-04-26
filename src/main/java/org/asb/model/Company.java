package org.asb.model;


import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="company")
public class Company extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private String title;
	private String titleLong;
	private String inn;
	private String okpo;
	private String postAddress;
	private String bankName;
	private String accountNumber;
	private String bik;
	private String phone;
	private String currency;
	private String subCurrency;
	private String webSite;
	private String responsiblePerson;
	private String responsiblePersonLong;
	private String responsiblePersonIs;
	private String address;
	private String director;
	private String toDirector;
	private String finDirector;
	private String finDirectorIs;
	private String lawyer;
	private String toLawyer;
	private String procuration;
	private Boolean main;
	private Boolean developer;
	private String city;
	private String shortName;
	private String psdFio;
	private String legalInfo;
	
	
	
	private Set<Construction> constructions;
	
	public Company() {}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	

	
	@OneToMany(mappedBy="company", cascade={CascadeType.REFRESH}, orphanRemoval=true)
	public Set<Construction> getConstructions() {
		return constructions;
	}
	
	public void setConstructions(Set<Construction> constructions) {
		this.constructions = constructions;
	}

	
	@Override
	public String toString() {
		return title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name="responsible_person")
	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getOkpo() {
		return okpo;
	}

	public void setOkpo(String okpo) {
		this.okpo = okpo;
	}

	@Column(name="post_address")
	public String getPostAddress() {
		return postAddress;
	}

	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	@Column(name="bank_name")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name="account_number")
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBik() {
		return bik;
	}

	public void setBik(String bik) {
		this.bik = bik;
	}

	@Column(name="web_site")
	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	@Column(name="sub_currency")
	public String getSubCurrency() {
		return subCurrency;
	}

	public void setSubCurrency(String subCurrency) {
		this.subCurrency = subCurrency;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getToDirector() {
		return toDirector;
	}

	public void setToDirector(String toDirector) {
		this.toDirector = toDirector;
	}

	public String getResponsiblePersonIs() {
		return responsiblePersonIs;
	}

	public void setResponsiblePersonIs(String responsiblePersonIs) {
		this.responsiblePersonIs = responsiblePersonIs;
	}

	public String getFinDirector() {
		return finDirector;
	}

	public void setFinDirector(String finDirector) {
		this.finDirector = finDirector;
	}

	public String getFinDirectorIs() {
		return finDirectorIs;
	}

	public void setFinDirectorIs(String finDirectorIs) {
		this.finDirectorIs = finDirectorIs;
	}

	public String getToLawyer() {
		return toLawyer;
	}

	public void setToLawyer(String toLawyer) {
		this.toLawyer = toLawyer;
	}

	public String getLawyer() {
		return lawyer;
	}

	public void setLawyer(String lawyer) {
		this.lawyer = lawyer;
	}

	public String getResponsiblePersonLong() {
		return responsiblePersonLong;
	}

	public void setResponsiblePersonLong(String responsiblePersonLong) {
		this.responsiblePersonLong = responsiblePersonLong;
	}

	public String getProcuration() {
		return procuration;
	}

	public void setProcuration(String procuration) {
		this.procuration = procuration;
	}

	public Boolean getMain() {
		return main;
	}

	public void setMain(Boolean main) {
		this.main = main;
	}

	public Boolean getDeveloper() {
		return developer;
	}

	public void setDeveloper(Boolean developer) {
		this.developer = developer;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="short_name")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPsdFio() {
		return psdFio;
	}

	public void setPsdFio(String psdFio) {
		this.psdFio = psdFio;
	}

	public String getLegalInfo() {
		return legalInfo;
	}

	public void setLegalInfo(String legalInfo) {
		this.legalInfo = legalInfo;
	}

	public String getTitleLong() {
		return titleLong;
	}

	public void setTitleLong(String titleLong) {
		this.titleLong = titleLong;
	}

}
