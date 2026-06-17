package org.flexitech.projects.embedded.truckscale.services.superadmin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.superadmincustomer.SuperAdminCustDAO;
import org.flexitech.projects.embedded.truckscale.dto.superadminuser.SuperAdminCustomerDTO;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SuperAdminCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SupAdminCustServiceImpl implements SupAdminCustService {

	@Autowired
	SuperAdminCustDAO superAdminCustomerDAO;
	
	private final Logger logger = LogManager.getLogger(getClass());
	
	@Override
	public SuperAdminCustomerDTO getCustomerById(Long id) {
		SuperAdminCustomer c = this.superAdminCustomerDAO.get(id);
		if (CommonValidators.isValidObject(c)) {
			return new SuperAdminCustomerDTO(c);
		}
		return null;
	}

	@Override
	public SuperAdminCustomerDTO manageCustomer(SuperAdminCustomerDTO dto) {
		if (!CommonValidators.isValidObject(dto)) {
			return null;
		}

		SuperAdminCustomer c = null;

		if (CommonValidators.validLong(dto.getId())) {
			c = this.superAdminCustomerDAO.get(dto.getId());
			c.setUpdatedTime(new Date());
		} else {
			c = new SuperAdminCustomer();
			c.setCreatedTime(new Date());
		}
		
		c.setContactName(dto.getContactName());
		c.setContactPhone(dto.getContactPhone());
		c.setContactEmail(dto.getContactEmail());
		c.setDob(dto.getDob());
		c.setSuperAdminLicenseKey(dto.getSuperAdminLicenseKey());
		c.setKey(dto.getKey());
		c.setSubscriptionTypeId(dto.getSubscriptionTypeId());
		c.setExpiration(dto.getExpiration());

		this.superAdminCustomerDAO.saveOrUpdate(c);

		return new SuperAdminCustomerDTO(c);
	}

	@Override
	public List<SuperAdminCustomerDTO> getAllCustomers(Integer status) {
		List<SuperAdminCustomer> customers = this.superAdminCustomerDAO.getAllSuperAdimCustomer(status);
		if (CommonValidators.validList(customers)) {
			return customers.stream().map(SuperAdminCustomerDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<SuperAdminCustomerDTO>();
	}

	@Override
	public boolean deleteSupAdminCustomer(Long id) {
		try {

			SuperAdminCustomer c = this.superAdminCustomerDAO.get(id);
			if (CommonValidators.isValidObject(c)) {
				this.superAdminCustomerDAO.delete(c);
				return true;
			}

		} catch (Exception e) {
			logger.error("Error on deleting customer: {}", ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
	
}
