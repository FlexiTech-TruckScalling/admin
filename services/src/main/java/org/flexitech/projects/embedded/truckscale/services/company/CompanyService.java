package org.flexitech.projects.embedded.truckscale.services.company;

import org.flexitech.projects.embedded.truckscale.dto.company.CompanyDTO;

public interface CompanyService {
	CompanyDTO getById(Long id);
	CompanyDTO manageCompanyDTO(CompanyDTO dto);
}
