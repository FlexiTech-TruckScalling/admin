package org.flexitech.projects.embedded.truckscale.entities.unit;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = TableNames.QUANTITY_UNIT)
@Data
@EqualsAndHashCode(callSuper = true)
public class QuantityUnit extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6934061683355834905L;

	private String name;
	
	private String code;
	
	private Integer sequence;
	
	@Column(name = "price_per_unit")
	private BigDecimal pricePerUnit;
	
}
