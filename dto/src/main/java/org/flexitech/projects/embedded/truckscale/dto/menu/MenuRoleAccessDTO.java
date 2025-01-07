package org.flexitech.projects.embedded.truckscale.dto.menu;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.menu.MenuRoleAccess;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuRoleAccessDTO extends CommonDTO{
	private Long userRoleId;
	private MenuDTO menuDTO;
	
	public MenuRoleAccessDTO(MenuRoleAccess access) {
		if(CommonValidators.isValidObject(access)) {
			this.userRoleId = access.getRole().getId();
			this.menuDTO = new MenuDTO(access.getMenu());
			setField(access);
		}
	}
}
