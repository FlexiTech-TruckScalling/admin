package org.flexitech.projects.embedded.truckscale.dao.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.Customers;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAOImpl extends CommonDAOImpl<Customers, Long> implements CustomerDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Customers> getAllCustomer(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customers> searchCustomer(CustomerSearchDTO searchDTO) {
		SQLQuery query = getCurrentSession().createSQLQuery(prepareSQLForSearch(searchDTO, false));
		setParametersForSearchQuery(query, searchDTO, false);
		query.addEntity(daoType);
		return query.list();
	}

	@Override
	public Integer countCustomer(CustomerSearchDTO searchDTO) {
		SQLQuery query = getCurrentSession().createSQLQuery(prepareSQLForSearch(searchDTO, true));
		setParametersForSearchQuery(query, searchDTO, true);
		return ((Number) query.uniqueResult()).intValue();
	}
	
	private String prepareSQLForSearch(CustomerSearchDTO searchDTO, boolean countOnly) {
		StringBuilder b = new StringBuilder();
		
		if (countOnly) {
			b.append(" SELECT COUNT(DISTINCT c.id) ");
		} else {
			b.append(" SELECT c.* ");
		}
		
		b.append("From customers c ")
			.append("LEFT OUTER JOIN customers_customer_types cct ON cct.customer_id = c.id ")
			.append("LEFT OUTER JOIN customer_vehicles cv ON cv.customer_id = c.id ");
		
		b.append("WHERE 1=1 ");
		
		if(CommonValidators.validString(searchDTO.getName())) {
			b.append("AND c.name LIKE :name ");
		}
		
		if(CommonValidators.validString(searchDTO.getCode())) {
			b.append("AND c.code = :code ");
		}
		
		if(CommonValidators.validString(searchDTO.getPhoneNumber())) {
			b.append("AND c.phone_number = :phoneNumber ");
		}
		
		if(CommonValidators.validLong(searchDTO.getCustomerType())) {
			b.append("AND cct.customer_type_id = :cctId ");
		}
		
		if(CommonValidators.validString(searchDTO.getVehicleNumber())) {
			b.append("AND cv.number = :vehicleNo ");
		}
		
		if(CommonValidators.validInteger(searchDTO.getStatus())) {
			b.append("AND c.status = :status ");
		}
		
		if (!countOnly) {
	        b.append(" LIMIT :limit OFFSET :offset ");
	    }
		
		return b.toString();
	}


	private void setParametersForSearchQuery(SQLQuery query, CustomerSearchDTO searchDTO, boolean countOnly) {
	    if (CommonValidators.validString(searchDTO.getName())) {
	        query.setParameter("name", "%" + searchDTO.getName() + "%");
	    }

	    if (CommonValidators.validString(searchDTO.getCode())) {
	        query.setParameter("code", searchDTO.getCode());
	    }

	    if (CommonValidators.validString(searchDTO.getPhoneNumber())) {
	        query.setParameter("phoneNumber", searchDTO.getPhoneNumber());
	    }

	    if (CommonValidators.validLong(searchDTO.getCustomerType())) {
	        query.setParameter("cctId", searchDTO.getCustomerType());
	    }

	    if (CommonValidators.validString(searchDTO.getVehicleNumber())) {
	        query.setParameter("vehicleNo", searchDTO.getVehicleNumber());
	    }

	    if (CommonValidators.validInteger(searchDTO.getStatus())) {
	        query.setParameter("status", searchDTO.getStatus());
	    }
	    
	    if (!countOnly) {
	        query.setParameter("limit", searchDTO.getLimit());
	        query.setParameter("offset", (searchDTO.getPageNo() - 1) * searchDTO.getLimit());
	    }
	}

}
