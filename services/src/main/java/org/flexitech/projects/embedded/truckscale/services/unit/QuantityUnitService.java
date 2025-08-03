package org.flexitech.projects.embedded.truckscale.services.unit;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.unit.QuantityUnitDTO;

public interface QuantityUnitService {
	QuantityUnitDTO getById(Long id);
	boolean isSequenceAlreadyUsed(Integer sequence, Long id);
	QuantityUnitDTO manageQuantityUnit(QuantityUnitDTO dto) throws Exception;
	List<QuantityUnitDTO> getQuantityUnitsByStatus(ActiveStatus status);
	boolean deleteQuantityUnit(Long id) throws Exception;
}
