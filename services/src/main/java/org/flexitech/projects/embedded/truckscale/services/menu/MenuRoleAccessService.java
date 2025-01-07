package org.flexitech.projects.embedded.truckscale.services.menu;

import java.util.List;
import java.util.Map;

import org.flexitech.projects.embedded.truckscale.dto.menu.MenuAccessListDTO;
import org.flexitech.projects.embedded.truckscale.dto.menu.MenuAccessTreeDTO;
import org.flexitech.projects.embedded.truckscale.dto.menu.MenuDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;

public interface MenuRoleAccessService {
	Map<MenuDTO, List<MenuDTO>> getMenuRoleAccessListByRoleId(Long roleId);

	List<MenuAccessTreeDTO> getSelectedAccessTree(Long userRoleId);

	void saveMenuAccess(MenuAccessListDTO accessTreeDtoList, UserDTO loginUser);
}
