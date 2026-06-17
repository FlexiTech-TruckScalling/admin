package org.flexitech.projects.embedded.truckscale.dao.superadminuser;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SuperAdminUser;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class SuperAdminUserDAOImpl extends CommonDAOImpl<SuperAdminUser, Long> implements SuperAdminUserDAO {

	@Override
	public SuperAdminUser findAdminUserByLoginName(String loginName) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("loginName", loginName));
		c.setMaxResults(1);
		return (SuperAdminUser) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SuperAdminUser> getAllUserByStatus(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if (CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.addOrder(Order.desc("createdTime"));
		return c.list();
	}

	@Override
	public SuperAdminUser findSuperAdminUserBySessionToken(String sessionToken) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("sessionToken", sessionToken));
		c.setMaxResults(1);
		return (SuperAdminUser) c.uniqueResult();
	}

}
