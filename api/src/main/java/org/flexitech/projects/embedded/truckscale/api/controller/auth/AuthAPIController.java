package org.flexitech.projects.embedded.truckscale.api.controller.auth;

import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.request.auth.LoginRequestDTO;
import org.flexitech.projects.embedded.truckscale.services.auth.AuthService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthAPIController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO){
		Response response = authService.login(loginRequestDTO);
		return ResponseUtil.send(response);
	}
	
	@GetMapping("/token")
	public ResponseEntity<?> checkToken(@RequestParam(required = false) String token){
		return ResponseUtil.send(authService.checkToken(token));
	}
}
