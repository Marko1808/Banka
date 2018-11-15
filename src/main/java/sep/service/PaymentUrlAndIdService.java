package sep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sep.model.PaymentUrlAndId;
import sep.repository.PaymentUrlAndIdRepository;

@Service
public class PaymentUrlAndIdService {

	@Autowired
	private PaymentUrlAndIdRepository urlAndIdRepository;
	
	public PaymentUrlAndId save(PaymentUrlAndId toSave){
		return urlAndIdRepository.save(toSave);
	}
	public PaymentUrlAndId findById(Long id){
		return urlAndIdRepository.findOne(id);
	}
	
}
