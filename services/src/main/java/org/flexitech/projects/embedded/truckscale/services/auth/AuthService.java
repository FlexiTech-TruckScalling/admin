package org.flexitech.projects.embedded.truckscale.services.auth;

import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.auth.LoginDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.AuthorizeRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.LoginRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.LogoutRequestDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;

public interface AuthService {
	UserDTO login(LoginDTO loginDTO) throws Exception;
	void logout(LogoutRequestDTO logoutRequest, UserDTO loggedUser) throws Exception;
	Response login(LoginRequestDTO loginRequestDTO); 
	Response checkToken(String token);
	boolean authorize(AuthorizeRequestDTO requestDTO) throws Exception;
}
