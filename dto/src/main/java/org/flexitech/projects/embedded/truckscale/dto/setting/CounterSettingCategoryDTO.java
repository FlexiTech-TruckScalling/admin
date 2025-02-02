package org.flexitech.projects.embedded.truckscale.dto.setting;

import java.util.ArrayList;
import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterSettingDTO;
import org.flexitech.projects.embedded.truckscale.entities.setting.CounterSettingCategory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CounterSettingCategoryDTO extends CommonDTO{
	private Integer code;
	private String description;
	private Integer sequence;
	private List<CounterSettingDTO> counterSettingDTOs = new ArrayList<CounterSettingDTO>();
	
	public CounterSettingCategoryDTO(CounterSettingCategory c) {
		if(!CommonValidators.isValidObject(c))return;
		this.code = c.getCode();
		this.description = c.getDescription();
		this.sequence = c.getSequence();
		setField(c);
	}
}
