package org.flexitech.projects.embedded.truckscale.entities.menu;

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
@Table(name = TableNames.MENU_TABLE)
public class Menu extends BaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = 2736156828861925779L;
	
	@Column(name ="menu_code")
	private String code;
	private String name;
	private String url;
	@Column(name = "parent_status")
	private Integer parentStatus;
	@Column(name = "parent_menu_code")
	private String parentMenuCode;
	private String description;
	private Integer order;
	private String icon;
}
