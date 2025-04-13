package org.flexitech.projects.embedded.truckscale.services.auth;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dao.counter.CounterDAO;
import org.flexitech.projects.embedded.truckscale.dao.shift.UserShiftDAO;
import org.flexitech.projects.embedded.truckscale.dao.shift.UserShiftSummaryDAO;
import org.flexitech.projects.embedded.truckscale.dao.user.UserDAO;
import org.flexitech.projects.embedded.truckscale.dto.auth.LoginDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.AuthorizeRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.LoginRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.LogoutRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.user_shift.UserShiftSummaryRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.auth.AuthResponse;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShift;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShiftSummary;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.flexitech.projects.embedded.truckscale.services.shift.UserShiftService;
import org.flexitech.projects.embedded.truckscale.util.auth.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

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
	
	@Autowired
	UserShiftDAO userShiftDAO;
	
	@Autowired
	CounterDAO counterDAO;
	
	@Autowired
	UserShiftSummaryDAO userShiftSummaryDAO;

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
			} else {
				throw new Exception("Wrong password, please try again!");
			}
		} catch (Exception e) {
			logger.error("Error on Login: {}", ExceptionUtils.getMessage(e));
			throw e;
		}
	}

	@Override
	public void logout(LogoutRequestDTO logoutRequest, UserDTO loggedUser)  throws Exception{
		if(!CommonValidators.isValidObject(logoutRequest)) {
			throw new IllegalArgumentException("Please provide a valid request!");
		}
		if(!CommonValidators.validLong(logoutRequest.getUserId())) {
			throw new IllegalArgumentException("Invalid request!");
		}
		
		Users user = this.userDAO.get(logoutRequest.getUserId());
		if(!CommonValidators.isValidObject(user)) {
			throw new IllegalArgumentException("Please provide a valid user id!");
		}
		
		Users loggedU = this.userDAO.get(loggedUser.getId());
		
		user.setSessionToken("");
		
		// logout change session
		this.userDAO.update(user);
		
		if(logoutRequest.isEndShift()) {
			
			if(!CommonValidators.isValidObject(logoutRequest.getShiftSummary())) {
				throw new IllegalArgumentException("Please provide shift summary to end shift!");
			}
			
			UserShift shift = this.userShiftDAO.getCurrentActiveShift(user.getId());
			
			if(!CommonValidators.isValidObject(shift)) {
				throw new IllegalArgumentException("Current user doesn't have active shift!");
			}
			
			// end shift
			this.shiftService.endShift(user.getId(), loggedUser);
			
			UserShiftSummaryRequestDTO summary = logoutRequest.getShiftSummary();
			
			UserShiftSummary s = new UserShiftSummary();
			
			Users sUser = this.userDAO.get(summary.getUserId());
			s.setUser(sUser);
			
			UserShift sShift = this.userShiftDAO.getUserShitByCode(summary.getShiftCode());
			s.setUserShift(sShift);
			
			Counters c = this.counterDAO.get(summary.getCounterId());
			s.setCounter(c);
			
			s.setTotalTransaction(summary.getTotalTransaction());
			s.setTotalInTransaction(summary.getTotalInTransaction());
			s.setTotalOutTransaction(summary.getTotalOutTranaction());
			s.setTotalAmount(summary.getTotalAmount());
			s.setEndByUser(loggedU);
			s.setCreatedTime(new Date());
			s.setStatus(ActiveStatus.ACTIVE.getCode());
			
			this.userShiftSummaryDAO.save(s);
		}
		
	}

	@Override
	public Response login(LoginRequestDTO loginRequestDTO) {
		try {
			LoginDTO loginDTO = new LoginDTO();
			loginDTO.setLoginName(loginRequestDTO.getLoginName());
			loginDTO.setPassword(loginRequestDTO.getPassword());
			UserDTO user = this.login(loginDTO);
			if (CommonValidators.isValidObject(user)) {

				if (!CommonValidators.isValidObject(user.getUserRoleDTO())
						|| !ActiveStatus.ACTIVE.getCode().equals(user.getUserRoleDTO().getUseApp())) {
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
				u.setLastLoginTime(new Date());
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
			} else {
				ErrorResponse<?> error = new ErrorResponse<>();
				error.setResponseCode(HttpStatus.BAD_REQUEST.value());
				error.setResponseMessage("Invalid credential!");
				return error;
			}
		} catch (Exception e) {
			logger.error("Error on login api: {}", ExceptionUtils.getStackTrace(e));
			ErrorResponse<?> error = new ErrorResponse<>();
			error.setResponseCode(HttpStatus.UNAUTHORIZED.value());
			error.setResponseMessage(ExceptionUtils.getMessage(e));
			return error;
		}
	}

	@Override
	public Response checkToken(String token) {
		try {
			String sessionToken = jwtService.extractSessionToken(token);
			if (jwtService.validateToken(token, sessionToken)) {
				Users user = this.userDAO.findUserBySessionToken(sessionToken);
				if (CommonValidators.isValidObject(user)) {
					UserShiftDTO activeShift = this.shiftService.getCurrentActiveShift(user.getId());
					if (CommonValidators.isValidObject(activeShift)) {
						BaseResponse<UserDTO> baseResponse = new BaseResponse<UserDTO>();
						baseResponse.setData(new UserDTO(user));
						baseResponse.setResponseCode(HttpStatus.OK.value());
						baseResponse.setResponseMessage(HttpStatus.OK.getReasonPhrase());
						return baseResponse;
					} else {
						ErrorResponse<String> error = new ErrorResponse<String>();
						error.setResponseCode(HttpStatus.UNAUTHORIZED.value());
						error.setResponseMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase());
						error.setResponseMessage("User has been logout or shift has changed! Please login again!");
						return error;
					}
				} else {
					ErrorResponse<String> error = new ErrorResponse<String>();
					error.setResponseCode(HttpStatus.UNAUTHORIZED.value());
					error.setResponseMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase());
					error.setResponseMessage("User has been logout or shift has changed! Please login again!");
					return error;
				}

			} else {
				ErrorResponse<String> error = new ErrorResponse<>();
				error.setResponseCode(HttpStatus.BAD_REQUEST.value());
				error.setResponseMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
				error.setError("Token is expired or invalid token!");
				return error;
			}
		} catch (Exception e) {
			logger.error("Error on checking token: {}", ExceptionUtils.getStackTrace(e));
			ErrorResponse<String> error = new ErrorResponse<String>();
			if (e instanceof ExpiredJwtException || e instanceof JwtException) {
				error.setResponseCode(HttpStatus.UNAUTHORIZED.value());
			} else {
				error.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			error.setResponseMessage(ExceptionUtils.getMessage(e));
			return error;
		}
	}

	@Override
	public boolean authorize(AuthorizeRequestDTO requestDTO) throws Exception{
		LoginDTO login = new LoginDTO();
		login.setLoginName(requestDTO.getName());
		login.setPassword(requestDTO.getPassword());
		UserDTO user = this.login(login);
		if(!CommonValidators.isValidObject(user)) return false;
		if(!user.getUserRoleDTO().getCode().equals(2) && !user.getUserRoleDTO().getCode().equals(1)) {
			throw new IllegalArgumentException("Only manager or admin can approve.");
		}
		return true;
	}

}
