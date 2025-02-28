package org.flexitech.projects.embedded.truckscale.dao.shift;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.dto.user_shift.UserShiftSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShift;

public interface UserShiftDAO extends CommonDAO<UserShift, Long> {
	UserShift getCurrentActiveShift(Long userId);
	UserShift getUserShitByCode(String code);
	List<UserShift> searchUserShift(UserShiftSearchDTO searchDTO, boolean export);
	Integer countUserShift(UserShiftSearchDTO searchDTO);
}
