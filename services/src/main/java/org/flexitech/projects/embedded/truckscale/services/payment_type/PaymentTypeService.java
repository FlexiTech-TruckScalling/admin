package org.flexitech.projects.embedded.truckscale.services.payment_type;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.payment_type.PaymentTypeDTO;

public interface PaymentTypeService {
	PaymentTypeDTO getById(Long id);
	List<PaymentTypeDTO> getAll(Integer status);
	PaymentTypeDTO managePaymentType(PaymentTypeDTO dto);
}
