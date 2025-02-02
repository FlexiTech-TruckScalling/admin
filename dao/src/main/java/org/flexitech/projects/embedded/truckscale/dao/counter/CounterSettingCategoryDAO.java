package org.flexitech.projects.embedded.truckscale.dao.counter;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.setting.CounterSettingCategory;

public interface CounterSettingCategoryDAO extends CommonDAO<CounterSettingCategory, Long>{
	List<CounterSettingCategory> getAllByStatus(Integer statue);
}
