package org.flexitech.projects.embedded.truckscale.services.unit;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.unit.QuantityUnitDAO;
import org.flexitech.projects.embedded.truckscale.dto.unit.QuantityUnitDTO;
import org.flexitech.projects.embedded.truckscale.entities.unit.QuantityUnit;
import org.springframework.stereotype.Service;


@Service
public class QuantityUnitServiceImpl implements QuantityUnitService {

	private final QuantityUnitDAO quantityUnitDAO;
	
	private final Logger logger = LogManager.getLogger(getClass());
	
	public QuantityUnitServiceImpl(QuantityUnitDAO dao) {
		this.quantityUnitDAO = dao;
	}
	
	@Override
	public QuantityUnitDTO getById(Long id) {
		QuantityUnit q = this.quantityUnitDAO.get(id);
		if(q != null) {
			return new QuantityUnitDTO(q);
		}
		return null;
	}

	@Override
	public QuantityUnitDTO manageQuantityUnit(QuantityUnitDTO dto) throws Exception{
		if(dto == null) return null;
		QuantityUnit u = null;
		if(CommonValidators.validLong(dto.getId())) {
			u = this.quantityUnitDAO.get(dto.getId());
			if(u == null) {
				throw new Exception("Cannot find data with give id!");
			}
			u.setUpdatedTime(new Date());
		}else {
			u = new QuantityUnit();
			u.setCreatedTime(new Date());
		}
		u.setName(dto.getName());
		u.setCode(dto.getCode());
		u.setStatus(dto.getStatus());
		u.setPricePerUnit(dto.getPricePerUnit());
		u.setSequence(dto.getSequence());
		this.quantityUnitDAO.saveOrUpdate(u);
		
		return new QuantityUnitDTO(u);
	}

	@Override
	public List<QuantityUnitDTO> getQuantityUnitsByStatus(ActiveStatus status) {
		List<QuantityUnit> data = this.quantityUnitDAO.findByStatus(status);
		if(!CommonValidators.validList(data)) {
			return Collections.emptyList();
		}
		return data.stream().map(QuantityUnitDTO::new).toList();
	}

	@Override
	public boolean deleteQuantityUnit(Long id) throws Exception {
		QuantityUnit u = this.quantityUnitDAO.get(id);
		if(u == null) {
			throw new Exception("Cannot find data with give id!");
		}
		try {
			this.quantityUnitDAO.delete(u);
			return true;
		} catch (Exception e) {
			logger.error("Error on deleting quantity unit: {}", ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean isSequenceAlreadyUsed(Integer sequence, Long id) {
		return this.quantityUnitDAO.isSequenceAlreadyUsed(sequence, id);
	}

}
