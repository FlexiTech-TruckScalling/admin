package org.flexitech.projects.embedded.truckscale.entities.customers;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.CUSTOMER_VEHICLE_TABLE)
public class CustomerVehicles extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7795538445181289247L;
	
	private String number;
	
	private BigDecimal weight;
	
	@ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customers customer;

}
