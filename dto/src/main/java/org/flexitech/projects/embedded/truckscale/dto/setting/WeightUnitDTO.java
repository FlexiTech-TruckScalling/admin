package org.flexitech.projects.embedded.truckscale.dto.setting;

import java.math.BigDecimal;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.setting.WeightUnit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WeightUnitDTO extends CommonDTO {
	private String name;
	private Integer code;
	private Integer sequence;
	private BigDecimal perKgValue;
	
	public WeightUnitDTO(WeightUnit wu) {
		if(CommonValidators.isValidObject(wu)) {
			name = wu.getName();
			code = wu.getCode();
			perKgValue = wu.getPerKgValue();
			sequence = wu.getSequence();
			
			setField(wu);
		}
	}
}
