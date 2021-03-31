package net.codejava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

	@Autowired
	private DeckService deckService;
	@Autowired
	private CardService cardService;
	@Autowired
	private UserService userService;
		
	@RequestMapping("/")
	public String viewHomePage(Model model) { //вывод таблицы
		List<User> listUsers = userService.listAll();
		model.addAttribute("listUsers", listUsers);
		
		return "index";
	}
	
	@RequestMapping("/{userId}/decks/{deckId}/cards")
	public String viewDeckCards(@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId, Model model) {
		List<Card> listCards = cardService.listAllByDeckId(deckId);
		model.addAttribute("listCards", listCards);
		
		return "deck_cards";
	}
	
	@RequestMapping("/{userId}/decks/{deckId}/cards/repeat")
	public String viewDeckCardsRepeat(@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId, Model model) {
		List<Card> repeatListCards = cardService.listAllForRepeatByDeckId(deckId);
		model.addAttribute("repeatListCards", repeatListCards);
		
		return "repeat_cards";
	}
	
	@RequestMapping("/{userId}/decks")
	public String viewUserDecks(@PathVariable(name = "userId") Long userId, Model model) {
		List<Deck> listDecks = deckService.listAllByUser(userId);
		model.addAttribute("listDecks", listDecks);
		model.addAttribute("userId", userId);
		
		return "user_decks";
	}
	
	@RequestMapping("/{userId}/decks/new")
	public String viewDeckCreate(@PathVariable(name = "userId") Long userId, Model model) {
		Deck deck = new Deck();
		deck.setAuthorId(userId);
		model.addAttribute("deck", deck);
		
		return "deck_create";
	}
	
	@RequestMapping("/{userId}/decks/{deckId}/cards/new")
	public String viewCardCreate(@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId, Model model) {
		Card card = new Card();
		card.setDeckId(deckId);
		model.addAttribute("card", card);
		
		return "card_create";
	}
	
	/*@RequestMapping("/new")
	public String showNewUserPage(Model model) { //создание нового продукта
		Product product = new Product();
		model.addAttribute("product", product);
		
		return "new_product";
	}*/
	
	@RequestMapping(value = "/deck_save", method = RequestMethod.POST)
	public String saveDeck(@ModelAttribute("deck") Deck deck) {
		deckService.save(deck);
		
		return "redirect:/";
	}

	@RequestMapping(value = "/card_save", method = RequestMethod.POST)
	public String saveCard(@ModelAttribute("card") Card card) {
		cardService.save(card);
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveUser(@ModelAttribute("user") User user) {
		userService.save(user);
		
		return "redirect:/";
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
		ModelAndView mav = new ModelAndView("edit_product");
		User user = userService.get(id);
		mav.addObject("user", user);
		
		return mav;
	}
	
	/*@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") int id) {
		service.delete(id);
		return "redirect:/";		
	}*/
}
