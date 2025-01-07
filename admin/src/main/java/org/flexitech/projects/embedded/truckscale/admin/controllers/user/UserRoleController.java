package org.flexitech.projects.embedded.truckscale.admin.controllers.user;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserRoleDTO;
import org.flexitech.projects.embedded.truckscale.services.user.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserRoleController {
	
	private Logger logger = LogManager.getLogger(getClass());

	@Autowired
	UserRoleService roleService;
	
	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "ENL | Truck Scale Role Manage";
	}
	
	@GetMapping("role-manage")
	public String roleManagePage(Model model, @RequestParam(required = false) Long id) {
		UserRoleDTO counterDTO = new UserRoleDTO();
		if(CommonValidators.validLong(id)) {
			counterDTO = this.roleService.getRoleById(id);
		}
		commonModel(model, counterDTO);
		return "role-manage";
	}
	
	@PostMapping("role-manage")
	public String roleManagePost(@ModelAttribute UserRoleDTO userRoleDTO, Model model, RedirectAttributes attribute) {
		try {
			boolean update = CommonValidators.validLong(userRoleDTO.getId());
			UserRoleDTO managed = this.roleService.manageRole(userRoleDTO);
			if(CommonValidators.isValidObject(managed)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, update?"Update success.":"Create success.");
				attribute.addFlashAttribute(CommonConstants.NEW_RECORD, managed.getId());
				return "redirect:role-manage.fxt";
			}else {
				model.addAttribute(CommonConstants.ERROR_MSG, update?"Update failed!":"Create failed!");
			}
		}catch(Exception e) {
			logger.error("Error on manage role: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "role-manage";
	}
	
	private void commonModel(Model model, UserRoleDTO dto) {
		model.addAttribute("counterDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("roleList", roleService.getAllRoles());
	}
	
}
