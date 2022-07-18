package in.ankit.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import in.ankit.entities.User;
import in.ankit.repo.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		String userName = principal.getName();
		User user = userRepo.getUserByUserName(userName);
		model.addAttribute("user", user);
		return "normal/user_dashboard";
	}
}
