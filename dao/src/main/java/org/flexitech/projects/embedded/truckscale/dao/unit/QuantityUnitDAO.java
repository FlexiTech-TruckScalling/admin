package org.flexitech.projects.embedded.truckscale.dao.unit;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.unit.QuantityUnit;

public interface QuantityUnitDAO extends CommonDAO<QuantityUnit, Long>{
	List<QuantityUnit> findByStatus(ActiveStatus status);
	boolean isSequenceAlreadyUsed(Integer sequence, Long ignoreId);
}
