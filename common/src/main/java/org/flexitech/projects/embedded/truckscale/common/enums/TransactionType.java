package org.flexitech.projects.embedded.truckscale.common.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum TransactionType {
	INOUT(1, "In/Out"),
	COMPLETE(2, "Complete");
	private final Integer code;
	private final String desc;

	TransactionType(int i, String string) {
		this.code = i;
		this.desc = string;
	}

	public static List<EnumObjects> getAll() {
		List<EnumObjects> result = new ArrayList<EnumObjects>();
		for (TransactionType s : values()) {
			result.add(new EnumObjects(s.code, s.desc));
		}
		return result;
	}

	public static String getDescByCode(Integer code) {

		for (TransactionType s : values()) {
			if (s.code.equals(code))
				return s.desc;
		}

		return null;
	}
}
