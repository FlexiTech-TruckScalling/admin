package org.flexitech.projects.embedded.truckscale.entities.company;

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
@Table(name = TableNames.COMPANY_TABLE)
public class Company extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2127124348396611977L;

	private String name;
	private String address;
	@Column(name = "contact_person")
	private String contactPerson;
	@Column(name = "contact_phone")
	private String contactPhone;
	@Column(name = "company_logo")
	private String companyLogo;
}
