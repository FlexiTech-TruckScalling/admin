package org.flexitech.projects.embedded.truckscale.dao.user;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;

public interface UserDAO extends CommonDAO<Users, Long> {
	Users findUserByLoginName(String loginName);
	List<Users> getAllUserByStatus(Integer status);
}
