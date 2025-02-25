package org.flexitech.projects.embedded.truckscale.dao.company;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.company.Company;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAOImpl extends CommonDAOImpl<Company, Long> implements CompanyDAO{

}
