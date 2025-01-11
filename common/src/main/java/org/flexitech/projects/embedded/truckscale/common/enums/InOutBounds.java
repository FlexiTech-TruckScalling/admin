package org.flexitech.projects.embedded.truckscale.common.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum InOutBounds {

	IN(1, "In Bound"),
	OUT(2, "Out Bound"),
	BOTH(3, "Both");

	private final Integer code;
	private final String desc;
	
	InOutBounds(int i, String string) {
		code = i;
		desc = string;
	}
	
	public static List<EnumObjects> getAll() {
		List<EnumObjects> result = new ArrayList<EnumObjects>();
		for (InOutBounds s : values()) {
			result.add(new EnumObjects(s.code, s.desc));
		}
		return result;
	}

	public static String getDescByCode(Integer code) {

		for (InOutBounds s : values()) {
			if (s.code.equals(code))
				return s.desc;
		}

		return null;
	}
	
}
