package com.contactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.entity.Contact;
import com.contactmanager.entity.User;
import com.contactmanager.helper.Message;
import com.contactmanager.repository.ContactRepository;
import com.contactmanager.repository.UserRepository;

import jakarta.persistence.criteria.Path;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	//common method for all url
	@ModelAttribute
	public void commonData(Model model, Principal principal) {

		String userName = principal.getName();
		System.out.println("User name " +userName);
		
		User user = userRepository.getUserByUserName(userName);
		
		System.out.println(user);
		
		model.addAttribute("user",user);
		
	}

	@GetMapping("/index")
	public String userDashboard(Model model,Principal principal) {//principal gives the username and password
		model.addAttribute("title","Home");
		return "normal/user_dashboard";
	}
	
	//open add from handler
	@GetMapping("/add-contact")
	public String addContact(Model model) {
		
		model.addAttribute("title","User Dashboard");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	//processing add contact 
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file ,
			Principal principal,HttpSession session) {
		
		try {
			
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
		//processing and uploading file
		
		if(file.isEmpty()) {
			//if the file is empty than try our message
			System.out.println("File is empty");
			contact.setImage("contact.jpg");
		}
		else {
			//file to folder and update the name to contact
			contact.setImage(file.getOriginalFilename());
			
			File saveFile=new ClassPathResource("static/img").getFile();
			
			java.nio.file.Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(file.getInputStream(), path ,StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Image is uploaded");
		}
				
		System.out.println("data"+contact.getDescription());
		
		contact.setUser(user);
		user.getContacts().add(contact);
        
		
		this.userRepository.save(user);
		
		System.out.println("data"+contact);
		System.out.println("added in the database");
		
		//message success
		//session.setAttribute("message","success");
		
			
		}
		 catch (Exception e) {
			 System.out.println("Error"+e.getMessage());
			 e.printStackTrace();
			  
			 //massage error
			 //session.setAttribute("message","danger");
				
		}
		return "normal/add_contact_form";	
	}
	
	//show contact handler
	@GetMapping("/show-contact")
	public String showContact(Model m ,Principal principal) {
		
		m.addAttribute("title","Show User Contact");
		
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		
		List<Contact> contacts = contactRepository.getContactByUser(user.getId());
		
		m.addAttribute("contacts", contacts);
		
		return "normal/show_contact";
	}
	
	//showing  particular contact details
	@RequestMapping("/contact/{cid}")
	public String showContactDetails(@PathVariable("cid") Integer cid, Model m, Principal principal) {
		
		Optional<Contact> contactOptional = contactRepository.findById(cid);
		Contact contact = contactOptional.get();
	
		String userName = principal.getName();
		
		User user = userRepository.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId()) {
			m.addAttribute("contact",contact);
			m.addAttribute("title",contact.getName());
		}
		
		//System.out.println("contact"+contact);
	
		return "normal/contact_detail";
	}
	
	//handler for the delete contact
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, Principal principal) {
		
	         Optional<Contact> contactOptional = contactRepository.findById(cid);
	         Contact contact = contactOptional.get();

	         contact.setUser(null);
	     
	 	
	         contactRepository.delete(contact);
	         model.addAttribute("deleted","success");
	 		
		
		return "redirect:/user/show-contact";
	}
	@PostMapping("/update/{cid}")
	public String updateContact(@PathVariable("cid") Integer cid, Model model) {
		Contact contact= contactRepository.findById(cid).get();
		model.addAttribute("title", "update contact");
		model.addAttribute("contact",contact);
		return "normal/update_contact";
	}
	
	//updating the form 
	/*
	 * @PostMapping("/update-process") public String updateProcess(@ModelAttribute
	 * Contact contact,Model m ,@RequestParam("profileImage") MultipartFile
	 * file,Principal principal ) { //data coming from the page that handl by
	 * modelattribute
	 * 
	 * try { //image if(!file.isEmpty()) {
	 * 
	 * } User user =this.userRepository.getUserByUserName(principal.getName());
	 * contact.setUser(user);
	 * 
	 * 
	 * this.contactRepository.save(contact);
	 * 
	 * 
	 * } catch (Exception e) { // TODO: handle exception }
	 * 
	 * System.out.println(contact.getName());
	 * 
	 * return "";
	 * 
	 * 
	 * 
	 * }
	 */
}
