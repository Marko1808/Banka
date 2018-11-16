package sep.controller;

import java.util.List;

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

	@CrossOrigin
	@RequestMapping(value = "/redirectToBankSite/{id}", method = RequestMethod.POST)
	public void redirect(@PathVariable("id") Integer id) {
		PaymentUrlAndId urlAndId = urlAndIdService.findById(new Long(id));
		System.out.println("Treba da ga prebacim na url: " + urlAndId.getPaymentUrl());
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
				System.out.println("Postoji covek sa ovom platnom karticom");
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
					System.out.println("Iznos nakon transkacije na placenikovoj kartici:" + nakonTransakcije.getAmount());
					karticaMarchanta.setAmount(karticaMarchanta.getAmount().add(trazeni.getAmount()));
					Card nakonTransk2 = cardService.save(karticaMarchanta);
					System.out.println("Iznos nakon transkacije na merchantovoj kartici:" + nakonTransk2.getAmount());
					URLDTO url = new URLDTO();
					url.setUrl("http://localhost:1234/uspesno.html");
					return url;
				}else {
					URLDTO url = new URLDTO();
					url.setUrl("http://localhost:1234/greska.html");
					return url;
				}
			}else {
				URLDTO url = new URLDTO();
				url.setUrl("http://localhost:1234/greska.html");
				return url;
			}
			
		}else {
			URLDTO url = new URLDTO();
			url.setUrl("http://localhost:1234/greska.html");
			return url;
		}
		
	}

}
