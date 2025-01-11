package org.flexitech.projects.embedded.truckscale.dao.setting;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.setting.WeightUnit;

public interface WeightUnitDAO extends CommonDAO<WeightUnit, Long> {
	List<WeightUnit> getAllWeightUnit(Integer status);

	boolean isSequenceAlreadyUsed(Integer sequence, Long ignoreId);
}
