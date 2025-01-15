package org.flexitech.projects.embedded.truckscale.dto.response.auth;

import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponse{
	private UserDTO user;
	private boolean isShiftStarted;
	private String token;
}
