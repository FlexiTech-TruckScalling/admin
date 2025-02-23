package org.flexitech.projects.embedded.truckscale.dao.payment_type;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.payment_type.PaymentType;

public interface PaymentTypeDAO extends CommonDAO<PaymentType, Long>{
	List<PaymentType> getAll(Integer status);
}
