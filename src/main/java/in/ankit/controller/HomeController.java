package in.ankit.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import in.ankit.entities.User;
import in.ankit.repo.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - User Registration");
		return "home";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - User Registration");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		try {
			
			if(!user.getPassword().equals(user.getConfirmPassword())) {
				model.addAttribute("message", "Password and confirm password is not match");
				return "signup";
			}
			
			if(userRepo.getUserByUserName(user.getEmail()) != null) {
				model.addAttribute("message", "Email id already exists");
				return "signup";
			}
			
			if(result.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setStatus("Active");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
			userRepo.save(user);
			model.addAttribute("user", new User());
			model.addAttribute("message", "Successfully Registered !!");
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			model.addAttribute("message", "Not registered, Please try again !!");
			return "signup";
		}
	}

	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "Login Page");
		return "login";
	}
}
