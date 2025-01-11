package org.flexitech.projects.embedded.truckscale.dao.counter;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.setting.MasterCounterSetting;

public interface MasterCounterSettingDAO extends CommonDAO<MasterCounterSetting, Long>{
	List<MasterCounterSetting> getAllByStatus(Integer status);
}
