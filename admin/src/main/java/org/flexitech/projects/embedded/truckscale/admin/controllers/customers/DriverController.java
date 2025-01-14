package org.flexitech.projects.embedded.truckscale.admin.controllers.customers;

import org.flexitech.projects.embedded.truckscale.admin.controllers.BaseController;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.customers.DriverDTO;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.services.customers.DriverService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DriverController extends BaseController<DriverDTO, DriverService> {

	protected DriverController(DriverService service) {
		super(service, "ENL | Truck Scale Driver Manage", "driver-manage");
	}

	@GetMapping("driver-manage")
	public String driverManagePage(Model model, @RequestParam(required = false) Long id) {
		return super.managePage(model, id, new DriverDTO());
	}
	
	@PostMapping("driver-manage")
	public String driverManagePost(@ModelAttribute DriverDTO driverDTO, Model model, RedirectAttributes attr) {
		return super.managePost(driverDTO, model, attr);
	}
	
	@PostMapping("driver-delete")
	public String deleteDriver(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attr) {
		return super.deleteEntity(deleteDTO, model, attr);
	}
	
	@Override
	protected DriverDTO getById(Long id) {
		return service.getDriverById(id);
	}

	@Override
	protected Long getId(DriverDTO dto) {
		return dto.getId();
	}

	@Override
	protected DriverDTO manage(DriverDTO dto) throws Exception {
		return service.manageDriver(dto);
	}

	@Override
	protected boolean deleteById(Long id) throws Exception {
		return service.deleteDriver(id);
	}

	@Override
	protected void commonModel(Model model, DriverDTO dto) throws Exception {
		model.addAttribute("driverDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("driverList", service.getAllDriver(null));
	}

}
