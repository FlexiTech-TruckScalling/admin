package org.flexitech.projects.embedded.truckscale.services.setting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.utils.ExceptionUtils;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.setting.WeightUnitDAO;
import org.flexitech.projects.embedded.truckscale.dto.setting.WeightUnitDTO;
import org.flexitech.projects.embedded.truckscale.entities.setting.WeightUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeightUnitServiceImpl implements WeightUnitService {
	
	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	WeightUnitDAO weightUnitDAO;

	@Override
	public WeightUnitDTO getWeightUnitById(Long id) {
		WeightUnit unit = weightUnitDAO.get(id);
		if(CommonValidators.isValidObject(unit)) {
			return new WeightUnitDTO(unit);
		}
		return null;
	}

	@Override
	public WeightUnitDTO manageWeightUnit(WeightUnitDTO dto) {

		if(!CommonValidators.isValidObject(dto)) return null;
		WeightUnit unit = null;
		if(CommonValidators.validLong(dto.getId())) {
			unit = weightUnitDAO.get(dto.getId());
			unit.setUpdatedTime(new Date());
		}else {
			unit = new WeightUnit();
			unit.setCreatedTime(new Date());
		}
		
		unit.setName(dto.getName());
		unit.setCode(dto.getCode());
		unit.setSequence(dto.getSequence());
		unit.setStatus(dto.getStatus());
		unit.setPerKgValue(dto.getPerKgValue());
		this.weightUnitDAO.saveOrUpdate(unit);
		
		return new WeightUnitDTO(unit);
	}

	@Override
	public List<WeightUnitDTO> getAllWeightUnit(Integer status) {
		List<WeightUnit> lists = weightUnitDAO.getAllWeightUnit(status);
		if(CommonValidators.validList(lists)) {
			return lists.stream().map(WeightUnitDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<WeightUnitDTO>();
	}

	@Override
	public boolean deleteWeightUnit(Long id) {
		try {
			WeightUnit unit = weightUnitDAO.get(id);
			if(CommonValidators.isValidObject(unit)) {
				this.weightUnitDAO.delete(unit);
				return true;
			}
			return false;
		}catch(Exception e) {
			logger.error("Error on deleting weight unit: {}", ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	@Override
	public boolean isSequenceAlreadyUsed(Integer sequence, Long ignoreId) {
		return weightUnitDAO.isSequenceAlreadyUsed(sequence, ignoreId);
	}

}
