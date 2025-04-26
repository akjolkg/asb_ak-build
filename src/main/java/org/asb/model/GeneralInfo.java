package org.asb.model;

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
@Table(name="general_info")
public class GeneralInfo extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private String programTitle;
	private String companyName;
	private String currency;
	private String subCurrency;
	
	
	public GeneralInfo() {}


	public String getProgramTitle() {
		return programTitle;
	}


	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public String getSubCurrency() {
		return subCurrency;
	}


	public void setSubCurrency(String subCurrency) {
		this.subCurrency = subCurrency;
	}
	
	
	
}
