package org.flexitech.projects.embedded.truckscale.dto.customers;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerTypes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerTypeDTO extends CommonDTO {
	private String name;
	private String code;
	public CustomerTypeDTO(CustomerTypes t) {
		if(CommonValidators.isValidObject(t)) {
			this.name = t.getName();
			this.code = t.getCode();
			
			setField(t);
		}
	}
}
