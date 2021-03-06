package net.codejava;

import java.util.ArrayList;
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
	
	@RequestMapping("/{userId}/decks/{deckId}/cards/fsrepeat")
	public String viewDeckCardsFirstSideRepeat(@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId, Model model) {
		List<Card> repeatListCards = cardService.listAllForRepeatByDeckId(deckId);
		AnswerForm answerForm = new AnswerForm();
		for (var i = 0; i < repeatListCards.size(); i++) {
			answerForm.addAnswer(new Answer(repeatListCards.get(i).getCardId(),
					repeatListCards.get(i).getFirstSide(),
					repeatListCards.get(i).getSecondSide()));
		}
		model.addAttribute("answerForm", answerForm);
		model.addAttribute("userId", userId);
		model.addAttribute("deckId", deckId);
		
		return "repeat_first_side_cards";
	}
	
	@RequestMapping("/{userId}/decks/{deckId}/cards/ssrepeat")
	public String viewDeckCardsSecondSideRepeat(@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId, Model model) {
		List<Card> repeatListCards = (ArrayList<Card>) cardService.listAllForRepeatByDeckId(deckId);
		Answer[] answerArray = new Answer[repeatListCards.size()];
		model.addAttribute("repeatListCards", repeatListCards);
		model.addAttribute("answerArray", answerArray);
		model.addAttribute("userId", userId);
		model.addAttribute("deckId", deckId);
		
		return "repeat_second_side_cards";
	}
	
	@RequestMapping("/{userId}/decks/{deckId}/cards/fsrepeat/control")
	public String viewDeckCardsFirstSideRepeatControl(
			@ModelAttribute("answerForm") AnswerForm answerForm,
			@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId, Model model) {
		model.addAttribute("answerForm", answerForm);
		
		return "repeat_first_side_cards_control";
	}
	
	@RequestMapping("/{userId}/decks/{deckId}/cards/fsrepeat/replanning")
	public String viewDeckCardsFirstSideRepeatReplanning(
			@ModelAttribute("answerForm") AnswerForm answerForm,
			@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId, Model model) {
		ReplanConfigForm configForm = new ReplanConfigForm();
		String[] messageArray = new String[answerForm.getAnswers().size()];
		for (var i = 0; i < answerForm.getAnswers().size(); i++) {
			configForm.addConfig(new ReplanConfig(answerForm.getAnswers().get(i).getCardId()));
			if (answerForm.getAnswers().get(i).getTruth())
				messageArray[i] = "Рекомендуется не понижать интервал (сейчас 1 раз в " +
						cardService.getBoxNumOnId(answerForm.getAnswers().get(i).getCardId()) +
						" дня/дней)";
			else messageArray[i] = "Рекомендуется понизить интервал (сейчас 1 раз в " +
					cardService.getBoxNumOnId(answerForm.getAnswers().get(i).getCardId()) +
					" дня/дней)";
		}
		
		
		model.addAttribute("answerForm", answerForm);
		model.addAttribute("configForm", configForm);
		model.addAttribute("messageArray", messageArray);
		
		return "repeat_first_side_cards_replanning";
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
		model.addAttribute("userId", userId);
		
		return "deck_create";
	}
	
	@RequestMapping("/{userId}/decks/{deckId}/cards/new")
	public String viewCardCreate(@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId, Model model) {
		Card card = new Card();
		card.setDeckId(deckId);
		model.addAttribute("card", card);
		model.addAttribute("userId", userId);
		model.addAttribute("deckId", deckId);
		
		return "card_create";
	}
	
	/*@RequestMapping("/new")
	public String showNewUserPage(Model model) { //создание нового продукта
		Product product = new Product();
		model.addAttribute("product", product);
		
		return "new_product";
	}*/
	
	@RequestMapping(value = "/{userId}/decks/{deckId}/cards/fsrepeat/replanning/save", method = RequestMethod.POST)
	public String saveReplanConfigs(@ModelAttribute("configForm") ReplanConfigForm configForm,
			@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId) {
		for (var i = 0; i < configForm.getConfigs().size(); i++)
			cardService.saveReplanUpdates(configForm.getConfigs().get(i).getCardId(),
					configForm.getConfigs().get(i).getNewBoxNum());
		
		return "redirect:/" + userId + "/decks/" + deckId + "/cards";
	}
	
	@RequestMapping(value = "/{userId}/decks/deck_save", method = RequestMethod.POST)
	public String saveDeck(@ModelAttribute("deck") Deck deck,
			@PathVariable(name = "userId") Long userId) {
		deckService.save(deck);
		
		return "redirect:/" + userId + "/decks";
	}

	@RequestMapping(value = "/{userId}/decks/{deckId}/cards/card_save", method = RequestMethod.POST)
	public String saveCard(@ModelAttribute("card") Card card,
			@PathVariable(name = "userId") Long userId,
			@PathVariable(name = "deckId") Long deckId) {
		cardService.save(card);
		
		return "redirect:/" + userId + "/decks/" + deckId + "/cards";
	}
	
	/*@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveUser(@ModelAttribute("user") User user) {
		userService.save(user);
		
		return "redirect:/";
	}//*/
	
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
