package org.flexitech.projects.embedded.truckscale.services.setting;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.setting.WeightUnitDTO;

public interface WeightUnitService {
	WeightUnitDTO getWeightUnitById(Long id);
	WeightUnitDTO manageWeightUnit(WeightUnitDTO dto);
	List<WeightUnitDTO> getAllWeightUnit(Integer status);
	boolean deleteWeightUnit(Long id);
	boolean isSequenceAlreadyUsed(Integer sequence, Long ignoreId);
}
