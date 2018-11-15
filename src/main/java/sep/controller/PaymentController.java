package sep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sep.model.PaymentUrlAndId;
import sep.service.PaymentUrlAndIdService;


@Controller
@RequestMapping(value = "/payment")
public class PaymentController {
	
	@Autowired
	PaymentUrlAndIdService urlAndIdService;
	
	@CrossOrigin
	@RequestMapping(
			value = "/redirectToBankSite/{id}",
			method = RequestMethod.POST
	)
	public void redirect(@PathVariable ("id") Integer id) {
		PaymentUrlAndId urlAndId = urlAndIdService.findById(new Long(id));
		System.out.println("Treba da ga prebacim na url: " + urlAndId.getPaymentUrl());
	}

}
