package org.flexitech.projects.embedded.truckscale.services.counter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.counter.CounterDAO;
import org.flexitech.projects.embedded.truckscale.dao.counter.CounterSettingDAO;
import org.flexitech.projects.embedded.truckscale.dao.counter.MasterCounterSettingDAO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.entities.counters.CounterSetting;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.flexitech.projects.embedded.truckscale.entities.setting.MasterCounterSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CounterServiceImpl implements CounterService {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	CounterDAO counterDAO;
	
	@Autowired
	CounterSettingDAO counterSettingDAO;
	
	@Autowired
	MasterCounterSettingDAO masterCounterSettingDAO;

	@Override
	public CounterDTO getCounterById(Long id) {
		Counters counter = this.counterDAO.get(id);
		if (CommonValidators.isValidObject(counter)) {
			return new CounterDTO(counter);
		}
		return null;
	}

	@Override
	public CounterDTO manageCounter(CounterDTO counterDTO) {
		if (CommonValidators.isValidObject(counterDTO)) {
			Counters counter = new Counters();
			if (CommonValidators.validLong(counterDTO.getId())) {
				counter = this.counterDAO.get(counterDTO.getId());
				if (counter == null) {
					return null;
				}
				counter.setUpdatedTime(new Date());
			} else {
				counter.setCreatedTime(new Date());
			}

			counter.setName(counterDTO.getName());
			counter.setCounterIp(counterDTO.getCounterIp());
			counter.setStatus(counterDTO.getStatus());

			counterDAO.saveOrUpdate(counter);
			
//			manageCounterSettingByCounter(counter);
			
			return new CounterDTO(counter);
		}
		return null;
	}

//	private void manageCounterSettingByCounter(Counters counter) {
//		if(CommonValidators.isValidObject(counter) && CommonValidators.validLong(counter.getId())) {
//			List<CounterSetting> counterSettings = this.counterSettingDAO.getCounterSettingByCounterId(counter.getId());
//			if(!CommonValidators.validList(counterSettings)) { // res
//				List<MasterCounterSetting> masterCounterSettings = this.masterCounterSettingDAO.getAllByStatus(ActiveStatus.ACTIVE.getCode());
//			}
//		}
//	}

	@Override
	public List<CounterDTO> getAllCounters(Integer status) {
		List<Counters> counters = this.counterDAO.getAllCounterByStatus(status);
		if (CommonValidators.validList(counters)) {
			return counters.stream().map(CounterDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<CounterDTO>();
	}

	@Override
	public boolean deleteCounter(Long id) throws Exception {
		Counters counter = this.counterDAO.get(id);
		try {
			if (CommonValidators.isValidObject(counter)) {
				this.counterDAO.delete(counter);
				return true;
			}
		} catch (Exception e) {
			logger.error("Erorr while deleting counter: {}", ExceptionUtils.getStackTrace(e));
			throw new Exception(e.getMessage());
		}
		return false;
	}

}
