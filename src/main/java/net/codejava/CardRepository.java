package net.codejava;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long> {
	@Query(value = "select * from card c where c.deck_id=?1", nativeQuery = true)
	List<Card> listAllByDeckId(Long deckId);

	@Query(value = "select * from card c where c.deck_id=?1 and c.learning_day%c.box_num=0",
			nativeQuery = true)
	List<Card> listAllForRepeatByDeckId(Long deckId);
}
