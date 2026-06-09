package org.flexitech.projects.embedded.truckscale.superadmin.controllers;

import javax.servlet.http.HttpSession;

import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.auth.LoginDTO;
import org.flexitech.projects.embedded.truckscale.dto.superadminuser.SuperAdminUserDTO;
import org.flexitech.projects.embedded.truckscale.services.auth.AuthService;
import org.flexitech.projects.embedded.truckscale.services.menu.MenuRoleAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@Autowired
	AuthService authService;

	@Autowired
	MenuRoleAccessService menuRoleAccessService;

	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "ENL | Truck Scale Admin";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		LoginDTO loginDTO = new LoginDTO();
		model.addAttribute("loginDTO", loginDTO);
		return "login";
	}

	@PostMapping("/login")
	public String loginPost(@ModelAttribute LoginDTO loginDTO, Model model, HttpSession session,
			RedirectAttributes attr) {
		try {
			System.out.println("user name " + loginDTO.getLoginName());
			System.out.println("pass " + loginDTO.getPassword());
			SuperAdminUserDTO loggedSuperAdminUser = this.authService.loginForSuperAdminUser(loginDTO);
			System.out.println("user name"+ loggedSuperAdminUser.getLoginName());
			System.out.println("pass"+ loggedSuperAdminUser.getPassword());
			if (CommonValidators.isValidObject(loggedSuperAdminUser)) {
				session.setAttribute(CommonConstants.SESSION_LOGGED_USER, loggedSuperAdminUser);
				 session.setAttribute(CommonConstants.SESSION_USER_MENUS,
				 this.menuRoleAccessService.getMenuRoleAccessListByRoleId(loggedSuperAdminUser.getUserRoleDTO().getId()));
				attr.addFlashAttribute(CommonConstants.SUCCESS_MSG,
						"Welcome back <strong>" + loggedSuperAdminUser.getLoginName() + "<strong>.");
				return "redirect:dashboard.fxt";
			} else {
				model.addAttribute(CommonConstants.ERROR_MSG, "Login failed! Invalid credentials.");
			}
		} catch (Exception e) {
			model.addAttribute(CommonConstants.ERROR_MSG, e.getMessage());
		}
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(CommonConstants.SESSION_LOGGED_USER);
		session.removeAttribute(CommonConstants.SESSION_USER_MENUS);
		return "redirect:login.fxt";
	}

}
