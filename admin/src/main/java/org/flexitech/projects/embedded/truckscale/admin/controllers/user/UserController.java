package org.flexitech.projects.embedded.truckscale.admin.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {

	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "ENL | Truck Scale User Manage";
	}
	
	@GetMapping("user-manage")
	public String userManagePage() {
		
		return "user-manage";
	}
	
}
