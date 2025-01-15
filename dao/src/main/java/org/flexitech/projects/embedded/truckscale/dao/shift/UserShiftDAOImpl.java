package org.flexitech.projects.embedded.truckscale.dao.shift;

import org.flexitech.projects.embedded.truckscale.common.enums.ShiftStatus;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShift;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UserShiftDAOImpl extends CommonDAOImpl<UserShift, Long> implements UserShiftDAO {

	@Override
	public UserShift getCurrentActiveShift(Long userId) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.createAlias("user", "u");
		c.add(Restrictions.eq("u.id", userId));
		c.add(Restrictions.eq("shiftStatus", ShiftStatus.OPEN.getCode()));
		c.add(Restrictions.isNull("endTime"));
		c.addOrder(Order.desc("startTime"));
		c.setMaxResults(1);
		return (UserShift) c.uniqueResult();
	}

}
