package org.flexitech.projects.embedded.truckscale.dao.counter;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.counters.CounterSetting;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class CounterSettingDAOImpl extends CommonDAOImpl<CounterSetting, Long> implements CounterSettingDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<CounterSetting> getCounterSettingByCounterId(Long counterId) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.createAlias("counter", "c");
		c.add(Restrictions.eq("c.id", counterId));
		return c.list();
	}

	@Override
	public CounterSetting getCounterSettingByCounterAndMasterSettingId(Long counterId, Long masterCounterId) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.createAlias("counter", "c");
		c.createAlias("masterCounterSetting", "m");
		c.add(Restrictions.eq("c.id", counterId));
		c.add(Restrictions.eq("m.id", masterCounterId));
		c.setMaxResults(1);
		return (CounterSetting) c.uniqueResult();
	}
}
