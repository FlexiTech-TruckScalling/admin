package org.flexitech.projects.embedded.truckscale.dto.user;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;

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
	private String password;
	private String phoneNo;
	
	private UserRoleDTO userRoleDTO;
	private CounterDTO counterDTO;
	
	private Long userRoleId;
	private Long counterId;
	
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
		// default fields
		setField(user);
	}
}
