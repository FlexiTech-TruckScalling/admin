package org.flexitech.projects.embedded.truckscale.dto.shift;

import java.util.Date;

import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShift;
import org.flexitech.projects.embedded.truckscale.util.DateUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserShiftDTO extends CommonDTO{
	private Date startTime;
	private String startTimeDesc;
	private Date endTime;
	private String endTimeDesc;
	private Integer shiftStatus;
	private UserDTO user;
	
	public UserShiftDTO(UserShift u) {
		if(CommonValidators.isValidObject(u)) {
			if(CommonValidators.isValidObject(u.getStartTime())) {
				this.startTime = u.getStartTime();
				this.startTimeDesc = DateUtils.dateToString(u.getStartTime(), CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT);
			}
			if(CommonValidators.isValidObject(u.getEndTime())) {
				this.endTime = u.getEndTime();
				this.endTimeDesc = DateUtils.dateToString(u.getEndTime(), CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT);
			}
			this.shiftStatus = u.getShiftStatus();
			if(CommonValidators.isValidObject(u.getUser())) {
				this.user = new UserDTO(u.getUser());
			}
			
			setField(u);
		}
	}
	
}
