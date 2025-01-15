package org.flexitech.projects.embedded.truckscale.services.shift;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.ShiftStatus;
import org.flexitech.projects.embedded.truckscale.dao.shift.UserShiftDAO;
import org.flexitech.projects.embedded.truckscale.dao.user.UserDAO;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShift;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;

@Service
@Transactional
public class UserShiftServiceImpl implements UserShiftService {

	@Autowired
	UserShiftDAO userShiftDAO;

	@Autowired
	UserDAO userDAO;

	@Override
	public UserShiftDTO getCurrentActiveShift(Long userId) {
		UserShift s = this.userShiftDAO.getCurrentActiveShift(userId);
		if (CommonValidators.isValidObject(s)) {
			return new UserShiftDTO(s);
		}
		return null;
	}

	@Override
	public UserShiftDTO getShiftById(Long id) {
		UserShift s = this.userShiftDAO.get(id);
		if (CommonValidators.isValidObject(s)) {
			return new UserShiftDTO(s);
		}
		return null;
	}

	@Override
	public UserShiftDTO manageShift(UserShiftDTO dto) throws Exception {
		if (!CommonValidators.isValidObject(dto)) {
			throw new IllegalArgumentException("Reference object cannot be empty!");
		}
		UserShift s = null;
		if (CommonValidators.validLong(dto.getId())) {
			s = this.userShiftDAO.get(dto.getId());
			if (!CommonValidators.isValidObject(s)) {
				throw new ResourceNotFoundException("Resource not found!");
			}
			s.setUpdatedTime(new Date());
		} else {
			s = new UserShift();
			s.setCreatedTime(new Date());
		}

		s.setStartTime(dto.getStartTime());
		s.setEndTime(dto.getEndTime());
		s.setShiftStatus(dto.getShiftStatus());

		if (CommonValidators.isValidObject(dto.getUser()) && CommonValidators.validLong(dto.getUser().getId())) {
			Users u = this.userDAO.get(dto.getUser().getId());
			s.setUser(u);
		}

		this.userShiftDAO.saveOrUpdate(s);
		return new UserShiftDTO(s);
	}

	@Override
	public boolean isUserCanStartShift(Long userId) throws Exception {
		Users user = this.userDAO.get(userId);
		if (user == null) {
			throw new IllegalArgumentException("User with ID " + userId + " not found!");
		}

		boolean isRoleActive = user.getUserRole() != null
				&& Objects.equal(user.getUserRole().getUseApp(), ActiveStatus.ACTIVE.getCode());

		boolean hasNoActiveShift = this.getCurrentActiveShift(userId) == null;

		return isRoleActive && hasNoActiveShift;
	}

	@Override
	public UserShiftDTO startShift(Long userId) throws Exception {
		if (!this.isUserCanStartShift(userId)) {
			throw new IllegalArgumentException("User is not eligible to start a shift.");
		}

		Users user = this.userDAO.get(userId);
		Date current = new Date();

		UserShift userShift = new UserShift();
		userShift.setStartTime(current);
		userShift.setShiftStatus(ActiveStatus.ACTIVE.getCode());
		userShift.setUser(user);
		userShift.setCreatedTime(current);

		this.userShiftDAO.save(userShift);

		return new UserShiftDTO(userShift);
	}

	@Override
	public UserShiftDTO endShift(Long userId, UserDTO loggedUser) throws Exception {
		if (loggedUser == null || loggedUser.getId() == null) {
			throw new IllegalArgumentException("Logged user information is invalid.");
		}

		UserShift activeShift = this.userShiftDAO.getCurrentActiveShift(userId);
		if (activeShift == null) {
			throw new Exception("No active shift found for user ID " + userId);
		}

		activeShift.setEndTime(new Date());
		activeShift.setShiftStatus(ShiftStatus.CLOSE.getCode());
		activeShift.setEndByUser(this.userDAO.get(loggedUser.getId()));

		this.userShiftDAO.update(activeShift);

		return new UserShiftDTO(activeShift);
	}

}
