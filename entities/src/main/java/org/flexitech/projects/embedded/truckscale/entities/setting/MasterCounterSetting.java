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
@Table(name = TableNames.MASTER_COUNTER_SETTING_TABLE)
public class MasterCounterSetting extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6966639121556223342L;

	private String code;
	
	private String description;
	
	private Integer sequence;
}
