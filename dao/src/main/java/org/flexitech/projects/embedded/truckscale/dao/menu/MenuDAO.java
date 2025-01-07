package org.flexitech.projects.embedded.truckscale.dao.menu;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.menu.Menu;

public interface MenuDAO extends CommonDAO<Menu, Long> {
	Menu getByCode(String code);
	List<Menu> getAllByStatus(Integer status);
	List<Menu> getParentMenu();
}
