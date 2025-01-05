package org.flexitech.projects.embedded.truckscale.admin.controllers;

import javax.servlet.http.HttpSession;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.auth.LoginDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

	@Autowired
	AuthService authService;
	
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		LoginDTO loginDTO = new LoginDTO();
		model.addAttribute("loginDTO", loginDTO);
		return "login";
	}
	
	@PostMapping("/login")
	public String loginPost(@ModelAttribute LoginDTO loginDTO, Model model, HttpSession session) {
	    try {
	        UserDTO loggedUser = this.authService.login(loginDTO);
	        
	        if (CommonValidators.isValidObject(loggedUser)) {
	            session.setAttribute("loggedInUser", loggedUser);
	            return "redirect:dashboard.fxt";
	        } else {
	            model.addAttribute("errorMsg", "Login failed! Invalid credentials.");
	        }
	    } catch (Exception e) {
	        model.addAttribute("errorMsg", "An error occurred during login. Please try again later.");
	    }
	    return "login";
	}
	
}
