package org.flexitech.projects.embedded.truckscale.api.controller.shift;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.services.shift.UserShiftService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shift")
public class UserShiftController {
	
	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	UserShiftService shiftService;

	@SuppressWarnings("unchecked")
	@PostMapping("/start")
	public ResponseEntity<?> startShit(@RequestParam(required = true) Long userId) {
		Response response = null;
		try {
			UserShiftDTO shiftDTO = this.shiftService.startShift(userId);
			if (CommonValidators.isValidObject(shiftDTO)) {
				response = new BaseResponse<UserShiftDTO>();
				response.setResponseCode(HttpStatus.OK.value());
				response.setResponseMessage("User shift successfully started!");
				((BaseResponse<UserShiftDTO>) response).setData(shiftDTO);
			}
		} catch (Exception e) {
			logger.error("Fail to start shift: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			if(e instanceof IllegalArgumentException) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setResponseMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
			}else {
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setResponseMessage("Unexpect error occour!");
			}
			((ErrorResponse<String>) response).setError(ExceptionUtils.getMessage(e));
		}
		return ResponseUtil.send(response);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/end")
	public ResponseEntity<?> endShit(@RequestParam(required = true) Long userId, HttpServletRequest request) {
		Response response = null;
		try {
			UserDTO loggedUser = (UserDTO) request.getAttribute(CommonConstants.API_ACCESSOR);
			UserShiftDTO shiftDTO = this.shiftService.endShift(userId, loggedUser);
			if (CommonValidators.isValidObject(shiftDTO)) {
				response = new BaseResponse<UserShiftDTO>();
				response.setResponseCode(HttpStatus.OK.value());
				response.setResponseMessage("User shift successfully end!");
				((BaseResponse<UserShiftDTO>) response).setData(shiftDTO);
			}
		} catch (Exception e) {
			logger.error("Fail to end shift: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			if(e instanceof IllegalArgumentException) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setResponseMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
			}else {
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setResponseMessage("Unexpect error occour!");
			}
			((ErrorResponse<String>) response).setError(ExceptionUtils.getMessage(e));
		}
		return ResponseUtil.send(response);
	}

}
