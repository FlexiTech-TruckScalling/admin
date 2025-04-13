package org.flexitech.projects.embedded.truckscale.api.controller.auth;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.AuthorizeRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.LoginRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.LogoutRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.services.auth.AuthService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthAPIController {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
		Response response = authService.login(loginRequestDTO);
		return ResponseUtil.send(response);
	}

	@GetMapping("/token")
	public ResponseEntity<?> checkToken(@RequestParam(required = false) String token) {
		return ResponseUtil.send(authService.checkToken(token));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody LogoutRequestDTO request, HttpServletRequest req) {
		Response response = new Response();
		try {
			UserDTO loggedUser = (UserDTO) req.getAttribute(CommonConstants.API_ACCESSOR);
			if (CommonValidators.isValidObject(loggedUser)) {
				this.authService.logout(request, loggedUser);
			}
			response = new BaseResponse<String>();
			response.setResponseCode(HttpStatus.OK.value());
			response.setResponseMessage("Logout successfully!");
		} catch (Exception e) {
			logger.error("Error on logout: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			if (e instanceof IllegalArgumentException) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
			} else {
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			response.setResponseMessage(ExceptionUtils.getMessage(e));
		}
		return ResponseUtil.send(response);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/authorize")
	public ResponseEntity<?> authorize(@RequestBody AuthorizeRequestDTO request) {
		Response response = new Response();
		try {
			boolean authorized = this.authService.authorize(request);
			response = new BaseResponse<Boolean>();
			response.setResponseCode(authorized ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value());
			response.setResponseMessage(authorized ? "Authorize success": "Authorize failed!");
			((BaseResponse<Boolean>) response).setData(authorized);
		}catch (Exception e) {
			logger.error("Error on logout: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			if (e instanceof IllegalArgumentException) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
			} else {
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			response.setResponseMessage(e.getMessage());
		}
		return ResponseUtil.send(response);
	}
}
