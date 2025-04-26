package org.asb.model;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="construction")
public class Construction extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private String title;
	private String plannedAddress;
	private Date plannedDate;
	private Date realDate;
	private Date installmentDate;
	private String legalAddress;
	private Company company;
	private String code;
	private String foreman;
	private String toForeman;
	private Company developer;
	private Date salesStartDate;
	private BigDecimal notSaleArea;
	private BigDecimal technicalArea;
	private BigDecimal parkingNotSaleArea;
	private BigDecimal minApPrice;
	private BigDecimal minApPrice2;
	private BigDecimal minOfPrice;
	private BigDecimal minOfPriced;
	private BigDecimal minBaPrice;
	private BigDecimal minPaPrice;
	private BigDecimal minBaPrice2;
	private BigDecimal minOfPrice2;
	private BigDecimal minOfPriced2;
	private BigDecimal minPaPrice2;
	private String legalDocuments;
	private Set<String> fields=new HashSet<>();
	private Integer position;
	private String shortName;
	private String blockText;
	private Boolean cottage=false;
	private Integer specialType;
	private String city;
	
	
	private String note;
	
	private Set<Appartment> appartments;
	
	public Construction() {}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	

	@OneToMany(mappedBy="construction")
	public Set<Appartment> getAppartments() {
		return appartments;
	}
	
	public void setAppartments(Set<Appartment> appartments) {
		this.appartments = appartments;
	}
	
	@Override
	public String toString() {
		return title;
	}

	
	@ManyToOne()
	@JoinColumn(name="company_id")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Column(name="planned_address")
	public String getPlannedAddress() {
		return plannedAddress;
	}

	public void setPlannedAddress(String plannedAddress) {
		this.plannedAddress = plannedAddress;
	}

	@Column(name="legal_address")
	public String getLegalAddress() {
		return legalAddress;
	}

	public void setLegalAddress(String legalAddress) {
		this.legalAddress = legalAddress;
	}

	@Column(name="planned_date")
	@Temporal(TemporalType.DATE)
	public Date getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}

	@Column(name="installment_date")
	@Temporal(TemporalType.DATE)
	public Date getInstallmentDate() {
		return installmentDate;
	}

	public void setInstallmentDate(Date installmentDate) {
		this.installmentDate = installmentDate;
	}

	@Column(name="real_date")
	@Temporal(TemporalType.DATE)
	public Date getRealDate() {
		return realDate;
	}

	public void setRealDate(Date realDate) {
		this.realDate = realDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getForeman() {
		return foreman;
	}

	public void setForeman(String foreman) {
		this.foreman = foreman;
	}

	public String getToForeman() {
		return toForeman;
	}

	public void setToForeman(String toForeman) {
		this.toForeman = toForeman;
	}

	@ManyToOne()
	@JoinColumn(name="developer_id")
	public Company getDeveloper() {
		return developer;
	}

	public void setDeveloper(Company developer) {
		this.developer = developer;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name="sales_start_date")
	@Temporal(TemporalType.DATE)
	public Date getSalesStartDate() {
		return salesStartDate;
	}

	public void setSalesStartDate(Date salesStartDate) {
		this.salesStartDate = salesStartDate;
	}

	@Column(name="not_sale_area")
	public BigDecimal getNotSaleArea() {
		return notSaleArea;
	}

	public void setNotSaleArea(BigDecimal notSaleArea) {
		this.notSaleArea = notSaleArea;
	}


	@Column(name="technical_area")
	public BigDecimal getTechnicalArea() {
		return technicalArea;
	}

	public void setTechnicalArea(BigDecimal technicalArea) {
		this.technicalArea = technicalArea;
	}


	@Column(name="parking_not_sale_area")
	public BigDecimal getParkingNotSaleArea() {
		return parkingNotSaleArea;
	}

	public void setParkingNotSaleArea(BigDecimal parkingNotSaleArea) {
		this.parkingNotSaleArea = parkingNotSaleArea;
	}

	@Column(name="min_ap_price")
	public BigDecimal getMinApPrice() {
		return minApPrice;
	}

	public void setMinApPrice(BigDecimal minApPrice) {
		this.minApPrice = minApPrice;
	}

	@Column(name="min_of_price")
	public BigDecimal getMinOfPrice() {
		return minOfPrice;
	}

	public void setMinOfPrice(BigDecimal minOfPrice) {
		this.minOfPrice = minOfPrice;
	}

	@Column(name="min_pa_price")
	public BigDecimal getMinPaPrice() {
		return minPaPrice;
	}

	public void setMinPaPrice(BigDecimal minPaPrice) {
		this.minPaPrice = minPaPrice;
	}
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "construction_fields",
            joinColumns = @JoinColumn(name = "construction_id"))
	public Set<String> getFields() {
		return fields;
	}

	public void setFields(Set<String> fields) {
		this.fields = fields;
	}

	@Column(name="legal_documents")
	public String getLegalDocuments() {
		return legalDocuments;
	}

	public void setLegalDocuments(String legalDocuments) {
		this.legalDocuments = legalDocuments;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Column(name="short_name")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Column(name="min_ap_price2")
	public BigDecimal getMinApPrice2() {
		return minApPrice2;
	}

	public void setMinApPrice2(BigDecimal minApPrice2) {
		this.minApPrice2 = minApPrice2;
	}

	@Column(name="min_of_price2")
	public BigDecimal getMinOfPrice2() {
		return minOfPrice2;
	}

	public void setMinOfPrice2(BigDecimal minOfPrice2) {
		this.minOfPrice2 = minOfPrice2;
	}

	@Column(name="min_pa_price2")
	public BigDecimal getMinPaPrice2() {
		return minPaPrice2;
	}

	public void setMinPaPrice2(BigDecimal minPaPrice2) {
		this.minPaPrice2 = minPaPrice2;
	}

	@Column(name="block_text")
	public String getBlockText() {
		return blockText;
	}

	public void setBlockText(String blockText) {
		this.blockText = blockText;
	}

	public BigDecimal getMinBaPrice() {
		return minBaPrice;
	}

	public void setMinBaPrice(BigDecimal minBaPrice) {
		this.minBaPrice = minBaPrice;
	}

	public BigDecimal getMinBaPrice2() {
		return minBaPrice2;
	}

	public void setMinBaPrice2(BigDecimal minBaPrice2) {
		this.minBaPrice2 = minBaPrice2;
	}

	public Boolean isCottage() {
		return cottage;
	}

	public void setCottage(Boolean cottage) {
		this.cottage = cottage;
	}

	public BigDecimal getMinOfPriced() {
		return minOfPriced;
	}

	public void setMinOfPriced(BigDecimal minOfPriced) {
		this.minOfPriced = minOfPriced;
	}

	public BigDecimal getMinOfPriced2() {
		return minOfPriced2;
	}

	public void setMinOfPriced2(BigDecimal minOfPriced2) {
		this.minOfPriced2 = minOfPriced2;
	}

	public Integer getSpecialType() {
		return specialType;
	}

	public void setSpecialType(Integer specialType) {
		this.specialType = specialType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
