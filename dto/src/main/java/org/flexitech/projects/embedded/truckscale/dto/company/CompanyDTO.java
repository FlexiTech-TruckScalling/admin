package org.flexitech.projects.embedded.truckscale.dto.company;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.company.Company;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyDTO extends CommonDTO{
	private String name;
	private String address;
	private String contactPerson;
	private String contactPhone;
	private String companyLogo;
	
	private MultipartFile logoImage;
	
	public CompanyDTO(Company c) {
		if(!CommonValidators.isValidObject(c))return;
		this.name = c.getName();
		this.address = c.getAddress();
		this.contactPerson = c.getContactPerson();
		this.contactPhone = c.getContactPhone();
		this.companyLogo = c.getCompanyLogo();
		
		setField(c);
	}
}
