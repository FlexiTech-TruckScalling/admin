package org.flexitech.projects.embedded.truckscale.api.controller.health;

import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

	@GetMapping
	public ResponseEntity<?> checkHealth(){
		BaseResponse<String> response = new BaseResponse<String>();
		response.setData("Server is online.");
		response.setResponseCode(HttpStatus.OK.value());
		response.setResponseMessage("Server is online.");
		return ResponseUtil.send(response);
	}
	
}
