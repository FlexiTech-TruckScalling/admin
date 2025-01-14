package org.flexitech.projects.embedded.truckscale.services.auth;

import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;

public interface JwtService {
	public String generateToken(UserDTO userDTO);
	public String extractSessionToken(String token);
	public boolean validateToken(String token, String sessionToken);
}
