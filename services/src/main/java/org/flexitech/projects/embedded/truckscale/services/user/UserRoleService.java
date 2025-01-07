package org.flexitech.projects.embedded.truckscale.services.user;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.user.UserRoleDTO;

public interface UserRoleService {
	UserRoleDTO getRoleById(Long id);
	UserRoleDTO manageRole(UserRoleDTO userRoleDTO);
	List<UserRoleDTO> getAllRoles();
}
