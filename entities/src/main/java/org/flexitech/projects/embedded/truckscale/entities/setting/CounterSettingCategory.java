package org.flexitech.projects.embedded.truckscale.entities.setting;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.COUNTER_SETTING_CATEGORY)
public class CounterSettingCategory extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2090557960906834344L;
	
	private Integer code;
	
	private String description;

	private Integer sequence;
}
