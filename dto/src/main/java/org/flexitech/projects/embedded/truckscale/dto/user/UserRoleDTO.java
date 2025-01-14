package org.flexitech.projects.embedded.truckscale.dto.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.menu.MenuRoleAccessDTO;
import org.flexitech.projects.embedded.truckscale.entities.user.UserRoles;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore
	private List<MenuRoleAccessDTO> menuRoleAccessDTOs = new ArrayList<MenuRoleAccessDTO>(); 
	
	public UserRoleDTO(UserRoles role) {
		this.code = role.getCode();
		this.name = role.getName();
		this.description = role.getDescription();
		if(CommonValidators.validList(role.getMenuRoleAccesses())) {
			this.menuRoleAccessDTOs = role.getMenuRoleAccesses().stream().map(MenuRoleAccessDTO::new).collect(Collectors.toList());
		}
		setField(role);
	}
	
}
