package org.flexitech.projects.embedded.truckscale.dao.payment_type;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.payment_type.PaymentType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentTypeDAOImpl extends CommonDAOImpl<PaymentType, Long> implements PaymentTypeDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentType> getAll(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.addOrder(Order.asc("sequence"));
		return c.list();
	}

}
