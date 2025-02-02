package org.flexitech.projects.embedded.truckscale.dto.counter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.CounterSettingCategoryDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.MasterCounterSettingDTO;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounterDTO extends CommonDTO {

	private String name;
	private String counterIp;
	@JsonInclude(Include.NON_EMPTY)
	private List<CounterSettingCategoryDTO> settingCategoryDTOs = new ArrayList<CounterSettingCategoryDTO>();
	
	private List<CounterSettingDTO> counterSettingDTOs = new ArrayList<CounterSettingDTO>();
	public CounterDTO(Counters counter) {
		this.name = counter.getName();
		this.counterIp = counter.getCounterIp();
		setField(counter);
	}

}
