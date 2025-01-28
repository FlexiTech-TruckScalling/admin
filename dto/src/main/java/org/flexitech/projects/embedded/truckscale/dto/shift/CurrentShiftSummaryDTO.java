package org.flexitech.projects.embedded.truckscale.dto.shift;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrentShiftSummaryDTO{
	private String date;
	private String sessionCode;
	private Integer totalTrn;
	private BigDecimal totalAmount;
}
