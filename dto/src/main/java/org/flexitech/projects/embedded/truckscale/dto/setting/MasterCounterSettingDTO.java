package org.flexitech.projects.embedded.truckscale.dto.setting;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.setting.MasterCounterSetting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterCounterSettingDTO extends CommonDTO {
	private String code;
	private String description;
	private Integer sequence;
	private Long categoryId;
	public MasterCounterSettingDTO(MasterCounterSetting setting) {
		this.code = setting.getCode();
		this.description = setting.getDescription();
		this.sequence = setting.getSequence();
		if(CommonValidators.isValidObject(setting.getCategory())) {
			this.categoryId = setting.getCategory().getId();
		}
		setField(setting);
	}
}
