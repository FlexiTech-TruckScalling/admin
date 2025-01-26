package org.flexitech.projects.embedded.truckscale.dto.user;

import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.flexitech.projects.embedded.truckscale.util.DateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends CommonDTO{
	private String loginName;
	private String name;
	@JsonIgnore
	private String password;
	private String phoneNo;
	
	private UserRoleDTO userRoleDTO;
	private CounterDTO counterDTO;
	
	private Long userRoleId;
	private Long counterId;
	
	@JsonIgnore
	private String sessionToken;
	private String lastLoginTime;
	
	public UserDTO(Users user) {
		this.loginName = user.getLoginName();
		this.name = user.getName();
		this.password = user.getPassword();
		this.phoneNo = user.getPhoneNo();
		if(CommonValidators.isValidObject(user.getUserRole())) {
			userRoleDTO = new UserRoleDTO(user.getUserRole());
			this.userRoleId = user.getUserRole().getId();
		}
		if(CommonValidators.isValidObject(user.getCounter())) {
			counterDTO = new CounterDTO(user.getCounter());
			this.counterId = user.getCounter().getId();
		}
		this.sessionToken = user.getSessionToken();
		if(CommonValidators.isValidObject(user.getLastLoginTime())) {
			this.lastLoginTime = DateUtils.dateToString(user.getLastLoginTime(), CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT);
		}
		// default fields
		setField(user);
	}
}
