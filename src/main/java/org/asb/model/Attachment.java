package org.asb.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Entity
@Cacheable(false)
@Access(AccessType.PROPERTY)
@Table(name="attachment")
public class Attachment extends AbstractEntity<Integer> {

	private String fileName;
	private String contentType;
	private byte[] data;
	private static final long serialVersionUID = -7813632797036582032L;
	
	public Attachment() {}
	
	@Column(name="file_name")
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name="content_type")
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Basic(fetch=FetchType.LAZY)
	@Column(name="data")
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}

}
