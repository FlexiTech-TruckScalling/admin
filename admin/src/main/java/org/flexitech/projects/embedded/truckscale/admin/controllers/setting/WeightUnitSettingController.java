package org.flexitech.projects.embedded.truckscale.admin.controllers.setting;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.WeightUnitDTO;
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
public class WeightUnitSettingController {
	
	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	WeightUnitService service;
	
	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "Flexitech | Truck Scale Weight Unit Setting";
	}
	
	@GetMapping("/weight-units")
	public String weightUnitPage(Model model, @RequestParam(required = false) Long id) {
		
		WeightUnitDTO dto = null;
		if(CommonValidators.validLong(id)) {
			dto = this.service.getWeightUnitById(id);
		}else {
			dto = new WeightUnitDTO();
		}
		
		commonModel(model, dto);
		return "weight-units";
	}
	
	@PostMapping("/weight-units")
	public String manageWeightUnit(@ModelAttribute WeightUnitDTO weightUnitDTO, Model model, RedirectAttributes attribute) {
		
		boolean update = CommonValidators.validLong(weightUnitDTO.getId());
		if(service.isSequenceAlreadyUsed(weightUnitDTO.getSequence(), weightUnitDTO.getId())) {
			model.addAttribute(CommonConstants.ERROR_MSG, "The order sequencec is already used!");
		}else {
			try {
				WeightUnitDTO managed= this.service.manageWeightUnit(weightUnitDTO);
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, !update ? "Save success.":"Update success.");
				attribute.addFlashAttribute(CommonConstants.NEW_RECORD, managed.getId());
				return "redirect:weight-units.fxt";
			}catch (Exception e) {
				logger.error("Error on managing weight unit: {}", ExceptionUtils.getStackTrace(e));
				model.addAttribute(CommonConstants.ERROR_MSG, update ? "Failed to update!":"Failed to save!");
			}
		}
		
		
		commonModel(model, weightUnitDTO);
		return "weight-units";
	}
	
	@PostMapping("weight-units-delete")
	public String deleteRole(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attribute) {
		try {
			Long unitId = deleteDTO.getId();
			if(!CommonValidators.validLong(unitId)) {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete! Unknow id.");
			}
			if(this.service.deleteWeightUnit(unitId)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Successfully deleted!");
			}else {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete!");
			}
		}catch(Exception e) {
			logger.error("Error on delete counter: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "redirect:weight-units.fxt";
	}
	
	private void commonModel(Model model, WeightUnitDTO dto) {
		model.addAttribute("weightUnitDTO", dto);
		model.addAttribute("weightUnitList", service.getAllWeightUnit(null));
		model.addAttribute("statusList", ActiveStatus.getAll());
	}
}
