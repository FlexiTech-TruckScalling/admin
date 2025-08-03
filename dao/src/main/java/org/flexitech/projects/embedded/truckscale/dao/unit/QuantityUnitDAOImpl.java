package org.flexitech.projects.embedded.truckscale.dao.unit;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.unit.QuantityUnit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class QuantityUnitDAOImpl extends CommonDAOImpl<QuantityUnit, Long> implements QuantityUnitDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<QuantityUnit> findByStatus(ActiveStatus status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.isValidObject(status)) {
			c.add(Restrictions.eq("status", status.getCode()));
		}
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
