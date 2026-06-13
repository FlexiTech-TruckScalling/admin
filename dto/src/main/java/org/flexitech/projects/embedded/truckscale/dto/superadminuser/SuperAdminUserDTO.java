package org.flexitech.projects.embedded.truckscale.dto.superadminuser;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserRoleDTO;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SuperAdminUser;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminUserDTO extends CommonDTO {

	private String loginName;
	
	private String name;

	private String phoneNo;

	private String email;
	@JsonIgnore
	private String password;
	
	private Long userRoleId;
	
	private UserRoleDTO userRoleDTO;
	public SuperAdminUserDTO(SuperAdminUser superAdminUser) {
		super();
		this.loginName = superAdminUser.getLoginName();
		this.phoneNo = superAdminUser.getPhoneNo();
		this.email = superAdminUser.getEmail();
		this.password = superAdminUser.getPassword();
		
		if(CommonValidators.isValidObject(superAdminUser.getUserRole())) {
			userRoleDTO = new UserRoleDTO(superAdminUser.getUserRole());
			this.userRoleId = superAdminUser.getUserRole().getId();
		}
		
		setField(superAdminUser);
	}
	
	
}
