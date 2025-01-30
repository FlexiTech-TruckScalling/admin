package org.flexitech.projects.embedded.truckscale.dto.request.user_shift;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserShiftSummaryRequestDTO {
	@JsonProperty("user_id")
	private Long userId;
	@JsonProperty("counter_id")
	private Long counterId;
	@JsonProperty("shift_code")
	private String shiftCode;
	@JsonProperty("total_transaction")
	private Integer totalTransaction;
	@JsonProperty("total_in_transaction")
	private Integer totalInTransaction;
	@JsonProperty("total_out_transaction")
	private Integer totalOutTranaction;
	@JsonProperty("total_amount")
	private BigDecimal totalAmount;
}
