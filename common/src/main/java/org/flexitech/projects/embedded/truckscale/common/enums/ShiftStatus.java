package org.flexitech.projects.embedded.truckscale.common.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum ShiftStatus {
	OPEN(1, "Opened"), CLOSE(2, "Closed");

	private final Integer code;
	
	private final String desc;
	
	ShiftStatus(int i, String string) {
		code = i;
		desc = string;
	}
	
	public static List<EnumObjects> getAll() {
		List<EnumObjects> result = new ArrayList<EnumObjects>();
		for (ShiftStatus s : values()) {
			result.add(new EnumObjects(s.code, s.desc));
		}
		return result;
	}

	public static String getDescByCode(Integer code) {

		for (ShiftStatus s : values()) {
			if (s.code.equals(code))
				return s.desc;
		}

		return null;
	}
}
