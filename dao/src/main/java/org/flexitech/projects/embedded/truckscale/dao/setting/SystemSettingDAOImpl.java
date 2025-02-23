package org.flexitech.projects.embedded.truckscale.dao.setting;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.setting.SystemSetting;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class SystemSettingDAOImpl extends CommonDAOImpl<SystemSetting, Long> implements SystemSettingDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemSetting> getAllByStatus(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.addOrder(Order.asc("sequence"));
		return c.list();
	}

	@Override
	public SystemSetting getSettingByCode(String code) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("code", code));
		c.add(Restrictions.eq("status", ActiveStatus.ACTIVE.getCode()));
		c.setMaxResults(1);
		return (SystemSetting) c.uniqueResult();
	}

}
