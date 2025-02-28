package org.flexitech.projects.embedded.truckscale.dao.shift;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShiftSummary;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UserShiftSummaryDAOImpl extends CommonDAOImpl<UserShiftSummary, Long> implements UserShiftSummaryDAO{

	@Override
	public UserShiftSummary getUserShiftDetailByShiftId(Long shiftId) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.createAlias("userShift", "s");
		c.add(Restrictions.eq("s.id", shiftId));
		c.addOrder(Order.desc("createdTime"));
		c.setMaxResults(1);
		
		return (UserShiftSummary) c.uniqueResult();
	}

}
