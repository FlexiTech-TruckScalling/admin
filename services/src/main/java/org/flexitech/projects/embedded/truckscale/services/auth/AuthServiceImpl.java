package org.flexitech.projects.embedded.truckscale.services.auth;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.user.UserDAO;
import org.flexitech.projects.embedded.truckscale.dto.auth.LoginDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	UserDAO userDAO;

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

}
