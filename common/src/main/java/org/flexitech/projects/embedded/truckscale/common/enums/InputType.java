package org.flexitech.projects.embedded.truckscale.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum InputType {
	TEXT(1, "Text"), PASSWORD(2, "Password"), NUMBER(2, "Number");
	private final Integer code;
	private final String desc;

	InputType(int i, String string) {
		this.code = i;
		this.desc = string;
	}

	public static List<EnumObjects> getAll() {
		List<EnumObjects> result = new ArrayList<EnumObjects>();
		for (InputType s : values()) {
			result.add(new EnumObjects(s.code, s.desc));
		}
		return result;
	}

	public static String getDescByCode(Integer code) {

		for (InputType s : values()) {
			if (s.code.equals(code))
				return s.desc;
		}

		return null;
	}
}
