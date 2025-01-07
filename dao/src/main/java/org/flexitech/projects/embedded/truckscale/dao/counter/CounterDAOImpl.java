package org.flexitech.projects.embedded.truckscale.dao.counter;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class CounterDAOImpl extends CommonDAOImpl<Counters, Long> implements CounterDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Counters> getAllCounterByStatus(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.addOrder(Order.desc("createdTime"));
		return c.list();
	}

}
