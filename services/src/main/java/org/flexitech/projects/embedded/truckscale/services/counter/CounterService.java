package org.flexitech.projects.embedded.truckscale.services.counter;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;

public interface CounterService {
	CounterDTO getCounterById(Long id);
	CounterDTO manageCounter(CounterDTO counterDTO);
	List<CounterDTO> getAllCounters(Integer status);
	boolean deleteCounter(Long id) throws Exception;
}
