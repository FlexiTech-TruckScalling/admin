package org.flexitech.projects.embedded.truckscale.dao.user;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.user.UserRoles;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleDAOImpl extends CommonDAOImpl<UserRoles, Long> implements UserRoleDAO{

	@Override
	public boolean isRoleCodeUsed(Integer code) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("code", code));
		List<?> resultList = c.list();
		return CommonValidators.validList(resultList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRoles> getAllRoleByStatus(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.addOrder(Order.desc("createdTime"));
		return c.list();
	}

}
