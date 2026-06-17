package org.flexitech.projects.embedded.truckscale.entities.superadmin;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.SUPERADMIN_CUSTOMER)
public class SuperAdminCustomer extends BaseEntity{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 51373685253403035L;

	@Column(name = "contact_name")
	private String contactName;

	@Column(name = "contact_phone")
	private String contactPhone;

	@Column(name = "contact_email")
	private String contactEmail;

	private Date dob;

	@Column(name = "superadmin_license_key", nullable = false)
	private Integer superAdminLicenseKey= 1;

	@Column(name = "superadmin_customer_id")
	private Long superAdminCustomerId;

	private Long key;

	@Column(name = "subscription_type_id")
	private Long subscriptionTypeId;

	private Date expiration;
}
