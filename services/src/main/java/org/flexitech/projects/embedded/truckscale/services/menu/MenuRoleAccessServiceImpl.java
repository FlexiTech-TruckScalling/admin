package org.flexitech.projects.embedded.truckscale.services.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.menu.MenuDAO;
import org.flexitech.projects.embedded.truckscale.dao.menu.MenuRoleAccessDAO;
import org.flexitech.projects.embedded.truckscale.dao.user.UserRoleDAO;
import org.flexitech.projects.embedded.truckscale.dto.menu.MenuAccessListDTO;
import org.flexitech.projects.embedded.truckscale.dto.menu.MenuAccessTreeDTO;
import org.flexitech.projects.embedded.truckscale.dto.menu.MenuDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.menu.Menu;
import org.flexitech.projects.embedded.truckscale.entities.menu.MenuRoleAccess;
import org.flexitech.projects.embedded.truckscale.entities.user.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MenuRoleAccessServiceImpl implements MenuRoleAccessService {

	@Autowired
	MenuRoleAccessDAO menuRoleAccessDAO;

	@Autowired
	MenuDAO menuDAO;

	@Autowired
	UserRoleDAO userRoleDAO;

//	private final Logger logger = LogManager.getLogger(getClass());

	@Override
	public Map<MenuDTO, List<MenuDTO>> getMenuRoleAccessListByRoleId(Long roleId) {
		Map<MenuDTO, List<MenuDTO>> result = new LinkedHashMap<MenuDTO, List<MenuDTO>>();

//		List<MenuRoleAccess> parentMenus = this.menuRoleAccessDAO.getParentMenusByRoleId(roleId);
		List<Menu> parentMenus = this.menuDAO.getParentMenu();
		for (Menu access : parentMenus) {
			MenuDTO parentMenu = new MenuDTO(access);
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
					result.put(parentMenu, childMenus);
				}
			}
		}

		return result;
	}

	@Override
	public List<MenuAccessTreeDTO> getSelectedAccessTree(Long userRoleId) {
		List<Menu> allMenus = this.menuDAO.getAllByStatus(ActiveStatus.ACTIVE.getCode());
		List<MenuRoleAccess> userMenus = this.menuRoleAccessDAO.getAllMenuRoleAccess(userRoleId,
				ActiveStatus.ACTIVE.getCode());

		List<MenuAccessTreeDTO> resultTree = generateMenuAccessTree(allMenus, userMenus);

		return resultTree;
	}

	private List<MenuAccessTreeDTO> generateMenuAccessTree(List<Menu> all, List<MenuRoleAccess> selected) {
		if (!CommonValidators.validList(all)) {
			return new ArrayList<>();
		}

		List<MenuAccessTreeDTO> resultTree = new ArrayList<>();
		Map<String, List<MenuAccessTreeDTO>> recordParent = new LinkedHashMap<>();

		for (Menu access : all) {
			MenuAccessTreeDTO currentTree = new MenuAccessTreeDTO();
			currentTree.setShotName(access.getCode());
			currentTree.setId(access.getId());
			currentTree.setHasChildren(false);
			currentTree.setText(access.getName());

			// parent and child
			if (CommonValidators.validInteger(access.getParentStatus())
					&& ActiveStatus.ACTIVE.getCode().equals(access.getParentStatus())) {
				currentTree.setHasChildren(true);
				if (!recordParent.containsKey(access.getCode())) {
					recordParent.put(access.getCode(), new ArrayList<MenuAccessTreeDTO>());
				}
				resultTree.add(currentTree);
			} else if (CommonValidators.validString(access.getParentMenuCode())) {
				String parentCode = access.getParentMenuCode();
				if (recordParent.containsKey(parentCode)) {
					recordParent.get(parentCode).add(currentTree);
				} else {
					List<MenuAccessTreeDTO> child = new ArrayList<MenuAccessTreeDTO>();
					child.add(currentTree);
					recordParent.put(parentCode, child);
				}
			} else {
				resultTree.add(currentTree);
			}

			// Check if the menu is selected
			if (CommonValidators.validList(selected)) {
				currentTree.setChecked(selected.stream().anyMatch(sel -> sel.getMenu().getId().equals(access.getId())));
			}

		}

		for (Map.Entry<String, List<MenuAccessTreeDTO>> entry : recordParent.entrySet()) {
			String parent = entry.getKey();
			resultTree.stream().filter(c -> c.getShotName().equals(parent))
					.forEach(m -> m.getChildren().addAll(entry.getValue()));
		}

		return resultTree;
	}

	@Override
	public void saveMenuAccess(MenuAccessListDTO accessTreeDtoList, UserDTO loginUser) {
		Long roleId = accessTreeDtoList.getRoleId();
		List<String> newAccessTreeList = accessTreeDtoList.getAccessTreeList();

		List<MenuRoleAccess> existingAccessList = menuRoleAccessDAO.getAllMenuRoleAccess(roleId,
				ActiveStatus.ACTIVE.getCode());

		Set<String> existingMenuIds = existingAccessList.stream().map(access -> access.getMenu().getCode())
				.collect(Collectors.toSet());

		Set<String> newMenuIds = new HashSet<>(newAccessTreeList);

		Set<String> menusToAdd = new HashSet<>(newMenuIds);
		menusToAdd.removeAll(existingMenuIds);

		Set<String> menusToRemove = new HashSet<>(existingMenuIds);
		menusToRemove.removeAll(newMenuIds);

		if (!menusToRemove.isEmpty()) {
			List<MenuRoleAccess> accessesToRemove = existingAccessList.stream()
					.filter(access -> menusToRemove.contains(access.getMenu().getCode()))
					.collect(Collectors.toList());

			for (MenuRoleAccess ac : accessesToRemove) {
				menuRoleAccessDAO.delete(ac);
			}
		}

		// 6. Add new menu accesses
		if (!menusToAdd.isEmpty()) {
			List<MenuRoleAccess> accessesToAdd = menusToAdd.stream().map(menuId -> {
				Menu menu = menuDAO.getByCode(menuId);

				UserRoles role = userRoleDAO.get(roleId);

				MenuRoleAccess newAccess = new MenuRoleAccess();
				newAccess.setMenu(menu);
				newAccess.setRole(role);
				newAccess.setCreatedTime(new Date());
				newAccess.setStatus(ActiveStatus.ACTIVE.getCode());
				return newAccess;
			}).collect(Collectors.toList());

			for (MenuRoleAccess access : accessesToAdd) {
				this.menuRoleAccessDAO.saveOrUpdate(access);
			}
		}
	}

}
