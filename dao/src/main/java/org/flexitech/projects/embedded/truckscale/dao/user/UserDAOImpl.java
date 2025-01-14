package org.flexitech.projects.embedded.truckscale.dao.user;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl extends CommonDAOImpl<Users, Long> implements UserDAO {

	@Override
	public Users findUserByLoginName(String loginName) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("loginName", loginName));
		c.setMaxResults(1);
		return (Users) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Users> getAllUserByStatus(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.addOrder(Order.desc("createdTime"));
		return c.list();
	}

	@Override
	public Users findUserBySessionToken(String sessionToken) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("sessionToken", sessionToken));
		c.setMaxResults(1);
		return (Users) c.uniqueResult();
	}

	
	
}
