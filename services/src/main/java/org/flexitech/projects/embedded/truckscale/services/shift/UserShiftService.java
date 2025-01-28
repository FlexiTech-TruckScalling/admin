package org.flexitech.projects.embedded.truckscale.services.shift;

import org.flexitech.projects.embedded.truckscale.dto.shift.CurrentShiftSummaryDTO;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;

public interface UserShiftService {
	UserShiftDTO getCurrentActiveShift(Long userId);
	UserShiftDTO getShiftById(Long id);
	UserShiftDTO manageShift(UserShiftDTO dto) throws Exception;
	boolean isUserCanStartShift(Long userId) throws Exception;
	UserShiftDTO startShift(Long userId) throws Exception;
	UserShiftDTO endShift(Long userId, UserDTO loggedUser) throws Exception;
	
	CurrentShiftSummaryDTO getCurrentShiftSummary(Long userId, String sessionCode);
}
