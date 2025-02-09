package org.flexitech.projects.embedded.truckscale.api.controller.customer;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerTypeDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.customer.CustomerTypesListResponseDTO;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerTypeService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer-type")
public class CustomerTypeAPIController {
	
	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private CustomerTypeService customerTypeService;
	
	@SuppressWarnings("unchecked")
	@GetMapping("/get-by-customer")
	public ResponseEntity<?> getByCustomer(@RequestParam(required = false) Long id){
		Response response = new Response();
		try {
			List<CustomerTypeDTO> customerTypes = this.customerTypeService.getCustomerTypeByCustomerId(id);
			response = new BaseResponse<CustomerTypesListResponseDTO>();
			response.setResponseCode(HttpStatus.OK.value());
			response.setResponseMessage("Get customer types by customer success.");
			CustomerTypesListResponseDTO dto = new CustomerTypesListResponseDTO();
			dto.setCustomerTypes(customerTypes);
			((BaseResponse<CustomerTypesListResponseDTO>) response).setData(dto);
		}catch (Exception e) {
			logger.error("Error on getting customer type by customer: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			if(e instanceof IllegalArgumentException) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
			}
			response.setResponseMessage(ExceptionUtils.getMessage(e));
		}
		return ResponseUtil.send(response);
	}
	
	
}
