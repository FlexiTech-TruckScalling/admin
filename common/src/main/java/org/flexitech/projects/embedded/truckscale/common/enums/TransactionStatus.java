package org.flexitech.projects.embedded.truckscale.common.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum TransactionStatus {
	COMPLETED(1, "Completed"), INCOMPLETED(2, "Incompleted"), CANCEL(3, "Cancelled");
	private final Integer code;
	private final String desc;

	TransactionStatus(int i, String string) {
		this.code = i;
		this.desc = string;
	}

	public static List<EnumObjects> getAll() {
		List<EnumObjects> result = new ArrayList<EnumObjects>();
		for (TransactionStatus s : values()) {
			result.add(new EnumObjects(s.code, s.desc));
		}
		return result;
	}

	public static String getDescByCode(Integer code) {

		for (TransactionStatus s : values()) {
			if (s.code.equals(code))
				return s.desc;
		}

		return null;
	}
}
