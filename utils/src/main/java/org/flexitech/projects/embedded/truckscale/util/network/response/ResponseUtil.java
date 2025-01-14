package org.flexitech.projects.embedded.truckscale.util.network.response;

import org.flexitech.projects.embedded.truckscale.common.enums.APIResponceCode;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
	public static ResponseEntity<?> send(Response body) {
		Integer code = body.getResponseCode();
		if (code == HttpStatus.OK.value()) {
			body.setResponseCode(APIResponceCode.OK.getCode());
			return ResponseEntity.ok(body);
		} else {
			body.setResponseCode(APIResponceCode.ERROR.getCode());
			return ResponseEntity.status(code).body(body);
		}
	}
}
