package org.flexitech.projects.embedded.truckscale.dto.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
	private Long transactionCount;
    private Double totalWeight;
    private BigDecimal totalRevenue;
}
