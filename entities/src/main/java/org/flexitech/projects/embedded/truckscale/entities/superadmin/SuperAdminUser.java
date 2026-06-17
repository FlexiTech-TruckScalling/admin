package org.flexitech.projects.embedded.truckscale.entities.superadmin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.flexitech.projects.embedded.truckscale.entities.user.UserRoles;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.SUPERADMIN_USER)
public class SuperAdminUser extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = -2548498820523954216L;

	@Column(name = "login_name")
	private String loginName;
	
	private String name;

	@Column(name = "phone_no")
	private String phoneNo;

	private String email;

	private String password;
	
	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private UserRoles userRole;


}
