package org.flexitech.projects.embedded.truckscale.entities.payment_type;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = TableNames.PAYMENT_TYPE_TABLE)
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentType extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 103964730014388430L;

	private String code;
	
	private String name;
	
	private Integer sequence;
	
}
