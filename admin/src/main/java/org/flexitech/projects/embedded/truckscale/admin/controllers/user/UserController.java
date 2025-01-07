package org.flexitech.projects.embedded.truckscale.admin.controllers.user;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserRoleDTO;
import org.flexitech.projects.embedded.truckscale.services.counter.CounterService;
import org.flexitech.projects.embedded.truckscale.services.user.UserRoleService;
import org.flexitech.projects.embedded.truckscale.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	private Logger logger = LogManager.getLogger(getClass());

	@Autowired
	UserService userService;
	
	@Autowired
	CounterService counterService;
	
	@Autowired
	UserRoleService userRoleService;
	
	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "ENL | Truck Scale User Manage";
	}
	
	@GetMapping("user-manage")
	public String roleManagePage(Model model, @RequestParam(required = false) Long id) {
		UserDTO userDTO = new UserDTO();
		if(CommonValidators.validLong(id)) {
			userDTO = this.userService.getUserById(id);
		}
		commonModel(model, userDTO);
		return "user-manage";
	}
	
	@PostMapping("user-manage")
	public String roleManagePost(@ModelAttribute("userDTO") UserDTO userDTO, Model model, RedirectAttributes attribute) {
		try {
			boolean update = CommonValidators.validLong(userDTO.getId());
			UserDTO managed = this.userService.manageUser(userDTO);
			if(CommonValidators.isValidObject(managed)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, update?"Update success.":"Create success.");
				attribute.addFlashAttribute(CommonConstants.NEW_RECORD, managed.getId());
				return "redirect:user-manage.fxt";
			}else {
				model.addAttribute(CommonConstants.ERROR_MSG, update?"Update failed!":"Create failed!");
			}
		}catch(Exception e) {
			logger.error("Error on manage user: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		commonModel(model, userDTO);
		return "user-manage";
	}
	
	@PostMapping("user-delete")
	public String deleteRole(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attribute) {
		try {
			Long roleId = deleteDTO.getId();
			if(!CommonValidators.validLong(roleId)) {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete! Unknow id.");
			}
			if(this.userService.deleteUser(roleId)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Successfully deleted!");
			}else {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete!");
			}
		}catch(Exception e) {
			logger.error("Error on manage user: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "redirect:user-manage.fxt";
	}
	
	private void commonModel(Model model, UserDTO dto) {
		model.addAttribute("userDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("userList", userService.getAllUsers(null));
		model.addAttribute("counterList", counterService.getAllCounters(ActiveStatus.ACTIVE.getCode()));
		model.addAttribute("userRoleList", userRoleService.getAllRoles(ActiveStatus.ACTIVE.getCode()));
	}
	
	
}
