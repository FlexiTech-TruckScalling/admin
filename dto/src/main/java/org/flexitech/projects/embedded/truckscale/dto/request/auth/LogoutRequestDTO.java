package org.flexitech.projects.embedded.truckscale.dto.request.auth;

import org.flexitech.projects.embedded.truckscale.dto.request.user_shift.UserShiftSummaryRequestDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LogoutRequestDTO {
	@JsonProperty("user_id")
	private Long userId;
	@JsonProperty("is_end_shift")
	private boolean isEndShift;
	
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("shift_summary")
	private UserShiftSummaryRequestDTO shiftSummary;
}
