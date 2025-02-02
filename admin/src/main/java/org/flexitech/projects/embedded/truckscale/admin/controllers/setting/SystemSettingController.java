package org.flexitech.projects.embedded.truckscale.admin.controllers.setting;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.setting.SystemSettingListDTO;
import org.flexitech.projects.embedded.truckscale.services.setting.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SystemSettingController {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private SystemSettingService systemSettingService;

	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "System Settings";
	}
	
	@GetMapping("/system-settings")
	public String systemSettingPage(Model model) {
		commonModel(model, systemSettingService.getAllSystemSettings(null));
		return "system-settings";
	}

	@PostMapping("system-settings")
	public String systemSettingPost(@ModelAttribute SystemSettingListDTO systemSettingListDTO, Model model,
			RedirectAttributes attr) {
		try {
			SystemSettingListDTO dto = this.systemSettingService.manageSystemSettings(systemSettingListDTO);
			if (CommonValidators.isValidObject(dto)) {
				attr.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Update success");
				return "redirect:/system-settings.fxt";
			} else {
				model.addAttribute(CommonConstants.ERROR_MSG, "Fails to udpate!");
			}
		} catch (Exception e) {
			logger.error("Error on update system setting: {}", ExceptionUtils.getStackTrace(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		commonModel(model, systemSettingListDTO);
		return "system-settings";
	}

	private void commonModel(Model model, SystemSettingListDTO dto) {
		model.addAttribute("systemSettingListDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
	}
}
