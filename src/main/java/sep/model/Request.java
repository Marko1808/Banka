package sep.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.URL;

@Entity
public class Request {
	
	@Id
	@GeneratedValue
	@Column(name = "REQUEST_ID")
	private Long id;
	
	@Column(name = "MERCHANT_ORDER_ID")
    private Integer merchantOrderId;

    @Column(name = "MERCHANT_ID")
    private String merchantId;

    @Column(name = "MERCHANT_PASSWORD")
    private String merchantPassword;

    @Column(name = "MERCHANT_TIMESTAMP")
    private Timestamp merchantTimestamp;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @URL
    @Column(name = "SUCCESS_URL")
    private String successUrl;
    
    @URL
    @Column(name = "FAILED_URL")
    private String failedUrl;
    
    @URL
    @Column(name = "ERROR_URL")
    private String errorUrl;
    
    public Request(){
    	
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(Integer merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantPassword() {
		return merchantPassword;
	}

	public void setMerchantPassword(String merchantPassword) {
		this.merchantPassword = merchantPassword;
	}

	public Timestamp getMerchantTimestamp() {
		return merchantTimestamp;
	}

	public void setMerchantTimestamp(Timestamp merchantTimestamp) {
		this.merchantTimestamp = merchantTimestamp;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailedUrl() {
		return failedUrl;
	}

	public void setFailedUrl(String failedUrl) {
		this.failedUrl = failedUrl;
	}

	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}

}
