package org.flexitech.projects.embedded.truckscale.admin.controllers.customers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.flexitech.projects.embedded.truckscale.admin.controllers.BaseController;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerVehicleDTO;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerService;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerVehicleService;
import org.flexitech.projects.embedded.truckscale.services.customers.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerVehicleController extends BaseController<CustomerVehicleDTO, CustomerVehicleService> {

	private Long customerId;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private DriverService driverService;
	
	protected CustomerVehicleController(CustomerVehicleService service) {
		super(service, "Flexitech | Truck Scale Customer Vehicle Manage", "customer-vehicle-manage");
	}
	
	@GetMapping("/{customerId}/customer-vehicle-manage")
	public String customerVehicleManage(@PathVariable Long customerId, Model model, @RequestParam(required = false) Long id) {
		this.customerId = customerId;
		CustomerVehicleDTO dto = new CustomerVehicleDTO();
		dto.setCustomerId(customerId);
		String page = super.managePage(model, id, dto);
		return resolveURL(customerId + "/", page);
	}

	@PostMapping("/{customerId}/customer-vehicle-manage")
	public String customerVehicleManagePost(@PathVariable Long customerId, @ModelAttribute CustomerVehicleDTO customerVehicleDTO, Model model, RedirectAttributes attr) {
		this.customerId = customerId;
		customerVehicleDTO.setCustomerId(customerId);
		
		if(service.isVehicleNumberAlreadyUserd(customerVehicleDTO.getPrefix(), customerVehicleDTO.getNumber(), customerVehicleDTO.getId())) {
			model.addAttribute(CommonConstants.ERROR_MSG, "The vehicel number is already used!");
			try {
				commonModel(model, customerVehicleDTO);
			} catch (Exception e) {
				logger.error("Error on manage customer vehicle: {}", ExceptionUtils.getStackTrace(e));
				model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
			}
			return "customer-vehicle-manage";
		}
		
		String page = super.managePost(customerVehicleDTO, model, attr);
		logger.debug("managed page: {}", page);
		return resolveURL(customerId + "/", page);
	}
	
	@PostMapping("/{customerId}/customer-vehicle-delete")
	public String deleteCustomerVehicle(@PathVariable Long customerId, @ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attr) {
		this.customerId = customerId;
		String page = super.deleteEntity(deleteDTO, model, attr);
		return resolveURL(customerId + "/", page);
	}
	
	@Override
	protected CustomerVehicleDTO getById(Long id) {
		return service.getCustomerVehicleById(id);
	}

	@Override
	protected Long getId(CustomerVehicleDTO dto) {
		return dto.getId();
	}

	@Override
	protected CustomerVehicleDTO manage(CustomerVehicleDTO dto) throws Exception {
		if(!CommonValidators.validLong(dto.getCustomerId())) {
			throw new Exception("Please select a customer!");
		}
		return service.manageCustomerVehicle(dto);
	}

	@Override
	protected boolean deleteById(Long id) throws Exception {
		return service.deleteCustomerVehicle(id, customerId);
	}

	@Override
	protected void commonModel(Model model, CustomerVehicleDTO dto) throws Exception {
		model.addAttribute("customerVehicleDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("customerVehicleList", service.getAllCustomerVehicle(customerId, null));
		model.addAttribute("customerDTO", this.customerService.getCustomerById(customerId));
		model.addAttribute("driverList", driverService.getAllDriver(ActiveStatus.ACTIVE.getCode()));
	}
	
	private String resolveURL(String prefix, String endpoint) {
	    if (endpoint != null && endpoint.startsWith("redirect:")) {
	        String redirect = endpoint.substring(0, endpoint.indexOf(":"));
	        String manage = endpoint.substring(endpoint.indexOf(":") + 1);
	        logger.debug("seperated: {} & {}", redirect, manage);
	        return redirect + ":/" + prefix + manage;
	    }
	    return (endpoint != null ? endpoint : "");
	}

}
