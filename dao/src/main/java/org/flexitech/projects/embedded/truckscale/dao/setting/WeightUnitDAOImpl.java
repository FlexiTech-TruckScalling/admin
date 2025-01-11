package org.flexitech.projects.embedded.truckscale.dao.setting;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.setting.WeightUnit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class WeightUnitDAOImpl extends CommonDAOImpl<WeightUnit, Long> implements WeightUnitDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<WeightUnit> getAllWeightUnit(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.addOrder(Order.asc("sequence"));
		return c.list();
	}

	@Override
	public boolean isSequenceAlreadyUsed(Integer sequence, Long ignoreId) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("sequence", sequence));
		if(CommonValidators.validLong(ignoreId)) {
			c.add(Restrictions.ne("id", ignoreId));
		}
		c.setMaxResults(1);
		List<?> list = c.list();
		return CommonValidators.validList(list);
	}

}
