package org.flexitech.projects.embedded.truckscale.entities.customers;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.CUSTOMER_TYPE_TABLE)
public class CustomerTypes extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8175718507362618693L;

	private String name;
	
	private String code;
	
}
