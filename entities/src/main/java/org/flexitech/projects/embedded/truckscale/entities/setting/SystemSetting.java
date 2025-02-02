package org.flexitech.projects.embedded.truckscale.entities.setting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper =  true)
@Entity
@Table(name = TableNames.SYSTEM_SETTING_TABLE)
public class SystemSetting extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1818668841691061092L;
	
	private String code;
	
	private String description;
	
	private String value;
	
	private Integer sequence;
	
	@Column(name = "input_type")
	private Integer inputType;

}
