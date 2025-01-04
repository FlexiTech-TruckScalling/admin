package org.flexitech.projects.embedded.truckscale.admin.controllers.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {
		return "dashboard";
	}
	
}
