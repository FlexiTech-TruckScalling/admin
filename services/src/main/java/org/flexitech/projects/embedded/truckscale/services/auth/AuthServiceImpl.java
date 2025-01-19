package org.flexitech.projects.embedded.truckscale.services.auth;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dao.user.UserDAO;
import org.flexitech.projects.embedded.truckscale.dto.auth.LoginDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.LoginRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.auth.AuthResponse;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.flexitech.projects.embedded.truckscale.services.shift.UserShiftService;
import org.flexitech.projects.embedded.truckscale.util.auth.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	UserShiftService shiftService;

	@Override
	public UserDTO login(LoginDTO loginDTO) throws Exception {
		try {
			Users user = this.userDAO.findUserByLoginName(loginDTO.getLoginName());
			if (user == null) {
				throw new Exception("Invalid login name!");
			}
			if (!CommonValidators.validString(loginDTO.getPassword())
					&& !CommonValidators.validString(user.getPassword())) {
				throw new Exception("Password cannot be empty!");
			}
			if (user.getPassword().equals(loginDTO.getPassword())) {
				return new UserDTO(user);
			}else {
				throw new Exception("Wrong password, please try again!");
			}
		} catch (Exception e) {
			logger.error("Error on Login: {}", ExceptionUtils.getMessage(e));
			throw e;
		}
	}

	@Override
	public void logout(UserDTO userDTO) {
		// TODO Auto-generated method stub
	}

	@Override
	public Response login(LoginRequestDTO loginRequestDTO) {
		try {
			LoginDTO loginDTO = new LoginDTO();
			loginDTO.setLoginName(loginRequestDTO.getLoginName());
			loginDTO.setPassword(loginRequestDTO.getPassword());
			UserDTO user = this.login(loginDTO);
			if(CommonValidators.isValidObject(user)) {
				
				if(!CommonValidators.isValidObject(user.getUserRoleDTO())
						||!ActiveStatus.ACTIVE.getCode().equals(user.getUserRoleDTO().getUseApp())) {
					ErrorResponse<String> error = new ErrorResponse<>();
					error.setResponseCode(HttpStatus.UNAUTHORIZED.value());
					error.setResponseMessage("User does not have access to use the app!");
					error.setError("User does not have access to use the app!");
					return error;
				}
				
				String sessionToken = TokenUtil.generateSessionToken(user.getLoginName());
				user.setSessionToken(sessionToken);
				Users u = this.userDAO.get(user.getId());
				u.setSessionToken(sessionToken);
				this.userDAO.update(u);
				String jwtToken = jwtService.generateToken(user);
				AuthResponse response = new AuthResponse();
				response.setToken(jwtToken);
				response.setUser(user);
				
				UserShiftDTO shift = this.shiftService.getCurrentActiveShift(u.getId());
				response.setShiftStarted(CommonValidators.isValidObject(shift));
				
				logger.debug("Shift status: " + CommonValidators.isValidObject(shift));
				
				BaseResponse<AuthResponse> res = new BaseResponse<AuthResponse>();
				res.setData(response);
				res.setResponseCode(HttpStatus.OK.value());
				res.setResponseMessage("Login success!");
				
				return res;
			}else {
				ErrorResponse<?> error = new ErrorResponse<>();
				error.setResponseCode(HttpStatus.BAD_REQUEST.value());
				error.setResponseMessage("Invalid credential!");
				return error;
			}
		}catch(Exception e) {
			logger.error("Error on login api: {}", ExceptionUtils.getStackTrace(e));
			ErrorResponse<?> error = new ErrorResponse<>();
			error.setResponseCode(HttpStatus.UNAUTHORIZED.value());
			error.setResponseMessage(ExceptionUtils.getMessage(e));
			return error;
		}
	}

	@Override
	public Response checkToken(String token) {
		String sessionToken = jwtService.extractSessionToken(token);
		if(jwtService.validateToken(token, sessionToken)) {
			Users user = this.userDAO.findUserBySessionToken(sessionToken);
			if(CommonValidators.isValidObject(user)) {
				UserShiftDTO activeShift = this.shiftService.getCurrentActiveShift(user.getId());
				if(CommonValidators.isValidObject(activeShift)) {
					BaseResponse<UserDTO> baseResponse = new BaseResponse<UserDTO>();
					baseResponse.setData(new UserDTO(user));
					baseResponse.setResponseCode(HttpStatus.OK.value());
					baseResponse.setResponseMessage(HttpStatus.OK.getReasonPhrase());
					return baseResponse;
				}else {
					ErrorResponse<String> error = new ErrorResponse<String>();
					error.setResponseCode(HttpStatus.UNAUTHORIZED.value());
					error.setResponseMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase());
					error.setResponseMessage("User has been logout or shift has changed! Please login again!");
					return error;
				}
			}else {
				ErrorResponse<String> error = new ErrorResponse<String>();
				error.setResponseCode(HttpStatus.UNAUTHORIZED.value());
				error.setResponseMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase());
				error.setResponseMessage("User has been logout or shift has changed! Please login again!");
				return error;
			}
			
		}else {
			ErrorResponse<String> error = new ErrorResponse<>();
			error.setResponseCode(HttpStatus.BAD_REQUEST.value());
			error.setResponseMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
			error.setError("Token is expired or invalid token!");
			return error;
		}
	}

}
