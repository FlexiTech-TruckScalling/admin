package org.flexitech.projects.embedded.truckscale.api.controller.user;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerSearchDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.customer.CustomerSearchReponseDTO;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerAPIController {

	@Autowired
	CustomerService customerService;

	@SuppressWarnings("unchecked")
	@PostMapping("/search")
	public ResponseEntity<?> searchCustomer(@RequestBody CustomerSearchDTO searchDTO) {

		Response response = new Response();
		searchDTO.setPageNo(1);
		
		try {
			List<CustomerDTO> customers = this.customerService.searchCustomers(searchDTO);
			CustomerSearchReponseDTO data = new CustomerSearchReponseDTO();
			data.setDataList(customers);
			response = new BaseResponse<CustomerSearchReponseDTO>();
			((BaseResponse<CustomerSearchReponseDTO>) response).setData(data);
			response.setResponseCode(HttpStatus.OK.value());
			response.setResponseMessage("Search customer success");
		} catch (Exception e) {
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponseMessage(ExceptionUtils.getMessage(e));
		}
		return ResponseUtil.send(response);
	}
}
