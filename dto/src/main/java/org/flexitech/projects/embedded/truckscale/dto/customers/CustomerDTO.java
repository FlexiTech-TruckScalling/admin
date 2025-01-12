package org.flexitech.projects.embedded.truckscale.dto.customers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.Customers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDTO extends CommonDTO {
	private String name;
	private String code;
	private String phoneNumber;
	private String address;
	
	private List<CustomerTypeDTO> customerTypeDTOs = new ArrayList<CustomerTypeDTO>();
	private List<Long> customerTypeIds = new ArrayList<Long>();
	private String customerTypeNames;
	
	private List<CustomerVehicleDTO> customerVehicleDTOs = new ArrayList<CustomerVehicleDTO>();
	
	public CustomerDTO(Customers c) {
		if(CommonValidators.isValidObject(c)) {
			this.name = c.getName();
			this.code = c.getCode();
			this.phoneNumber = c.getPhoneNumber();
			this.address = c.getAddress();
			
			if(CommonValidators.validList(c.getCustomerTypes())) {
				this.customerTypeDTOs = c.getCustomerTypes().stream().map(CustomerTypeDTO::new).collect(Collectors.toList());
				this.customerTypeIds = c.getCustomerTypes().stream().map(t->t.getId()).collect(Collectors.toList());
				this.customerTypeNames = c.getCustomerTypes().stream().map(t->t.getName()).collect(Collectors.joining(", "));
			}
			
			if(CommonValidators.validList(c.getVehicles())) {
				this.customerVehicleDTOs = c.getVehicles().stream().map(CustomerVehicleDTO::new).collect(Collectors.toList());
			}
			
			setField(c);
		}
	}
	
}
