package org.flexitech.projects.embedded.truckscale.services.shift;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.user_shift.UserShiftSearchDTO;
import org.flexitech.projects.embedded.truckscale.dto.user_shift.UserShiftSummaryDTO;

public interface UserShiftReportService {
	
	List<UserShiftDTO> searchUserShift(UserShiftSearchDTO searchDTO, boolean export);
	Integer countUserShift(UserShiftSearchDTO searchDTO);
	UserShiftSummaryDTO getShiftDetailByShiftId(Long shiftId);
}
