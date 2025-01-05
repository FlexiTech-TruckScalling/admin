package org.flexitech.projects.embedded.truckscale.entities.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.USER_ROLE_TABLE)
public class UserRoles extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3967226071727632900L;

	@Column(name = "code")
	private Integer code;
	
	@Column(name = "name")
	private String name;
	
	private String description;
}
