package org.flexitech.projects.embedded.truckscale.services.user;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;

public interface UserService {
	UserDTO getUserById(Long id);
	UserDTO manageUser(UserDTO userRoleDTO);
	List<UserDTO> getAllUsers(Integer status);
	boolean deleteUser(Long id) throws Exception;
}