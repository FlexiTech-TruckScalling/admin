package org.flexitech.projects.embedded.truckscale.dto.products;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.product.Goods;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoodDTO extends CommonDTO {
	private String name;
	private Integer code;
	
	public GoodDTO(Goods g) {
		if(CommonValidators.isValidObject(g)) {
			this.name = g.getName();
			this.code = g.getCode();
			
			setField(g);
		}
	}
}
