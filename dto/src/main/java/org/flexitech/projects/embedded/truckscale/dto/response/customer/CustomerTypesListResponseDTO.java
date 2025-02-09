package org.flexitech.projects.embedded.truckscale.dto.response.customer;

import java.util.Collections;
import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerTypeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTypesListResponseDTO {
	private List<CustomerTypeDTO> customerTypes = Collections.emptyList();
}
