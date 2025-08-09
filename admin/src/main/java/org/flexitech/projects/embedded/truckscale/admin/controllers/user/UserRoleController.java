package org.flexitech.projects.embedded.truckscale.admin.controllers.user;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
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
		return "Flexitech | Truck Scale Role Manage";
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
	public String roleManagePost(@ModelAttribute("roleDTO") UserRoleDTO roleDTO, Model model, RedirectAttributes attribute) {
		try {
			boolean update = CommonValidators.validLong(roleDTO.getId());
			if(!update && this.roleService.isRoleCodeAlreadyUsed(roleDTO.getCode())) {
				model.addAttribute(CommonConstants.ERROR_MSG, "Code is already used!");
			}else {
				UserRoleDTO managed = this.roleService.manageRole(roleDTO);
				if(CommonValidators.isValidObject(managed)) {
					attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, update?"Update success.":"Create success.");
					attribute.addFlashAttribute(CommonConstants.NEW_RECORD, managed.getId());
					return "redirect:role-manage.fxt";
				}else {
					model.addAttribute(CommonConstants.ERROR_MSG, update?"Update failed!":"Create failed!");
				}
			}
		}catch(Exception e) {
			logger.error("Error on manage role: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		commonModel(model, roleDTO);
		return "role-manage";
	}
	
	@PostMapping("role-delete")
	public String deleteRole(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attribute) {
		try {
			Long roleId = deleteDTO.getId();
			if(!CommonValidators.validLong(roleId)) {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete! Unknow id.");
			}
			if(this.roleService.deleteRole(roleId)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Successfully deleted!");
			}else {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete!");
			}
		}catch(Exception e) {
			logger.error("Error on manage role: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "redirect:role-manage.fxt";
	}
	
	private void commonModel(Model model, UserRoleDTO dto) {
		model.addAttribute("roleDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("roleList", roleService.getAllRoles(null));
	}
	
}
