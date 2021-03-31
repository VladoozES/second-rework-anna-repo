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
	
	/*@RequestMapping("/new")
	public String showNewUserPage(Model model) { //создание нового продукта
		Product product = new Product();
		model.addAttribute("product", product);
		
		return "new_product";
	}*/
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("user") User user) {
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
