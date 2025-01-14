package org.flexitech.projects.embedded.truckscale.services.customers;

import java.util.List;
import org.flexitech.projects.embedded.truckscale.dto.customers.DriverDTO;

public interface DriverService {
	DriverDTO getDriverById(Long id);
	DriverDTO manageDriver(DriverDTO dto) throws Exception;
	List<DriverDTO> getAllDriver(Integer status);
	boolean deleteDriver(Long id) throws Exception;
}
