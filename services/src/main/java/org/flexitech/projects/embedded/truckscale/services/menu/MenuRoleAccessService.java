package org.flexitech.projects.embedded.truckscale.services.menu;

import java.util.List;
import java.util.Map;

import org.flexitech.projects.embedded.truckscale.dto.menu.MenuDTO;

public interface MenuRoleAccessService {
	Map<MenuDTO, List<MenuDTO>> getMenuRoleAccessListByRoleId(Long roleId);
}
