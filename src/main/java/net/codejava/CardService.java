package net.codejava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CardService {

	@Autowired
	private CardRepository repo;
	
	public List<Card> listAll() {
		return repo.findAll();
	}
	
	public void save(Card card) {
		repo.save(card);
	}
	
	public Card get(long id) {
		return repo.findById(id).get();
	}
	
	public void delete(long id) {
		repo.deleteById(id);
	}
}
