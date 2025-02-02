package org.flexitech.projects.embedded.truckscale.dto.setting;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.setting.SystemSetting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SystemSettingDTO extends CommonDTO {
	private String code;
	private String description;
	private String value;
	private Integer sequence;
	private Integer inputType;

	public SystemSettingDTO(SystemSetting s) {
		if (!CommonValidators.isValidObject(s))
			return;
		this.code = s.getCode();
		this.description = s.getDescription();
		this.value = s.getValue();
		this.sequence = s.getSequence();
		this.inputType = s.getInputType();
		setField(s);
	}
}
