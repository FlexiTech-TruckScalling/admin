package org.flexitech.projects.embedded.truckscale.dto.superadminuser;

import java.util.Date;

import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SuperAdminCustomer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminCustomerDTO extends CommonDTO {

	private String contactName;

	private String contactPhone;

	private String contactEmail;

	private Date dob;

	private Long superAdminLicenseKey;

	private Long superAdminCustomerId;

	private Long key;

	private Long subscriptionTypeId;

	private Date expiration;

	public SuperAdminCustomerDTO(SuperAdminCustomer c) {
		super();
		this.contactName = c.getContactName();
		this.contactPhone = c.getContactPhone();
		this.contactEmail = c.getContactEmail();
		this.dob = c.getDob();
		this.superAdminLicenseKey = c.getSuperAdminLicenseKey();
		this.superAdminCustomerId = c.getSuperAdminCustomerId();
		this.key = c.getKey();
		this.subscriptionTypeId = c.getSubscriptionTypeId();
		this.expiration = c.getExpiration();
		
		setField(c);
	}
	
	
}
