package org.flexitech.projects.embedded.truckscale.admin.controllers.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.menu.MenuAccessListDTO;
import org.flexitech.projects.embedded.truckscale.dto.menu.MenuAccessTreeDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.services.menu.MenuRoleAccessService;
import org.flexitech.projects.embedded.truckscale.services.user.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Controller
public class MenuAccessController {

	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "ENL | Truck Scale Menu Access";
	}
	
	private final Logger logger = LogManager.getLogger(MenuAccessController.class);

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private MenuRoleAccessService menuRoleAccessService;

	@GetMapping("/menu-access-manage")
	public String setupRoleAccess(Model model, HttpServletRequest httpRequest) {

		logger.info("Role access setup => [GET] start ");

		UserDTO sessionUser = (UserDTO) httpRequest.getSession().getAttribute(CommonConstants.SESSION_LOGGED_USER);

		List<MenuAccessTreeDTO> accessTreeList = menuRoleAccessService.getSelectedAccessTree(sessionUser.getUserRoleId());
		MenuAccessListDTO accessTreeDtoList = new MenuAccessListDTO();
		accessTreeDtoList.setRoleId(sessionUser.getUserRoleId());

		model.addAttribute("roleList", userRoleService.getAllRoles(ActiveStatus.ACTIVE.getCode()));
		model.addAttribute("accessTreeDtoList", accessTreeDtoList);
		model.addAttribute("accessTreeList", new Gson().toJson(accessTreeList));

		logger.info("Role access setup => [GET] finish ");

		return "menu-access-manage";

	}

	@GetMapping("/get_tree_json.json")
	public @ResponseBody String getTreeJson(Model model, @RequestParam(value = "roleId", required = false) Long roleId,
			HttpServletRequest request) {

		logger.info("get_tree_json.json => start ");

		UserDTO loginUser = (UserDTO) request.getSession().getAttribute(CommonConstants.SESSION_LOGGED_USER);

		ObjectMapper mapper = new ObjectMapper();
		String jsonResponse = null;
		List<MenuAccessTreeDTO> accessTreeList = menuRoleAccessService.getSelectedAccessTree(loginUser.getUserRoleId());
		if (roleId != null && roleId != 0l) {
			accessTreeList = menuRoleAccessService.getSelectedAccessTree(roleId);
		}

		try {
			jsonResponse = mapper.writeValueAsString(accessTreeList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error("Exception occurred while calling get_tree_json.json => Exception => "
					+ ExceptionUtils.getStackTrace(e));
		}

		logger.info("get_tree_json.json => finish ");

		return jsonResponse;
	}

	@PostMapping(value = "/menu-access-manage")
	public String saveRoleAccessRight(@ModelAttribute("accessTreeDtoList") MenuAccessListDTO accessTreeDtoList,
			Model model, BindingResult result, HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {

		logger.info("Role access setup => [Post] start ");

		try {

			UserDTO loginUser = (UserDTO) request.getSession().getAttribute(CommonConstants.SESSION_LOGGED_USER);

			menuRoleAccessService.saveMenuAccess(accessTreeDtoList, loginUser);
			session.setAttribute(CommonConstants.SESSION_USER_MENUS, this.menuRoleAccessService.getMenuRoleAccessListByRoleId(loginUser.getUserRoleDTO().getId()));
			attributes.addFlashAttribute(CommonConstants.SUCCESS_MSG,"Role Access created successfully.");

		} catch (Exception e) {
			logger.error("Exception occurred while creating role access...." + ExceptionUtils.getStackTrace(e));
			attributes.addFlashAttribute(CommonConstants.ERROR_MSG, "Role Access creating failed.");
		}

		logger.info("Role access setup => [Post] finish ");
		return "redirect:/menu-access-manage.fxt";
	}
	
}
