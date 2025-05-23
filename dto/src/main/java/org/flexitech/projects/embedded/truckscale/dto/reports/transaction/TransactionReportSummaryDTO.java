package org.flexitech.projects.embedded.truckscale.dto.reports.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionReportSummaryDTO {
	private Double totalWeight;
	private Double totalCargoWeight;
	private Double totalNetWeight;
	
	private String totalAmount;
	private Integer totalIn;
	private Integer totalOut;
}
