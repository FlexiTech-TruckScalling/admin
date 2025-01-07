package org.flexitech.projects.embedded.truckscale.dao.menu;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.menu.MenuRoleAccess;

public interface MenuRoleAccessDAO extends CommonDAO<MenuRoleAccess, Long> {
	List<MenuRoleAccess> getParentMenusByRoleId(Long roleId);
	List<MenuRoleAccess> getChildMenuByParentMenuIdAndRoleId(String parentMenuCode, Long roleId);
	List<MenuRoleAccess> getAllMenuRoleAccess(Long roleId, Integer status);
}
