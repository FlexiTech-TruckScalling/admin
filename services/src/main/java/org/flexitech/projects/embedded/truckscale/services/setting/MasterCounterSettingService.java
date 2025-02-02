package org.flexitech.projects.embedded.truckscale.services.setting;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.setting.CounterSettingCategoryDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.MasterCounterSettingListDTO;

public interface MasterCounterSettingService {
	MasterCounterSettingListDTO getSettingList(Integer status);
	MasterCounterSettingListDTO updateSetting(MasterCounterSettingListDTO dto) throws Exception;
	List<CounterSettingCategoryDTO> getAllCategory(Integer status);
}
