package org.flexitech.projects.embedded.truckscale.entities.customers;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.DRIVER_TABLE)
public class Drivers extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4025084348342688253L;
	
	private String name;
	
	private String phoneNumber;
	
	@OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CustomerVehicles> vehicles;

}
