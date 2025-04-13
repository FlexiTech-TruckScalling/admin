package org.flexitech.projects.embedded.truckscale.services.company;

import java.io.IOException;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.company.CompanyDAO;
import org.flexitech.projects.embedded.truckscale.dto.company.CompanyDTO;
import org.flexitech.projects.embedded.truckscale.entities.company.Company;
import org.flexitech.projects.embedded.truckscale.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private CompanyDAO companyDAO;
	
	@Autowired
    private ImageUtil imageUtil;
	
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
		
		String oldImage = null;
		
		if(CommonValidators.validLong(dto.getId())) {
			c = this.companyDAO.get(dto.getId());
			c.setUpdatedTime(new Date());
			oldImage = c.getCompanyLogo();
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

		if (dto.getLogoImage() != null && !dto.getLogoImage().isEmpty()) {
			if (oldImage != null) {
                imageUtil.deleteImage("/logo/"+oldImage);
            }
            String storedPath;
			try {
				storedPath = imageUtil.writeImage(
						dto.getLogoImage(), 
				    "logo"
				);
				c.setCompanyLogo(storedPath);
				this.companyDAO.update(c);
			} catch (IOException e) {
				logger.error("Error on upload image: {}", e);
			}
        }
		
		return new CompanyDTO(c);
	}

}
