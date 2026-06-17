package org.flexitech.projects.embedded.truckscale.dao.superadmin.subscriptiontype;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SubscriptionType;

public interface SubscriptionTypeDAO extends CommonDAO<SubscriptionType, Long> {
	List<SubscriptionType> getAllSubscriptionType(Integer status);
}
