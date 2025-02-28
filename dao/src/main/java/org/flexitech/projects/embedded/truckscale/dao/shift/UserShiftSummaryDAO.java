package org.flexitech.projects.embedded.truckscale.dao.shift;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShiftSummary;

public interface UserShiftSummaryDAO extends CommonDAO<UserShiftSummary, Long>{

	UserShiftSummary getUserShiftDetailByShiftId(Long shiftId);
	
}
