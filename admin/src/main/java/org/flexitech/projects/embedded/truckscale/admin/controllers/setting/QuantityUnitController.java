package org.flexitech.projects.embedded.truckscale.admin.controllers.setting;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.dto.unit.QuantityUnitDTO;
import org.flexitech.projects.embedded.truckscale.services.unit.QuantityUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class QuantityUnitController {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	QuantityUnitService service;
	
	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "ENL | Truck Scale Quantity Unit Setting";
	}
	
	@GetMapping("/quantity-units")
	public String quantityUnitPage(Model model, @RequestParam(required = false) Long id) {
		
		QuantityUnitDTO dto = null;
		if(CommonValidators.validLong(id)) {
			dto = this.service.getById(id);
		}else {
			dto = new QuantityUnitDTO();
		}
		
		commonModel(model, dto);
		return "quantity-units";
	}
	
	@PostMapping("/quantity-units")
	public String manageWeightUnit(@ModelAttribute QuantityUnitDTO quantityUnitDTO, Model model, RedirectAttributes attribute) {
		
		boolean update = CommonValidators.validLong(quantityUnitDTO.getId());
		if(service.isSequenceAlreadyUsed(quantityUnitDTO.getSequence(), quantityUnitDTO.getId())) {
			model.addAttribute(CommonConstants.ERROR_MSG, "The order sequencec is already used!");
		}else {
			try {
				QuantityUnitDTO managed= this.service.manageQuantityUnit(quantityUnitDTO);
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, !update ? "Save success.":"Update success.");
				attribute.addFlashAttribute(CommonConstants.NEW_RECORD, managed.getId());
				return "redirect:quantity-units.fxt";
			}catch (Exception e) {
				logger.error("Error on managing quantity unit: {}", ExceptionUtils.getStackTrace(e));
				model.addAttribute(CommonConstants.ERROR_MSG, update ? "Failed to update!":"Failed to save!");
			}
		}
		
		
		commonModel(model, quantityUnitDTO);
		return "quantity-units";
	}
	
	@PostMapping("quantity-units-delete")
	public String deleteRole(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attribute) {
		try {
			Long unitId = deleteDTO.getId();
			if(!CommonValidators.validLong(unitId)) {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete! Unknow id.");
			}
			if(this.service.deleteQuantityUnit(unitId)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Successfully deleted!");
			}else {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete!");
			}
		}catch(Exception e) {
			logger.error("Error on delete counter: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "redirect:quantity-units.fxt";
	}
	
	private void commonModel(Model model, QuantityUnitDTO dto) {
		model.addAttribute("quantityUnitDTO", dto);
		model.addAttribute("quantityUnitList", service.getQuantityUnitsByStatus(null));
		model.addAttribute("statusList", ActiveStatus.getAll());
	}
	
}
