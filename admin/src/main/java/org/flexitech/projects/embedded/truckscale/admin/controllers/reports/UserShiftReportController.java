package org.flexitech.projects.embedded.truckscale.admin.controllers.reports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.dto.user_shift.UserShiftSearchDTO;
import org.flexitech.projects.embedded.truckscale.services.shift.UserShiftReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserShiftReportController {

	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	UserShiftReportService userShiftReportService;
	
	
	@GetMapping("shift-report")
	public String getUserShiftReportPage(Model model) {
		
		UserShiftSearchDTO searchDTO = new UserShiftSearchDTO();
		searchDTO.setPageNo(1);
		commonModelForSearch(model, searchDTO);
		return "shift-report";
	}
	
	@PostMapping("shift-report")
	public String searchUserShiftReport(@ModelAttribute UserShiftSearchDTO searchDTO,Model model) {
		commonModelForSearch(model, searchDTO);
		return "shift-report";
	}
	
	@GetMapping("shift-detail")
	public String shiftDetailPage(@RequestParam(required = false) Long shiftId,Model model) {
		model.addAttribute("userShiftSummary", this.userShiftReportService.getShiftDetailByShiftId(shiftId));
		model.addAttribute("pageTitle", "Flexitech | Truck Scale User Shift Detail");
		return "shift-detail";
	}
	
	private void commonModelForSearch(Model model, UserShiftSearchDTO searchDTO) {
		model.addAttribute("pageTitle", "Flexitech | Truck Scale User Shifts");
		model.addAttribute("shiftList", this.userShiftReportService.searchUserShift(searchDTO, false));
		
		Integer total = this.userShiftReportService.countUserShift(searchDTO);
		int totalPages = (int) Math.ceil((double) total / searchDTO.getLimit());
		
		searchDTO.setPageCount(totalPages);
		searchDTO.setTotalRecords(total);
		
		model.addAttribute("searchDTO", searchDTO);
	}
	
}
