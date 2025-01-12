package org.flexitech.projects.embedded.truckscale.dto.customers;

import java.math.BigDecimal;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerVehicles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerVehicleDTO extends CommonDTO {

	private String number;
	
	private BigDecimal weight;
	
	public CustomerVehicleDTO(CustomerVehicles v) {
		if(CommonValidators.isValidObject(v)) {
			this.number = v.getNumber();
			this.weight = v.getWeight();
			
			setField(v);
		}
	}
	
}
