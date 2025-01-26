package org.flexitech.projects.embedded.truckscale.api.controller.counter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.counter.CounterSettingResponse;
import org.flexitech.projects.embedded.truckscale.services.counter.CounterService;
import org.flexitech.projects.embedded.truckscale.services.counter.CounterSettingService;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerTypeService;
import org.flexitech.projects.embedded.truckscale.services.setting.WeightUnitService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
public class CounterAPIController {
	
	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	CounterSettingService counterSettingService;
	
	@Autowired
	CounterService counterService;
	
	@Autowired
	WeightUnitService weightUnitService;
	
	@Autowired
	CustomerTypeService customerTypeService;
	
	@SuppressWarnings("unchecked")
	@GetMapping("/setting")
	public ResponseEntity<?> getCounterSetting(@RequestParam(required = false) Long id){
		
		Response response = new Response();
		
		logger.debug("Getting counter setting by counter id: {}", id);
		
		if(CommonValidators.validLong(id)) {
			CounterSettingResponse settingResponse = new CounterSettingResponse();
			CounterDTO counter = this.counterSettingService.getCounterSettingByCounterId(id);
			
			if(CommonValidators.isValidObject(counter)) {
				settingResponse.setCounter(counter);
				settingResponse.setUnits(this.weightUnitService.getAllWeightUnit(ActiveStatus.ACTIVE.getCode()));
				settingResponse.setBounds(InOutBounds.getAll());
				settingResponse.setCustomerTypes(this.customerTypeService.getAllCustomerTypes(ActiveStatus.ACTIVE.getCode()));
				
				response = new BaseResponse<CounterSettingResponse>();
				response.setResponseCode(HttpStatus.OK.value());
				response.setResponseMessage("Getting counter setting success!");
				((BaseResponse<CounterSettingResponse>) response).setData(settingResponse);
			}else {
				response = new ErrorResponse<String>();
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setResponseMessage("No associated counter found by the provided id!");
				logger.error("No associated counter found by the provided id!");
			}
		}else {
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.BAD_REQUEST.value());
			response.setResponseMessage("Please provide the counter id!");
			logger.error("Please provide the counter id!");
		}
		
		return ResponseUtil.send(response);
	}
	
}
