package org.flexitech.projects.embedded.truckscale.admin.controllers.customers;

import org.flexitech.projects.embedded.truckscale.admin.controllers.BaseController;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerSearchDTO;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerService;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController extends BaseController<CustomerDTO, CustomerService> {

	@Autowired
	CustomerTypeService customerTypeService;
	
	protected CustomerController(CustomerService service) {
		super(service, "ENL | Truck Scale Customer Manage", "customer-manage");
	}

	@GetMapping("customer-manage")
	public String customerManagePage(Model model, @RequestParam(required = false) Long id) {
		return super.managePage(model, id, new CustomerDTO());
	}
	
	@PostMapping("customer-manage")
	public String customerManagePost(@ModelAttribute CustomerDTO customerDTO, Model model, RedirectAttributes attr) {
		return super.managePost(customerDTO, model, attr);
	}
	
	@GetMapping("customer-search")
	public String customerSearchPage(Model model) {
		CustomerSearchDTO searchDTO = new CustomerSearchDTO();
		searchDTO.setPageNo(1);
		commonSearchModel(model, searchDTO);
		return "customer-search";
	}
	
	@PostMapping("customer-search")
	public String customerSearchPost(@ModelAttribute CustomerSearchDTO searchDTO,Model model) {
		commonSearchModel(model, searchDTO);
		return "customer-search";
	}
	
	private void commonSearchModel(Model model, CustomerSearchDTO searchDTO) {
		model.addAttribute("pageTitle", "ENL | Truck Scale Customer Search");
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("customerList", service.searchCustomers(searchDTO));
		model.addAttribute("searchDTO", searchDTO);
		model.addAttribute("custometTypeList", customerTypeService.getAllCustomerTypes(ActiveStatus.ACTIVE.getCode()));
	}
	
	@PostMapping("customer-delete")
	public String deleteCustomer(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attr) {
		return super.deleteEntity(deleteDTO, model, attr);
	}
	
	@Override
	protected CustomerDTO getById(Long id) {
		return service.getCustomerById(id);
	}

	@Override
	protected Long getId(CustomerDTO dto) {
		return dto.getId();
	}

	@Override
	protected CustomerDTO manage(CustomerDTO dto) {
		return service.manageCustomer(dto);
	}

	@Override
	protected boolean deleteById(Long id) {
		return service.deleteCustomer(id);
	}

	@Override
	protected void commonModel(Model model, CustomerDTO dto) {
		model.addAttribute("customerDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("customerTypeList", customerTypeService.getAllCustomerTypes(ActiveStatus.ACTIVE.getCode()));
	}

}
