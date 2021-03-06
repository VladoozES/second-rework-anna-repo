package net.codejava;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long> {
	@Query(value = "select * from card c where c.deck_id=?1", nativeQuery = true)
	List<Card> listAllByDeckId(Long deckId);

	@Query(value = "select * from card c where c.deck_id=?1 and c.learning_day%c.box_num=0",
			nativeQuery = true)
	List<Card> listAllForRepeatByDeckId(Long deckId);
	
	@Query(value = "select box_num from card c where c.card_id=?1 limit 1", nativeQuery = true)
	int getBoxNumOnId(Long cardIf);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "update test_spring_db.card c set c.box_num = :newBoxNum where c.card_id= :cardId", nativeQuery = true)
	void saveReplanUpdates(@Param("cardId") Long cardId, @Param("newBoxNum") int newBoxNum);
}
