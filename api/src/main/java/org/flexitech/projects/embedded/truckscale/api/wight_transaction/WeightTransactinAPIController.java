package org.flexitech.projects.embedded.truckscale.api.wight_transaction;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionPreloadDataResponse;
import org.flexitech.projects.embedded.truckscale.services.weight_transaction.WeightTransactionService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weight-transactions")
public class WeightTransactinAPIController {

	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	WeightTransactionService weightTransactionService;

	@SuppressWarnings("unchecked")
	@GetMapping("/preload-data")
	public ResponseEntity<?> getWeightTransactionPreloadData(@RequestParam(required = false) Long userId) {
		Response response = new Response();
		try {
			if (CommonValidators.validLong(userId)) {
				WeightTransactionPreloadDataResponse data = weightTransactionService
						.getWeightTransactionPreloadData(userId);
				response = new BaseResponse<WeightTransactionPreloadDataResponse>();
				response.setResponseCode(HttpStatus.OK.value());
				response.setResponseMessage("Getting preload data success");
				((BaseResponse<WeightTransactionPreloadDataResponse>) response).setData(data);
			} else {
				response = new ErrorResponse<String>();
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setResponseMessage("Please provide required id!");
			}
		} catch (Exception e) {
			logger.error("Error on getting weight transaction preload data: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponseMessage(ExceptionUtils.getMessage(e));
		}
		return ResponseUtil.send(response);
	}

}
