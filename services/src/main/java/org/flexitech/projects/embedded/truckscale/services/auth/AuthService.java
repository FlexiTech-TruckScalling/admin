package org.flexitech.projects.embedded.truckscale.services.auth;

import org.flexitech.projects.embedded.truckscale.dto.auth.LoginDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;

public interface AuthService {
	UserDTO login(LoginDTO loginDTO) throws Exception;
	void logout(UserDTO userDTO);
}
