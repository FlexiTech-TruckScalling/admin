package org.flexitech.projects.embedded.truckscale.superadmin.controllers.customers;

import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.dto.superadminuser.SuperAdminCustomerDTO;
import org.flexitech.projects.embedded.truckscale.services.superadmin.SupAdminCustService;
import org.flexitech.projects.embedded.truckscale.superadmin.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SupAdminCustController extends BaseController<SuperAdminCustomerDTO, SupAdminCustService>{

	protected SupAdminCustController(SupAdminCustService service) {
		super(service, "Flexitech | Truck Scale Customer Manage", "customer-manage");
		// TODO Auto-generated constructor stub
	}

	@GetMapping("customer-manage")
	public String customerManagePage(Model model, @RequestParam(required = false) Long id) {
		return super.managePage(model, id, new SuperAdminCustomerDTO());
	}
	
	@PostMapping("customer-manage")
	public String customerManagePost(@ModelAttribute SuperAdminCustomerDTO supAdminCustDTO, Model model, RedirectAttributes attr) {
		return super.managePost(supAdminCustDTO, model, attr);
	}

	@Override
	protected SuperAdminCustomerDTO getById(Long id) {
		return service.getCustomerById(id);
	}

	@Override
	protected Long getId(SuperAdminCustomerDTO dto) {
		return dto.getId();
	}

	@Override
	protected SuperAdminCustomerDTO manage(SuperAdminCustomerDTO dto) throws Exception {
		return service.manageCustomer(dto);
	}

	@Override
	protected boolean deleteById(Long id) throws Exception {
		return service.deleteSupAdminCustomer(id);
	}
	
	@PostMapping("customer-delete")
	public String deleteCustomer(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attr) {
		return super.deleteEntity(deleteDTO, model, attr);
	}

	@Override
	protected void commonModel(Model model, SuperAdminCustomerDTO dto) throws Exception {
		
		model.addAttribute("supAdminCustDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		
		
	}

	
}
