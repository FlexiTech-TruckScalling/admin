package org.flexitech.projects.embedded.truckscale.services.payment_type;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.payment_type.PaymentTypeDAO;
import org.flexitech.projects.embedded.truckscale.dto.payment_type.PaymentTypeDTO;
import org.flexitech.projects.embedded.truckscale.entities.payment_type.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentTypeServiceImpl implements PaymentTypeService{

	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private PaymentTypeDAO paymentTypeDAO;
	
	@Override
	public PaymentTypeDTO getById(Long id) {
		PaymentType type = this.paymentTypeDAO.get(id);
		if(CommonValidators.isValidObject(type)) {
			return new PaymentTypeDTO(type);
		}
		return null;
	}

	@Override
	public List<PaymentTypeDTO> getAll(Integer status) {
		List<PaymentType> types = this.paymentTypeDAO.getAll(status);
		if(CommonValidators.validList(types)) {
			return types.stream().map(PaymentTypeDTO::new).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public PaymentTypeDTO managePaymentType(PaymentTypeDTO dto) {
		if(!CommonValidators.isValidObject(dto)) return null;
		PaymentType p = null;
		if(CommonValidators.validLong(dto.getId())) {
			p = this.paymentTypeDAO.get(dto.getId());
			p.setUpdatedTime(new Date());
		}else {
			p = new PaymentType();
			p.setCreatedTime(new Date());
		}
		p.setName(dto.getName());
		p.setCode(dto.getCode());
		p.setSequence(dto.getSequence());
		p.setStatus(dto.getStatus());
		
		this.paymentTypeDAO.saveOrUpdate(p);
		
		return new PaymentTypeDTO(p);
	}

}
