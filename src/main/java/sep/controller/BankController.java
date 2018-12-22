package sep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import sep.dto.MerchantDTO;
import sep.dto.PaymentUrlIdDTO;
import sep.model.PaymentUrlAndId;
import sep.model.Request;
import sep.service.PaymentUrlAndIdService;
import sep.service.RequestService;

@RestController
@RequestMapping(value = "/bank")
public class BankController {
	
	@Autowired
	RequestService requestService;
	
	@Autowired
	PaymentUrlAndIdService paymentService;
	
	private static final Logger logger = LoggerFactory.getLogger(BankController.class);
	
	@CrossOrigin
	@RequestMapping(
			value = "/proveriZahtev",
			method = RequestMethod.POST
	)
	public PaymentUrlIdDTO proveriZahtev(@RequestBody MerchantDTO merchant) {
		System.out.println("\n\t\tDOSAO U BANKU\n");
		
		boolean isMerchantOk = false;
		RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
     
        try {
            System.out.println("\n\t\tProsleđujem zahtev za proveru merchant-a.\n");
             //step 2
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MerchantDTO> entity = new HttpEntity<>(merchant, headers);
            isMerchantOk = client.postForObject("https://localhost:9081/casopis/proveriMerchanta", entity,
                    Boolean.class);
            System.out.println("\n\t\tProvera da li je merchant ok: " + isMerchantOk + "\n");
        } catch (Exception e) {
            System.out.println("\n\t\tNe može da pošalje zahtev za proveru merchant-a.\n\n");
        }
        
        //ako je prodavac okej, generisi ovo
        if(isMerchantOk){
			PaymentUrlIdDTO retVal = new PaymentUrlIdDTO();
			PaymentUrlAndId toSave = new PaymentUrlAndId();
			toSave.setPaymentUrl("https://localhost:1235/api/defaultUrl");
			PaymentUrlAndId saved = paymentService.save(toSave);
			retVal.setPaymentId(saved.getId().intValue());
			retVal.setUrl("https://localhost:1235/payment/redirectToBankSite?paymentId=" + retVal.getPaymentId());
			saved.setPaymentUrl(retVal.getUrl());
			paymentService.save(saved);
			saveRequest(merchant,saved.getId());
			logger.info("\n\t\tMerchant je prošao proveru, moguće je nastaviti proces.\n");
			return retVal;
        }else{
        	logger.info("\n\t\tMerchant nije prošao proveru.\n");
        	return null;
        }      
	}
	
	public void saveRequest(MerchantDTO merchant,Long paymentId){
		Request toSave = new Request();
		toSave.setPaymentId(paymentId);
		toSave.setAmount(merchant.getAmount());
		toSave.setMerchantId(merchant.getMerchant_id());
		toSave.setMerchantOrderId(merchant.getMerchant_order_id());
		toSave.setMerchantPassword(merchant.getMerchant_password());
		toSave.setMerchantTimestamp(merchant.getMerchant_timestamp());
		toSave.setErrorUrl(merchant.getErrorUrl());
		toSave.setFailedUrl(merchant.getFailedUrl());
		toSave.setSuccessUrl(merchant.getSuccessUrl());
		requestService.save(toSave);
		return;
	}
	
}
