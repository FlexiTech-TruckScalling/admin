package org.flexitech.projects.embedded.truckscale.services.counter;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.MasterCounterSettingDTO;

public interface CounterSettingService {
	List<MasterCounterSettingDTO> getMasterCounterSettings();
	CounterDTO getCounterSettingByCounterId(Long counterId);
	CounterDTO getCounterSettingWithMasterSettingByCounterId(Long counterId);
	void manageCounterSetting(CounterDTO counterDTO);
}
