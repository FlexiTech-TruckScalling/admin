package org.flexitech.projects.embedded.truckscale.dao.superadmincustomer;

import java.util.List;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SuperAdminCustomer;

public interface SuperAdminCustDAO extends CommonDAO<SuperAdminCustomer, Long> {
	List<SuperAdminCustomer> getAllSuperAdimCustomer(Integer status);
}
