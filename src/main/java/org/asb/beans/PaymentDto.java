package org.asb.beans;

public class PaymentDto {
	private Integer id;
	private Integer objectId;
	private Integer amountVoter;
	private Integer amountGetter;
	private Integer kassirId;
	private Integer kassirObjectId;
	private Integer passportId;
	private Integer passportObjectId;
	private Integer voterId;
	private Integer voterObjectId;
	
	public PaymentDto(){
	
	}
	
	
	public PaymentDto(Integer id, Integer objectId, Integer amountVoter, Integer amountGetter, Integer kassirId,
			Integer kassirObjectId, Integer passportId, Integer passportObjectId,Integer voterId, Integer voterObjectId) {
		super();
		this.id = id;
		this.objectId = objectId;
		this.amountVoter = amountVoter;
		this.amountGetter = amountGetter;
		this.kassirId = kassirId;
		this.kassirObjectId = kassirObjectId;
		this.passportId = passportId;
		this.passportObjectId = passportObjectId;
		this.voterId=voterId;
		this.voterObjectId=voterObjectId;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getObjectId() {
		return objectId;
	}
	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}
	public Integer getAmountVoter() {
		return amountVoter;
	}
	public void setAmountVoter(Integer amountVoter) {
		this.amountVoter = amountVoter;
	}
	public Integer getAmountGetter() {
		return amountGetter;
	}
	public void setAmountGetter(Integer amountGetter) {
		this.amountGetter = amountGetter;
	}
	public Integer getKassirId() {
		return kassirId;
	}
	public void setKassirId(Integer kassirId) {
		this.kassirId = kassirId;
	}
	public Integer getKassirObjectId() {
		return kassirObjectId;
	}
	public void setKassirObjectId(Integer kassirObjectId) {
		this.kassirObjectId = kassirObjectId;
	}
	public Integer getPassportId() {
		return passportId;
	}
	public void setPassportId(Integer passportId) {
		this.passportId = passportId;
	}
	public Integer getPassportObjectId() {
		return passportObjectId;
	}
	public void setPassportObjectId(Integer passportObjectId) {
		this.passportObjectId = passportObjectId;
	}


	public Integer getVoterId() {
		return voterId;
	}


	public void setVoterId(Integer voterId) {
		this.voterId = voterId;
	}

	public Integer getVoterObjectId() {
		return voterObjectId;
	}


	public void setVoterObjectId(Integer voterObjectId) {
		this.voterObjectId = voterObjectId;
	}
	
	
	
}
