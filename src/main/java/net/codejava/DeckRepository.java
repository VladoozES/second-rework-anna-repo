package net.codejava;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeckRepository extends JpaRepository<Deck, Long> {
	@Query(value = "select * from deck d where d.deck_id=?1", nativeQuery = true)
	List<Deck> listAllByUser(Long userId);
}
