package org.flexitech.projects.embedded.truckscale.services.menu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.menu.MenuRoleAccessDAO;
import org.flexitech.projects.embedded.truckscale.dto.menu.MenuDTO;
import org.flexitech.projects.embedded.truckscale.entities.menu.MenuRoleAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MenuRoleAccessServiceImpl implements MenuRoleAccessService {

	@Autowired
	MenuRoleAccessDAO menuRoleAccessDAO;

	@Override
	public Map<MenuDTO, List<MenuDTO>> getMenuRoleAccessListByRoleId(Long roleId) {
		Map<MenuDTO, List<MenuDTO>> result = new LinkedHashMap<MenuDTO, List<MenuDTO>>();

		List<MenuRoleAccess> parentMenus = this.menuRoleAccessDAO.getParentMenusByRoleId(roleId);

		for (MenuRoleAccess access : parentMenus) {
			MenuDTO parentMenu = new MenuDTO(access.getMenu());
			if (CommonValidators.isValidObject(parentMenu)) {
				List<MenuDTO> childMenus = new ArrayList<MenuDTO>();
				List<MenuRoleAccess> childMenuRoleAccess = this.menuRoleAccessDAO
						.getChildMenuByParentMenuIdAndRoleId(parentMenu.getCode(), roleId);
				if (CommonValidators.validList(childMenuRoleAccess)) {
					for (MenuRoleAccess childAccess : childMenuRoleAccess) {
						if (CommonValidators.isValidObject(childAccess)
								&& CommonValidators.isValidObject(childAccess.getMenu())) {
							childMenus.add(new MenuDTO(childAccess.getMenu()));
						}
					}
				}
				result.put(parentMenu, childMenus);
			}
		}

		return result;
	}

}
