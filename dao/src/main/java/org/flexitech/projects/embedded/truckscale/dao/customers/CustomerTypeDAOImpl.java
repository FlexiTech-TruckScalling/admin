package org.flexitech.projects.embedded.truckscale.dao.customers;

import java.util.Collections;
import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerTypes;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerTypes> getCustomerTypesByCustomerId(Long customerId) {
		if(!CommonValidators.validLong(customerId))return Collections.emptyList();
		
		StringBuilder b = new StringBuilder();
		
		b.append("SELECT ct.* FROM customer_types ct ")
		.append("LEFT JOIN customers_customer_types cct ON cct.customer_type_id = ct.id ")
		.append("WHERE cct.customer_id = :customerId");
		
		SQLQuery query = getCurrentSession().createSQLQuery(b.toString());
		query.addEntity(daoType);
		query.setParameter("customerId", customerId);
		return query.list();
	}

}
