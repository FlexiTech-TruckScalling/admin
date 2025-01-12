package org.flexitech.projects.embedded.truckscale.dto.customers;

import org.flexitech.projects.embedded.truckscale.dto.CommonSearchDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchDTO extends CommonSearchDTO {
	private String name;
	private String code;
	private Integer status;
	private String phoneNumber;
	private Long customerType;
	private String vehicleNumber;
}
