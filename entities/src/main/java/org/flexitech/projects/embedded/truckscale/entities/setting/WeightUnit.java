package org.flexitech.projects.embedded.truckscale.entities.setting;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.WEIGHT_UNIT_TABLE)
public class WeightUnit extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1702848387959471160L;
	
	private String name;
	
	private Integer code;
	
	private Integer sequence;
	
	private BigDecimal perKgValue;

}
