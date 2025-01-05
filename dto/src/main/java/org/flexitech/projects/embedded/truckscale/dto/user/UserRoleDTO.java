package org.flexitech.projects.embedded.truckscale.dto.user;

import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.user.UserRoles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO extends CommonDTO{

	private Integer code;
	private String name;
	private String description;
	
	public UserRoleDTO(UserRoles role) {
		this.code = role.getCode();
		this.name = role.getName();
		this.description = role.getDescription();
		
		setField(role);
	}
	
}
