package org.flexitech.projects.embedded.truckscale.dto.request.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorizeRequestDTO {
	/* private Integer type; */
	private String name;
	private String password;
}
