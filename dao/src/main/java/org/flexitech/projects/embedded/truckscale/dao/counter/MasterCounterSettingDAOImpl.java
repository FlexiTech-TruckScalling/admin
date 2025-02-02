package org.flexitech.projects.embedded.truckscale.dao.counter;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.setting.MasterCounterSetting;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class MasterCounterSettingDAOImpl extends CommonDAOImpl<MasterCounterSetting, Long> implements MasterCounterSettingDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterCounterSetting> getAllByStatus(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.createAlias("category", "c");
		c.addOrder(Order.asc("c.sequence"));
		c.addOrder(Order.asc("sequence"));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterCounterSetting> getAllByCategory(Long categoryId, Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.createAlias("category", "c");
		c.add(Restrictions.eq("c.id", categoryId));
		c.addOrder(Order.asc("sequence"));
		return c.list();
	}

}
