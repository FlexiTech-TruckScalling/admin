package org.flexitech.projects.embedded.truckscale.dao.user;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.hibernate.Criteria;
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

	
	
}
