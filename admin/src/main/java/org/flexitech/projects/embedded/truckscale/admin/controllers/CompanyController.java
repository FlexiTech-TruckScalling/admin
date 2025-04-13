package org.flexitech.projects.embedded.truckscale.admin.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.company.CompanyDTO;
import org.flexitech.projects.embedded.truckscale.services.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CompanyController {

	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private CompanyService companyService;
	
	@GetMapping("company")
	public String companySetupPage(Model model) {
		CompanyDTO companyDTO = this.companyService.getById(1L);
		if(!CommonValidators.isValidObject(companyDTO)) {
			companyDTO = new CompanyDTO();
		}
		model.addAttribute("companyDTO", companyDTO);
		model.addAttribute("statusList", ActiveStatus.getAll());
		return "company";
	}
	
	@PostMapping("company")
	public String companySetupPost(@ModelAttribute CompanyDTO companyDTO, Model model, RedirectAttributes attr) {
		try {
			companyDTO = this.companyService.manageCompanyDTO(companyDTO);
			if(CommonValidators.isValidObject(companyDTO)) {
				attr.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Successfully save.");
			}else {
				attr.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to save company.");
			}
		}catch (Exception e) {
			logger.error("Error on manage company: {}", e);
			attr.addFlashAttribute(CommonConstants.ERROR_MSG, e.getMessage());
		}
		return "redirect:/company.fxt";
	}
	
}
