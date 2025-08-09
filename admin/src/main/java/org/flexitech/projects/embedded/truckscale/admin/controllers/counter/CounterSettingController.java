package org.flexitech.projects.embedded.truckscale.admin.controllers.counter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.services.counter.CounterService;
import org.flexitech.projects.embedded.truckscale.services.counter.CounterSettingService;
import org.flexitech.projects.embedded.truckscale.services.setting.WeightUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CounterSettingController {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	CounterSettingService counterSettingService;

	@Autowired
	CounterService counterService;
	
	@Autowired
	WeightUnitService weightUnitService;
	
	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "Flexitech | Truck Scale Counter Setting";
	}

	@GetMapping("counter-setting")
	public String counterSettingPage(Model model, @RequestParam(required = false) Long counterId) {
		CounterDTO counter = counterSettingService.getCounterSettingByCounterId(counterId);
		commonModel(model, counter);
		return "counter-setting";
	}

	@PostMapping("counter-setting")
	public String counterSettingManage(@ModelAttribute CounterDTO counterDTO, Model model,
			RedirectAttributes attribute) {

		try {
			this.counterSettingService.manageCounterSetting(counterDTO);
			attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Save success.");

			return "redirect:/counter-setting.fxt?counterId=" + counterDTO.getId();
		} catch (Exception e) {
			logger.error("Error while saving counter setting : {}", ExceptionUtils.getStackTrace(e));
			model.addAttribute(CommonConstants.ERROR_MSG, "Error: "+e.getMessage());
		}
		commonModel(model, counterDTO);
		return "counter-setting";
	}

	private void commonModel(Model model, CounterDTO counter) {
		model.addAttribute("counterDTO", counter);
		model.addAttribute("counterList", counterService.getAllCounters(ActiveStatus.ACTIVE.getCode()));
		model.addAttribute("inOutBoundList", InOutBounds.getAll(false));
		model.addAttribute("weigthUnitList", weightUnitService.getAllWeightUnit(ActiveStatus.ACTIVE.getCode()));
	}

}
