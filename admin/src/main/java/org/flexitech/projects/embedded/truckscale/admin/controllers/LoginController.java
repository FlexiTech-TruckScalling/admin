package org.flexitech.projects.embedded.truckscale.admin.controllers;

import org.flexitech.projects.embedded.truckscale.dto.auth.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String loginPage(Model model) {
		LoginDTO loginDTO = new LoginDTO();
		model.addAttribute("loginDTO", loginDTO);
		return "login";
	}
	
	@PostMapping("/login")
	public String loginPost(@ModelAttribute LoginDTO loginDTO, Model model) {
//		model.addAttribute("loginDTO", loginDTO);
		return "redirect:dashboard.fxt";
	}
	
}
