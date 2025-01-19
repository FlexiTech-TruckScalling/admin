package org.flexitech.projects.embedded.truckscale.common.enums;

import lombok.Getter;

@Getter
public enum APIResponceCode {
	OK(1, "Success"),
	ERROR(-1, "Error"),
	UNAUTHORIZED(-2, "Unauthorized");

	private final int code;
	private final String desc;
	
	APIResponceCode(int i, String string) {
		code = i;
		desc = string;
	}
}
