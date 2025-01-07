package org.flexitech.projects.embedded.truckscale.dao.user;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.user.UserRoles;

public interface UserRoleDAO extends CommonDAO<UserRoles, Long>{
	boolean isRoleCodeUsed(Integer code);
	List<UserRoles> getAllRoleByStatus(Integer status);
}
