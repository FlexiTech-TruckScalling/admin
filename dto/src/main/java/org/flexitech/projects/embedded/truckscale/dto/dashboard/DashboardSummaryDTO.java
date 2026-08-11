package org.flexitech.projects.embedded.truckscale.dto.dashboard;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashboardSummaryDTO {

	private long todayTransactionCount;
    private String todayTotalWeight;
    private String todayTotalRevenue = "0";
    private long vehiclesInYard;
    private long activeShiftsCount;

    private Map<Integer, Long> hourlyTransactionCounts = new LinkedHashMap<>();

    private List<RecentTransactionDTO> recentTransactions;

    private List<CustomerStatDTO> topCustomers;
	
}
