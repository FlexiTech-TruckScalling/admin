package org.flexitech.projects.embedded.truckscale.services.superadmin.subscriptiontype;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.superadminuser.SubscriptionTypeDTO;

public interface SubscriptionTypeService {
	SubscriptionTypeDTO getSubscriptionTypeById(Long id);
	SubscriptionTypeDTO manageSubscriptionType(SubscriptionTypeDTO dto);
	List<SubscriptionTypeDTO> getAllSubscriptionType(Integer status);
	
	boolean deleteSubscriptionType(Long id);
}
