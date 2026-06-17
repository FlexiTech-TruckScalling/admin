package org.flexitech.projects.embedded.truckscale.dao.superadminuser;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SuperAdminUser;

public interface SuperAdminUserDAO extends CommonDAO<SuperAdminUser, Long> {
	SuperAdminUser findAdminUserByLoginName(String loginName);
	List<SuperAdminUser> getAllUserByStatus(Integer status);
	SuperAdminUser findSuperAdminUserBySessionToken(String sessionToken);
}
