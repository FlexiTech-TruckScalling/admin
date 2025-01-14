package org.flexitech.projects.embedded.truckscale.dto.customers;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.Drivers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DriverDTO extends CommonDTO {
	private String name;
	private String phoneNumber;
	
	public DriverDTO(Drivers d) {
		if(CommonValidators.isValidObject(d)) {
			this.name = d.getName();
			this.phoneNumber = d.getPhoneNumber();
			setField(d);
		}
	}
}
