package org.flexitech.projects.embedded.truckscale.dao.superadmincustomer;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SuperAdminCustomer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class SuperAdminCustDAOImpl extends CommonDAOImpl<SuperAdminCustomer, Long> implements SuperAdminCustDAO  {

	@SuppressWarnings("unchecked")
	@Override
	public List<SuperAdminCustomer> getAllSuperAdimCustomer(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		return c.list();
	}

}
