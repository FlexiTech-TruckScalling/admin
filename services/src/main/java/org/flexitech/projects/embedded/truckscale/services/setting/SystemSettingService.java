package org.flexitech.projects.embedded.truckscale.services.setting;

import org.flexitech.projects.embedded.truckscale.dto.setting.SystemSettingDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.SystemSettingListDTO;

public interface SystemSettingService {
	SystemSettingListDTO getAllSystemSettings(Integer status);
	SystemSettingListDTO manageSystemSettings(SystemSettingListDTO settingListDTO);
	SystemSettingDTO getSettingByCode(String code);
}
