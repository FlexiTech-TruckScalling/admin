package org.flexitech.projects.embedded.truckscale.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatDTO {
	private String customerName;
    private long transactionCount;
    private double totalWeight;
}
