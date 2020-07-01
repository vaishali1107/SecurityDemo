package com.psl.app.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="blacklisted_tokens")
public class BlackListedTokens {

	@Id
	@Column
	private String jti;
	
	@Column
	private Date expiryDate;
	
	

	public BlackListedTokens() {
		super();
	}

	public BlackListedTokens (String jti, Date expiryDate) {
		this.jti = jti;
		this.expiryDate = expiryDate;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
	
	
}
