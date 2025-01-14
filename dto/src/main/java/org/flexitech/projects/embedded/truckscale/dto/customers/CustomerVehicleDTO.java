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
	
	private Long customerId;
	
	private DriverDTO driver;
	
	private Long driverId;
	
	public CustomerVehicleDTO(CustomerVehicles v) {
		if(CommonValidators.isValidObject(v)) {
			this.number = v.getNumber();
			this.weight = v.getWeight();
			if(CommonValidators.isValidObject(v.getDriver())) {
				this.driver = new DriverDTO(v.getDriver());
				driverId = v.getDriver().getId();
			}
			setField(v);
		}
	}
	
}
