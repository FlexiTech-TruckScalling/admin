package org.flexitech.projects.embedded.truckscale.dao.setting;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.setting.SystemSetting;

public interface SystemSettingDAO extends CommonDAO<SystemSetting, Long>{
	List<SystemSetting> getAllByStatus(Integer status);
}
