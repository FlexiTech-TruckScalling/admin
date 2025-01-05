package org.flexitech.projects.embedded.truckscale.dto.setting;

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
	
	public MasterCounterSettingDTO(MasterCounterSetting setting) {
		this.code = setting.getCode();
		this.description = setting.getDescription();
		
		setField(setting);
	}
}
