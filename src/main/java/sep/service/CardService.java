package sep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sep.model.Card;
import sep.repository.CardRepository;

@Service
public class CardService {
	
	@Autowired
	private CardRepository cardRepository;
	
	public List<Card> getAll(){
		return cardRepository.findAll();
	}
	
	public Card save(Card c) {
		return cardRepository.save(c);
	}
	
	
}
