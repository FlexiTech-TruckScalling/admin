package org.flexitech.projects.embedded.truckscale.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDTO {
	
	@JsonProperty("login_name")
	private String loginName;
	
	@JsonProperty("password")
	private String password;
}
