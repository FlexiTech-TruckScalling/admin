package org.flexitech.projects.embedded.truckscale.common.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum LincenseKeyStatus {
	EXPIRED(1, "Expried"), ACTIVE(2, "Active"), BAN(3, "Ban");

	private final Integer code;
	private final String desc;
	
	private LincenseKeyStatus(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static List<EnumObjects> getAll() {
		List<EnumObjects> result = new ArrayList<EnumObjects>();
		for (LincenseKeyStatus s : values()) {
			result.add(new EnumObjects(s.code, s.desc));
		}
		return result;
	}

	public static String getDescByCode(Integer code) {

		for (LincenseKeyStatus s : values()) {
			if (s.code.equals(code))
				return s.desc;
		}

		return null;
	}
}
