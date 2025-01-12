package org.flexitech.projects.embedded.truckscale.dao.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerTypes;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerTypeDAOImpl extends CommonDAOImpl<CustomerTypes, Long> implements CustomerTypeDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerTypes> getAllCustomerTypes(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		return c.list();
	}

}
