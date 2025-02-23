package org.flexitech.projects.embedded.truckscale.dto.payment_type;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.payment_type.PaymentType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentTypeDTO extends CommonDTO {
	private String code;
	private String name;
	private Integer sequence;
	
	public PaymentTypeDTO(PaymentType p) {
		if(!CommonValidators.isValidObject(p)) return;
		this.code = p.getCode();
		this.name = p.getName();
		this.sequence = p.getSequence();
		
		setField(p);
	}
}
