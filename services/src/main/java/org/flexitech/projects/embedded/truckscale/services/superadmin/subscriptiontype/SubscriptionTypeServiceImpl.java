package org.flexitech.projects.embedded.truckscale.services.superadmin.subscriptiontype;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.superadmin.subscriptiontype.SubscriptionTypeDAO;
import org.flexitech.projects.embedded.truckscale.dto.superadminuser.SubscriptionTypeDTO;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SubscriptionType;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionTypeServiceImpl implements SubscriptionTypeService {

	@Autowired
	SubscriptionTypeDAO subTypeDAO;
	
	private final Logger logger = LogManager.getLogger(getClass());
	
	@Override
	public SubscriptionTypeDTO getSubscriptionTypeById(Long id) {
		SubscriptionType s = this.subTypeDAO.get(id);
		if (CommonValidators.isValidObject(s)) {
			return new SubscriptionTypeDTO(s);
		}
		return null;
	}

	@Override
	public SubscriptionTypeDTO manageSubscriptionType(SubscriptionTypeDTO dto) {
		if (!CommonValidators.isValidObject(dto))
			return null;
		SubscriptionType s = null;
		if (CommonValidators.validLong(dto.getId())) {
			s = this.subTypeDAO.get(dto.getId());
			s.setUpdatedTime(new Date());
		} else {
			s = new SubscriptionType();
			s.setCreatedTime(new Date());
		}
		s.setSubscriptionName(dto.getSubscriptionName());
		s.setPeriod(dto.getPeriod());
		this.subTypeDAO.saveOrUpdate(s);

		return new SubscriptionTypeDTO(s);
	}

	@Override
	public List<SubscriptionTypeDTO> getAllSubscriptionType(Integer status) {
		List<SubscriptionType> subType = this.subTypeDAO.getAllSubscriptionType(status);
		if (CommonValidators.validList(subType)) {
			return subType.stream().map(SubscriptionTypeDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<SubscriptionTypeDTO>();
	}

	@Override
	public boolean deleteSubscriptionType(Long id) {
		try {
			SubscriptionType s = this.subTypeDAO.get(id);
			if (CommonValidators.isValidObject(s)) {
				this.subTypeDAO.delete(s);
				return true;
			}
		} catch (Exception e) {
			logger.error("Error on deleting product: {}", ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
	
}
