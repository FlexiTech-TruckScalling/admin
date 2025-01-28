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
import org.flexitech.projects.embedded.truckscale.dto.shift.CurrentShiftSummaryDTO;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.services.shift.UserShiftService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shift")
public class UserShiftAPIController {

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
			if (e instanceof IllegalArgumentException) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setResponseMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
			} else {
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
			if (e instanceof IllegalArgumentException) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setResponseMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
			} else {
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setResponseMessage("Unexpect error occour!");
			}
			((ErrorResponse<String>) response).setError(ExceptionUtils.getMessage(e));
		}
		return ResponseUtil.send(response);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/active-shift-summary")
	public ResponseEntity<?> getCurrentShiftSummary(@RequestParam(required = false) Long userId,
			@RequestParam(required = false) String sessionCode) {
		Response response = new Response();
		if (!CommonValidators.validLong(userId) || !CommonValidators.validString(sessionCode)) {
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.BAD_REQUEST.value());
			response.setResponseMessage("Invalid request!");
		} else {
			try {
				CurrentShiftSummaryDTO dto = this.shiftService.getCurrentShiftSummary(userId, sessionCode);
				response = new BaseResponse<CurrentShiftSummaryDTO>();
				response.setResponseCode(HttpStatus.OK.value());
				response.setResponseMessage("Succes.");
				((BaseResponse<CurrentShiftSummaryDTO>) response).setData(dto);
			} catch (Exception e) {
				logger.error("Error on getting active shift summary: {}", ExceptionUtils.getStackTrace(e));
				response = new ErrorResponse<String>();
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setResponseMessage("Failed to get summary data!");
			}
		}
		return ResponseUtil.send(response);
	}

}
