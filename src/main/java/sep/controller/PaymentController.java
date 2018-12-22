package sep.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sep.dto.CardDTO;
import sep.dto.URLDTO;
import sep.model.Card;
import sep.model.PaymentUrlAndId;
import sep.model.Request;
import sep.service.CardService;
import sep.service.PaymentUrlAndIdService;
import sep.service.RequestService;

@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

	@Autowired
	PaymentUrlAndIdService urlAndIdService;

	@Autowired
	private CardService cardService;
	
	@Autowired
	private RequestService requestService;
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@CrossOrigin
	@RequestMapping(value = "/redirectToBankSite/{id}", method = RequestMethod.POST)
	public void redirect(@PathVariable("id") Integer id) {
		PaymentUrlAndId urlAndId = urlAndIdService.findById(new Long(id));
		System.out.println("\n\t\tTreba da ga prebacim na url: " + urlAndId.getPaymentUrl() + "\n");
		logger.info("\n\t\tRedirekcija na sajt banke:\n" + urlAndId.getPaymentUrl() + "\n");
	}
	
	@CrossOrigin
	@RequestMapping(
			value = "/findCard",
			method = RequestMethod.POST
	)
	public CardDTO findCard(@RequestBody CardDTO card) {
		Long paymentId = new Long(card.getPaymentId());
		List<Request> requestList = requestService.getAll();
		Request trazeni = new Request();
		
		for(Request req : requestList) {
			if(req.getPaymentId().compareTo(paymentId)==0) {
				trazeni = req;
			}
		}
		
		List<Card> listaKartica = cardService.getAll();
		Card karticaMarchanta = new Card();
		Long idMerchanta = new Long(trazeni.getMerchantId());
		
		for(Card c : listaKartica) {
			if(c.getMerchantId()!=null && c.getMerchantId().compareTo(idMerchanta)==0) {
				karticaMarchanta = c;
			}
		}
		
		CardDTO cardToReturn = new CardDTO();
		cardToReturn.setPan(karticaMarchanta.getPan().toString());
		
		logger.info("\n\t\tVlasnik kartice: " + cardToReturn.getCardHolderName() + "\n");
		return cardToReturn;
	}
	
	@CrossOrigin
	@RequestMapping(
			value = "/autentifikacijaUsera",
			method = RequestMethod.POST
	)
	public Boolean autentifikacijaUsera(@RequestBody CardDTO card) {
		List<Card> listaKartica = cardService.getAll();
		Long pan = new Long(card.getPan());
		Long securityCode = new Long(card.getSecurityCode());
		Boolean postojiKartica = false;
		Card karticaPlacenika = new Card();
		Long paymentId = new Long(card.getPaymentId());
		List<Request> requestList = requestService.getAll();
		Request trazeni = new Request();
		
		for(Request req : requestList) {
			if(req.getPaymentId().compareTo(paymentId)==0) {
				trazeni = req;
			}
		}
		
		for(Card c : listaKartica) {
			if(card.getCardHolderName().equals(c.getCardHolderName()) && 
					card.getExpirationDate().equals(c.getExpirationDate()) &&
							pan.compareTo(c.getPan())==0 && securityCode.compareTo(c.getSecurityCode())==0
							) {
				System.out.println("\n\t\tPostoji čovek sa ovom platnom karticom.\n");
				postojiKartica = true;
				karticaPlacenika = c;
			}
		}
		
		if(postojiKartica) {		
			if(trazeni.getId()!=null) {
				if(karticaPlacenika.getAmount().compareTo(trazeni.getAmount())>0) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
			
		}else {
			return false;
		}
	}

	@CrossOrigin
	@RequestMapping(
			value = "/izvrsiPlacanje",
			method = RequestMethod.POST
	)
	public URLDTO izvrsiPlacanje(@RequestBody CardDTO card) {
		List<Card> listaKartica = cardService.getAll();
		Long pan = new Long(card.getPan());
		Long securityCode = new Long(card.getSecurityCode());
		Boolean postojiKartica = false;
		Card karticaPlacenika = new Card();
		Card karticaMarchanta = new Card();
		Long paymentId = new Long(card.getPaymentId());
		List<Request> requestList = requestService.getAll();
		Request trazeni = new Request();
		
		for(Request req : requestList) {
			if(req.getPaymentId().compareTo(paymentId)==0) {
				trazeni = req;
			}
		}
		
		Long idMerchanta = new Long(trazeni.getMerchantId());
		for(Card c : listaKartica) {
			if(card.getCardHolderName().equals(c.getCardHolderName()) && 
					card.getExpirationDate().equals(c.getExpirationDate()) &&
							pan.compareTo(c.getPan())==0 && securityCode.compareTo(c.getSecurityCode())==0
							) {
				System.out.println("\n\t\tPostoji čovek sa ovom platnom karticom.\n");
				postojiKartica = true;
				karticaPlacenika = c;
			}if(c.getMerchantId()!=null && c.getMerchantId().compareTo(idMerchanta)==0) {
				karticaMarchanta = c;
			}
		}
		
		if(postojiKartica) {	
			if(trazeni.getId()!=null) {
				if(karticaPlacenika.getAmount().compareTo(trazeni.getAmount())>0) {
					karticaPlacenika.setAmount(karticaPlacenika.getAmount().subtract(trazeni.getAmount()));
					Card nakonTransakcije = cardService.save(karticaPlacenika);
					System.out.println("\n\t\tIznos nakon transkacije na plaćenikovoj kartici: " + nakonTransakcije.getAmount() + "\n");
					karticaMarchanta.setAmount(karticaMarchanta.getAmount().add(trazeni.getAmount()));
					Card nakonTransk2 = cardService.save(karticaMarchanta);
					System.out.println("\n\t\tIznos nakon transkacije na merchant-ovoj kartici: " + nakonTransk2.getAmount() + "\n");
					URLDTO url = new URLDTO();
					url.setUrl("https://localhost:9081/uspesno.html");
					
					logger.info("Izvršeno plaćanje: " + url + "\n");
					return url;
				}else {
					URLDTO url = new URLDTO();
					url.setUrl("https://localhost:9081/greska.html");
					
					logger.info("Greška prilikom pokušaja plaćanja!");
					return url;
				}
			}else {
				URLDTO url = new URLDTO();
				url.setUrl("https://localhost:9081/greska.html");
				
				logger.info("Greška prilikom pokušaja plaćanja!");
				return url;
			}
			
		}else {
			URLDTO url = new URLDTO();
			url.setUrl("https://localhost:9081/greska.html");
			
			logger.info("Greška prilikom pokušaja plaćanja!");
			return url;
		}
	}

}
