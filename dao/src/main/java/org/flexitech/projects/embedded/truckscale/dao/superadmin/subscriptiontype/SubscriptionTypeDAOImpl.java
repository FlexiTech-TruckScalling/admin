package org.flexitech.projects.embedded.truckscale.dao.superadmin.subscriptiontype;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SubscriptionType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionTypeDAOImpl extends CommonDAOImpl<SubscriptionType, Long> implements SubscriptionTypeDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriptionType> getAllSubscriptionType(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		return c.list();
	}

}
