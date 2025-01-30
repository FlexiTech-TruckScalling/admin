package org.flexitech.projects.embedded.truckscale.dao.shift;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShift;

public interface UserShiftDAO extends CommonDAO<UserShift, Long> {
	UserShift getCurrentActiveShift(Long userId);
	UserShift getUserShitByCode(String code);
}
