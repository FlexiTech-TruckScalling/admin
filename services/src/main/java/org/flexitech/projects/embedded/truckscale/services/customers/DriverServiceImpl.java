package org.flexitech.projects.embedded.truckscale.services.customers;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.customers.DriverDAO;
import org.flexitech.projects.embedded.truckscale.dto.customers.DriverDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.Drivers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private DriverDAO driverDAO;
	
	@Override
	public DriverDTO getDriverById(Long id) {
		Drivers d = this.driverDAO.get(id);
		if(CommonValidators.isValidObject(d)) {
			return new DriverDTO(d);
		}
		return null;
	}

	@Override
	public DriverDTO manageDriver(DriverDTO dto) throws Exception {
		if(!CommonValidators.isValidObject(dto)) {
			throw new Exception("Reference object cannot be null!");
		}
		Drivers d = null;
		if(CommonValidators.validLong(dto.getId())) {
			d = this.driverDAO.get(dto.getId());
			d.setUpdatedTime(new Date());
		}else {
			d = new Drivers();
			d.setCreatedTime(new Date());
		}
		d.setName(dto.getName());
		d.setPhoneNumber(dto.getPhoneNumber());
		d.setStatus(dto.getStatus());
		
		this.driverDAO.saveOrUpdate(d);
		return new DriverDTO(d);
	}

	@Override
	public List<DriverDTO> getAllDriver(Integer status) {
		List<Drivers> drivers = this.driverDAO.getAllDrivers(status);
		if(CommonValidators.validList(drivers)) {
			return drivers.stream().map(DriverDTO::new).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public boolean deleteDriver(Long id) throws Exception {
		try {
			Drivers d = this.driverDAO.get(id);
			if(CommonValidators.isValidObject(d)) {
				this.driverDAO.delete(d);
				return true;
			}
		}catch(Exception e) {
			logger.error("Error on deleting driver : {}", ExceptionUtils.getStackTrace(e));
			return false;
		}
		return false;
	}

}
