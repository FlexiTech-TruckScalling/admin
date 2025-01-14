package org.flexitech.projects.embedded.truckscale.dao.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.customers.Drivers;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class DriverDAOImpl extends CommonDAOImpl<Drivers, Long> implements DriverDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Drivers> getAllDrivers(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.addOrder(Order.desc("createdTime"));
		return c.list();
	}

}
