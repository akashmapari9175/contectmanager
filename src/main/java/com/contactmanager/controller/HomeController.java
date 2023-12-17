package com.contactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.entity.User;
import com.contactmanager.helper.Message;
import com.contactmanager.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	
	  @Autowired 
	  private BCryptPasswordEncoder passwordEncoder;
	 
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title","home-Contact-Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","about-home-Contact-Manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user",new User());
		return "signup";
	}
	
	// handler for registering user
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(value="agreement",defaultValue = "false") boolean agreement,Model m, HttpSession session) {
		
		try {
			
			if(!agreement) {
				System.out.println("you have---- not agreed the trems and conditions");
				throw new Exception("you have not agreed the trems and conditions");
			}
			
			if(result.hasErrors()) {
				//System.out.println("ERROR", +result.toString());
				m.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			//System.out.println("Aggrement=" +agreement);
			//System.out.println("USER="+user);
			
			User result1 = this.userRepository.save(user);
			m.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully Registered !!","alert-success"));
			
			return "signup";

		
			
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user",user);
			session.setAttribute("message",new Message("something went worng!!"+e.getMessage(),"alert-denger"));
			return "signup";
		}
		

	}
	//custom handler for regigsterUser
	@GetMapping("/signin")
	public String signin(Model model) {
		model.addAttribute("title","login page");
		return "login";
	}
//	@GetMapping("/login-fail")
//	public String loginFail() {
//		return "loginfail";
//	}
	
	
	
	
	
}
