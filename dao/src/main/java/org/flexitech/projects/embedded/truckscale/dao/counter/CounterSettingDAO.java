package org.flexitech.projects.embedded.truckscale.dao.counter;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.counters.CounterSetting;

public interface CounterSettingDAO extends CommonDAO<CounterSetting, Long>{
	List<CounterSetting> getCounterSettingByCounterId(Long counterId);
	CounterSetting getCounterSettingByCounterAndMasterSettingId(Long counterId, Long masterCounterId);
}
