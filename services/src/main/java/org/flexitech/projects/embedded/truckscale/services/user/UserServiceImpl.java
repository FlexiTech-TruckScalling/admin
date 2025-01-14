package org.flexitech.projects.embedded.truckscale.services.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.counter.CounterDAO;
import org.flexitech.projects.embedded.truckscale.dao.user.UserDAO;
import org.flexitech.projects.embedded.truckscale.dao.user.UserRoleDAO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.flexitech.projects.embedded.truckscale.entities.user.UserRoles;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserRoleDAO userRoleDAO;

	@Autowired
	private CounterDAO counterDAO;

	@Override
	public UserDTO getUserById(Long id) {
		Users user = this.userDAO.get(id);
		if (CommonValidators.isValidObject(user)) {
			return new UserDTO(user);
		}
		return null;
	}

	@Override
	public UserDTO manageUser(UserDTO userDTO) {
		if (!CommonValidators.isValidObject(userDTO))
			return null;

		Users user = new Users();

		if (CommonValidators.validLong(userDTO.getId())) {
			user = this.userDAO.get(userDTO.getId());
			if (!CommonValidators.isValidObject(user)) {
				logger.error("Try to update user but user not found!");
			}
			user.setUpdatedTime(new Date());
		} else {
			user.setCreatedTime(new Date());
		}
		user.setName(userDTO.getName());
		user.setLoginName(userDTO.getLoginName());
		user.setPhoneNo(userDTO.getPhoneNo());
		user.setPassword(userDTO.getPassword());
		user.setStatus(userDTO.getStatus());

		if (CommonValidators.validLong(userDTO.getCounterId())) {
			Counters counter = this.counterDAO.get(userDTO.getCounterId());
			user.setCounter(counter);
		}
		if (CommonValidators.validLong(userDTO.getUserRoleId())) {
			UserRoles userRoles = this.userRoleDAO.get(userDTO.getUserRoleId());
			user.setUserRole(userRoles);
		}
		this.userDAO.saveOrUpdate(user);

		return new UserDTO(user);
	}

	@Override
	public List<UserDTO> getAllUsers(Integer status) {
		List<Users> users = this.userDAO.getAllUserByStatus(status);
		if (CommonValidators.validList(users)) {
			return users.stream().map(UserDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<UserDTO>();
	}

	@Override
	public boolean deleteUser(Long id) throws Exception {
		Users user = this.userDAO.get(id);
		try {
			if (CommonValidators.isValidObject(user)) {
				this.userDAO.delete(user);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("Fail to delete user: {}", ExceptionUtils.getStackTrace(e));
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public UserDTO findUserBySessionToken(String sessionToken) {
		Users user = this.userDAO.findUserBySessionToken(sessionToken);
		if(CommonValidators.isValidObject(user)) {
			return new UserDTO(user);
		}
		return null;
	}

}
