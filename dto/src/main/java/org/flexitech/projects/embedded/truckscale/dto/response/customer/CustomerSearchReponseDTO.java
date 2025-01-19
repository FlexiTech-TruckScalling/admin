package org.flexitech.projects.embedded.truckscale.dto.response.customer;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerSearchReponseDTO {
	private List<CustomerDTO> dataList;
}
