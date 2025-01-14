package org.flexitech.projects.embedded.truckscale.services.setting;

import org.flexitech.projects.embedded.truckscale.dto.setting.MasterCounterSettingListDTO;

public interface MasterCounterSettingService {
	MasterCounterSettingListDTO getSettingList(Integer status);
	MasterCounterSettingListDTO updateSetting(MasterCounterSettingListDTO dto) throws Exception;
}
