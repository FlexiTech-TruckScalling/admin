package org.flexitech.projects.embedded.truckscale.admin.controllers.customers;

import org.flexitech.projects.embedded.truckscale.admin.controllers.BaseController;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerTypeDTO;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerTypeController extends BaseController<CustomerTypeDTO, CustomerTypeService> {

	protected CustomerTypeController(CustomerTypeService service) {
		super(service, "Flexitech | Truck Scale Customer Type Manage", "customer-type-manage");
	}

	@GetMapping("customer-type-manage")
	public String customerTypePage(Model model, @RequestParam(required = false) Long id) {
		return super.managePage(model, id, new CustomerTypeDTO());
	}

	@PostMapping("customer-type-manage")
	public String customerTypeManage(@ModelAttribute CustomerTypeDTO customerTypeDTO, Model model,
			RedirectAttributes attr) {
		return super.managePost(customerTypeDTO, model, attr);
	}

	@PostMapping("customer-type-delete")
	public String customerTypeDelete(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attr) {
		return super.deleteEntity(deleteDTO, model, attr);
	}

	@Override
	protected CustomerTypeDTO getById(Long id) {
		return service.getCustomerTypeById(id);
	}

	@Override
	protected Long getId(CustomerTypeDTO dto) {
		return dto.getId();
	}

	@Override
	protected CustomerTypeDTO manage(CustomerTypeDTO dto) {
		return service.manageCustomerType(dto);
	}

	@Override
	protected boolean deleteById(Long id) {
		return service.deleteCustomerType(id);
	}

	@Override
	protected void commonModel(Model model, CustomerTypeDTO dto) {
		model.addAttribute("customerTypeDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("customerTypeList", service.getAllCustomerTypes(null));
	}

}
