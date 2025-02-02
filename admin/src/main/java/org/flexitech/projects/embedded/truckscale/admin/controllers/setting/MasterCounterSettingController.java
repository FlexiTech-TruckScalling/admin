package org.flexitech.projects.embedded.truckscale.admin.controllers.setting;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.setting.MasterCounterSettingListDTO;
import org.flexitech.projects.embedded.truckscale.services.setting.MasterCounterSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MasterCounterSettingController {
	
	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	MasterCounterSettingService counterSettingService;
	
	@GetMapping("master-counter-settings")
	public String masterCounterSettingsPage(Model model) {
		commonModel(model, this.counterSettingService.getSettingList(null));
		return "master-counter-settings";
	}
	
	@PostMapping("master-counter-settings")
	public String masterCounterSettingsPost(@ModelAttribute MasterCounterSettingListDTO masterCounterSettingListDTO, Model model, RedirectAttributes attr) {
		try {
			counterSettingService.updateSetting(masterCounterSettingListDTO);
			attr.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Update success.");
			return "redirect:master-counter-settings.fxt";
		}catch(Exception e) {
			logger.error("Error on managing master counter setting: {}", ExceptionUtils.getStackTrace(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "master-counter-settings";
	}
	
	private void commonModel(Model model, MasterCounterSettingListDTO dto) {
		model.addAttribute("masterCounterSettingListDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("categoryList", this.counterSettingService.getAllCategory(ActiveStatus.ACTIVE.getCode()));
	}
}
