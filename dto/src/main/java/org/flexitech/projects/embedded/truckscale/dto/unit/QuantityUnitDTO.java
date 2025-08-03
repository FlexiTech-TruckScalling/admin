package org.flexitech.projects.embedded.truckscale.dto.unit;

import java.math.BigDecimal;

import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.unit.QuantityUnit;
import org.flexitech.projects.embedded.truckscale.util.CommonUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityUnitDTO extends CommonDTO{
	private String name;
	private Integer sequence;
	private String code;
	private BigDecimal pricePerUnit;
	private String pricePerUnitDesc;
	
	public QuantityUnitDTO(QuantityUnit q) {
		if(q!= null) {
			this.name = q.getName();
			this.code = q.getCode();
			this.sequence = q.getSequence();
			this.pricePerUnit = q.getPricePerUnit();
			this.pricePerUnitDesc = CommonUtil.formatNumber(pricePerUnit);
			
			setField(q);
		}
	}
	
}
