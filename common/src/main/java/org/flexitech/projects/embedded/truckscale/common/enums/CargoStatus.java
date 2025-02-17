package org.flexitech.projects.embedded.truckscale.common.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum CargoStatus {
	WITH_CARGO(1, "With Cargo"), WITHOUT_CARGO(2, "Without Cargo");
	private final Integer code;
	private final String desc;

	CargoStatus(int i, String string) {
		this.code = i;
		this.desc = string;
	}

	public static List<EnumObjects> getAll() {
		List<EnumObjects> result = new ArrayList<EnumObjects>();
		for (CargoStatus s : values()) {
			result.add(new EnumObjects(s.code, s.desc));
		}
		return result;
	}

	public static String getDescByCode(Integer code) {

		for (CargoStatus s : values()) {
			if (s.code.equals(code))
				return s.desc;
		}

		return null;
	}
}
