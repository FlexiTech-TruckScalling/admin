package org.flexitech.projects.embedded.truckscale.dto.user_shift;

import org.flexitech.projects.embedded.truckscale.dto.CommonSearchDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserShiftSearchDTO  extends CommonSearchDTO {
	private String username;
	private String startTime;
	private String endTime;
}
