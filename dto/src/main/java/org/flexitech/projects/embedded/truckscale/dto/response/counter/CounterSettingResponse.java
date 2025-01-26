package org.flexitech.projects.embedded.truckscale.dto.response.counter;

import java.util.ArrayList;
import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.enums.EnumObjects;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerTypeDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.WeightUnitDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CounterSettingResponse{
	private CounterDTO counter;
	private List<WeightUnitDTO> units;
	private List<EnumObjects> bounds;
	private List<CustomerTypeDTO> customerTypes = new ArrayList<CustomerTypeDTO>();
}
