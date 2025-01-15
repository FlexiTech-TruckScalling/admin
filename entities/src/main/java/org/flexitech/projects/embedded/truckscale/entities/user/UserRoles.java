package org.flexitech.projects.embedded.truckscale.entities.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.flexitech.projects.embedded.truckscale.entities.menu.MenuRoleAccess;

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
	
	@Column(name = "use_app")
	private Integer useApp;
	
	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MenuRoleAccess> menuRoleAccesses;
}
