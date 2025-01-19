package org.flexitech.projects.embedded.truckscale.dto.counter;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.MasterCounterSettingDTO;
import org.flexitech.projects.embedded.truckscale.entities.counters.CounterSetting;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounterSettingDTO extends CommonDTO{
	
	@JsonIgnore
	private CounterDTO counterDTO;
	private String value;
	private String remark;
	private MasterCounterSettingDTO masterCounterSettingDTO;
	
	public CounterSettingDTO(CounterSetting setting) {
		if(CommonValidators.isValidObject(setting.getCounter())) {
			this.counterDTO = new CounterDTO(setting.getCounter());
		}
		this.value = setting.getValue();
		this.remark = setting.getRemark();
		
		if(CommonValidators.isValidObject(setting.getMasterCounterSetting())) {
			this.masterCounterSettingDTO = new MasterCounterSettingDTO(setting.getMasterCounterSetting());
		}
		
		setField(setting);
	}

}
