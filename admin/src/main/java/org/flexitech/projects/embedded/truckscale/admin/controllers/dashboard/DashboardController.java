package org.flexitech.projects.embedded.truckscale.admin.controllers.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class DashboardController {

	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "ENL | Truck Scale Dashboard";
	}
	
	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {
		return "dashboard";
	}
	
}
