package org.flexitech.projects.embedded.truckscale.admin.controllers.counter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.services.counter.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CounterController {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	CounterService counterService;
	
	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "ENL | Truck Scale Counter Manage";
	}
	
	@GetMapping("counter-manage")
	public String counterManagePage(Model model, @RequestParam(required = false) Long id) {
		CounterDTO counterDTO = new CounterDTO();
		if(CommonValidators.validLong(id)) {
			counterDTO = this.counterService.getCounterById(id);
		}
		commonModel(model, counterDTO);
		return "counter-manage";
	}
	
	@PostMapping("counter-manage")
	public String counterManagePost(@ModelAttribute CounterDTO counterDTO, Model model, RedirectAttributes attribute) {
		try {
			boolean update = CommonValidators.validLong(counterDTO.getId());
			CounterDTO managed = this.counterService.manageCounter(counterDTO);
			if(CommonValidators.isValidObject(managed)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, update?"Update success.":"Create success.");
				attribute.addFlashAttribute(CommonConstants.NEW_RECORD, managed.getId());
				return "redirect:counter-manage.fxt";
			}else {
				model.addAttribute(CommonConstants.ERROR_MSG, update?"Update failed!":"Create failed!");
			}
		}catch(Exception e) {
			logger.error("Error on manage counter: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "counter-manage";
	}
	
	@PostMapping("counter-delete")
	public String deleteRole(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attribute) {
		try {
			Long counterId = deleteDTO.getId();
			if(!CommonValidators.validLong(counterId)) {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete! Unknow id.");
			}
			if(this.counterService.deleteCounter(counterId)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Successfully deleted!");
			}else {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete!");
			}
		}catch(Exception e) {
			logger.error("Error on delete counter: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "redirect:counter-manage.fxt";
	}
	
	private void commonModel(Model model, CounterDTO dto) {
		model.addAttribute("counterDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("counterList", counterService.getAllCounters(null));
	}
}
