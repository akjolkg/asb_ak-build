package org.asb.model;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.asb.enums.AppartmentType;
import org.asb.enums.OfficeType;
import org.asb.enums.PlanType;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="appartment")
public class Appartment extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 7182171865249674881L;
	private String title;
	private String docNumber;
	private Integer roomQuantity;
	private String blockNumber;
	private Integer flat;
	private Integer entranceNumber;
	private Integer appartmentEntranceNumber;
	private AppartmentType type;
	private OfficeType officeType;
	private PlanType ptype=PlanType.PROJECT;
	private BigDecimal docTotalArea;
	private BigDecimal totalArea;
	private Construction construction;
	private Client client;
	private String specNote;
	public Appartment() {}
	

	
	public Integer getFlat() {
		return flat;
	}

	public void setFlat(Integer flat) {
		this.flat = flat;
	}
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@ManyToOne
	@JoinColumn(name="construction")
	public Construction getConstruction() {
		return construction;
	}
	
	public void setConstruction(Construction construction) {
		this.construction = construction;
	}
	
	@Column(name="room_quantity")
	public Integer getRoomQuantity() {
		return roomQuantity;
	}

	public void setRoomQuantity(Integer roomQuantity) {
		this.roomQuantity = roomQuantity;
	}
	
	@Column(name="total_area")
	public BigDecimal getTotalArea() {
		return totalArea;
	}

	public void setTotalArea(BigDecimal totalArea) {
		this.totalArea = totalArea;
	}

	@OneToOne(mappedBy="appartment", cascade={CascadeType.REFRESH}, orphanRemoval=true)
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Enumerated(EnumType.ORDINAL)
	public AppartmentType getType() {
		return type;
	}

	public void setType(AppartmentType type) {
		this.type = type;
	}

	@Column(name="doc_number")
	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	@Column(name="doc_total_area")
	public BigDecimal getDocTotalArea() {
		return docTotalArea;
	}

	public void setDocTotalArea(BigDecimal docTotalArea) {
		this.docTotalArea = docTotalArea;
	}


	@Column(name="block_number")
	public String getBlockNumber() {
		return blockNumber;
	}



	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}


	@Column(name="entrance_number")
	public Integer getEntranceNumber() {
		return entranceNumber;
	}



	public void setEntranceNumber(Integer entranceNumber) {
		this.entranceNumber = entranceNumber;
	}


	@Column(name="appartment_entrance_number")
	public Integer getAppartmentEntranceNumber() {
		return appartmentEntranceNumber;
	}



	public void setAppartmentEntranceNumber(Integer appartmentEntranceNumber) {
		this.appartmentEntranceNumber = appartmentEntranceNumber;
	}
	
	@Transient
	public String toLog() {
		String buffer="";
		if(type.equals(AppartmentType.APPARTMENT))
			buffer=buffer+"Квартира (";
		if(type.equals(AppartmentType.OFFICE))
			buffer=buffer+"Помещение (";
		if(type.equals(AppartmentType.PARKING))
			buffer=buffer+"Паркинг (";
		if(id!=null)
			buffer=buffer+"ID:"+id;
		buffer=buffer+", Номер:"+title;
		if(roomQuantity!=null)
			buffer=buffer+", Количество комнат:"+roomQuantity;
		if(blockNumber!=null)
			buffer=buffer+", Номер блока:"+blockNumber;
		if(flat!=null)
			buffer=buffer+", Этаж:"+flat;
		if(entranceNumber!=null)
			buffer=buffer+", Подъезд:"+entranceNumber;
		if(appartmentEntranceNumber!=null)
			buffer=buffer+", Квартира на площадке:"+appartmentEntranceNumber;
		if(totalArea!=null)
			buffer=buffer+", Площадь:"+totalArea;
		if(docNumber!=null)
			buffer=buffer+", Номер согласно техпаспорту:"+docNumber;
		if(docTotalArea!=null)
			buffer=buffer+", Площадь по техпаспорту :"+docTotalArea;
		if(construction!=null)
			buffer=buffer+", Строительный объект: (ID:"+construction.getId()+", Наименование:"+construction.getTitle()+")";
		buffer=buffer+")";
		return buffer;
		
		
	}
	
	@Column(name="spec_note")
	public String getSpecNote() {
		return specNote;
	}



	public void setSpecNote(String specNote) {
		this.specNote = specNote;
	}


	@Enumerated(EnumType.ORDINAL)
	public PlanType getPtype() {
		return ptype;
	}



	public void setPtype(PlanType ptype) {
		this.ptype = ptype;
	}


	@Enumerated(EnumType.ORDINAL)
	@Column(name="office_type")
	public OfficeType getOfficeType() {
		return officeType;
	}



	public void setOfficeType(OfficeType officeType) {
		this.officeType = officeType;
	}
}
