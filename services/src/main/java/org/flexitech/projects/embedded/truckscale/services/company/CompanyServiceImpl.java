package org.flexitech.projects.embedded.truckscale.services.company;

import java.util.Date;

import javax.transaction.Transactional;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.company.CompanyDAO;
import org.flexitech.projects.embedded.truckscale.dto.company.CompanyDTO;
import org.flexitech.projects.embedded.truckscale.entities.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyDAO companyDAO;
	
	@Override
	public CompanyDTO getById(Long id) {
		Company c = this.companyDAO.get(id);
		if(CommonValidators.isValidObject(c)) {
			return new CompanyDTO(c);
		}
		return null;
	}

	@Override
	public CompanyDTO manageCompanyDTO(CompanyDTO dto) {
		if(!CommonValidators.isValidObject(dto)) {
			return null;
		}
		
		Company c = null;
		
		if(CommonValidators.validLong(dto.getId())) {
			c = this.companyDAO.get(dto.getId());
			c.setUpdatedTime(new Date());
		}else {
			c = new Company();
			c.setCreatedTime(new Date());
		}
		
		c.setName(dto.getName());
		c.setAddress(dto.getAddress());
		c.setContactPerson(dto.getContactPerson());
		c.setContactPhone(dto.getContactPhone());
		c.setCompanyLogo(dto.getCompanyLogo());
		c.setStatus(ActiveStatus.ACTIVE.getCode());
		
		this.companyDAO.saveOrUpdate(c);

		return new CompanyDTO(c);
	}

}
