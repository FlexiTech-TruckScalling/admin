package org.flexitech.projects.embedded.truckscale.dao.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerVehicles;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerVehicleDAOImpl extends CommonDAOImpl<CustomerVehicles, Long> implements CustomerVehicleDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerVehicles> getAllCustomerVehicles(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerVehicles> getAllCustomerVehicles(Long customerId, Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.createAlias("customer", "c");
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.add(Restrictions.eq("c.id", customerId));
		return c.list();
	}

	@Override
	public boolean isVehicleNumberAlreadyUserd(String number, Long ignoreId, String prefix) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("prefix", prefix));
		c.add(Restrictions.eq("number", number));
		if(CommonValidators.validLong(ignoreId)) {
			c.add(Restrictions.ne("id", ignoreId));
		}
		c.setMaxResults(1);
		return c.uniqueResult() != null;
	}

}
