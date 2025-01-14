package org.flexitech.projects.embedded.truckscale.entities.user;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShift;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.USER_TABLE)
public class Users extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = -2548498820523954216L;

	@Column(name = "login_name")
	private String loginName;

	private String name;

	@Column(name = "phone_no")
	private String phoneNo;

	private String password;

	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private UserRoles userRole;

	@ManyToOne
	@JoinColumn(name = "counter_id", nullable = true)
	private Counters counter;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserShift> shifts;

	@Column(name = "last_login_time")
	private Date lastLoginTime;
	
	@Column(name = "session_token")
	private String sessionToken;
}
