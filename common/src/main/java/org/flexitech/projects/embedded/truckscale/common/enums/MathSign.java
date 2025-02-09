package org.flexitech.projects.embedded.truckscale.common.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum MathSign {
	EQUAL(1, "="), GREATER_THAN(2, ">"), GREATER_THAN_EQUAL(3, ">="),
	LESS_THAN(4, "<"), LESS_THAN_EQUAL(5, "<=");
	
	private final Integer code;
	private final String desc;

	MathSign(int i, String string) {
		this.code = i;
		this.desc = string;
	}

	public static List<EnumObjects> getAll() {
		List<EnumObjects> result = new ArrayList<EnumObjects>();
		for (MathSign s : values()) {
			result.add(new EnumObjects(s.code, s.desc));
		}
		return result;
	}

	public static String getDescByCode(Integer code) {

		for (MathSign s : values()) {
			if (s.code.equals(code))
				return s.desc;
		}

		return null;
	}
}
