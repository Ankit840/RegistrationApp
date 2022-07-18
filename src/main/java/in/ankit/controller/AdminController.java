package in.ankit.controller;

import java.security.Principal;
import java.util.List;

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
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		User user = userRepo.getUserByUserName(userName);
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "Admin Dashboard");
		return "admin/admin_dashboard";
	}

	@GetMapping("/add-user")
	public String openAddUserForm(Model model) {
		model.addAttribute("title", "Add User");
		model.addAttribute("user", new User());
		return "admin/add_user";
	}

	// processing add user form
	@PostMapping("/process-user")
	public String processUser(@ModelAttribute User user, BindingResult result, Model model) {
		try {

			if (!user.getPassword().equals(user.getConfirmPassword())) {
				model.addAttribute("message", "Password and confirm password is not match");
				return "admin/add_user";
			}

			if (userRepo.getUserByUserName(user.getEmail()) != null) {
				model.addAttribute("message", "Email id already exists");
				return "admin/add_user";
			}

			if (result.hasErrors()) {
				model.addAttribute("user", user);
				return "admin/add_user";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setStatus("Active");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
			userRepo.save(user);
			model.addAttribute("user", new User());
			model.addAttribute("message", "Successfully Registered !!");
			return "admin/add_user";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			model.addAttribute("message", "Not registered, Please try again !!");
			return "admin/add_user";
		}
	}
	
	//show contacts handler
	@GetMapping("/show-users")
	public String showUsers(Model model) {
		model.addAttribute("title", "Show User");
		List<User> usersList = userRepo.findAll();
		model.addAttribute("listUser", usersList);
		return "admin/show_users";
	}
}
