package sep.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Card {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "PAN")
    private Long pan;
	
	@Column(name = "SECURITY_CODE")
    private Long securityCode;
	
	@Column(name = "CARD_HOLDER_NAME")
    private String cardHolderName;
	
	@Column(name = "EXPIRATION_DATE")
    private String expirationDate;
	
	@Column(name = "MERCHANT_ID",nullable=true)
    private Long merchantId;
	
	@Column(name = "Amount")
    private BigDecimal amount;
	
	public Card() {
		
	}
	

	public Long getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPan() {
		return pan;
	}

	public void setPan(Long pan) {
		this.pan = pan;
	}

	public Long getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(Long securityCode) {
		this.securityCode = securityCode;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	
	
}
