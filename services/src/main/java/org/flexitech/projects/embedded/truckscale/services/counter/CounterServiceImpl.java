package org.flexitech.projects.embedded.truckscale.services.counter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.counter.CounterDAO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CounterServiceImpl implements CounterService {

	@Autowired
	CounterDAO counterDAO;

	@Override
	public CounterDTO getCounterById(Long id) {
		Counters counter = this.counterDAO.get(id);
		if(CommonValidators.isValidObject(counter)) {
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
			}else {
				counter.setCreatedTime(new Date());
			}

			counter.setName(counterDTO.getName());
			counter.setCounterIp(counterDTO.getCounterIp());
			counter.setStatus(counterDTO.getStatus());

			counterDAO.saveOrUpdate(counter);
			return new CounterDTO(counter);
		}
		return null;
	}

	@Override
	public List<CounterDTO> getAllCounters() {
		List<Counters> counters = this.counterDAO.getAll();
		if (CommonValidators.validList(counters)) {
			return counters.stream().map(CounterDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<CounterDTO>();
	}

}
