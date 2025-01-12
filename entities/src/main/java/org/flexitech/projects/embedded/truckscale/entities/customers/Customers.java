package org.flexitech.projects.embedded.truckscale.entities.customers;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.CUSTOMER_TABLE)
public class Customers extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7697112460660042743L;

	private String name;
	
	private String code;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	private String address;
	
	@ManyToMany
    @JoinTable(
        name = "customers_customer_types",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "customer_type_id")
    )
    private Set<CustomerTypes> customerTypes;
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CustomerVehicles> vehicles;
	
}
