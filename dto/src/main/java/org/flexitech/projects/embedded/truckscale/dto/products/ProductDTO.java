package org.flexitech.projects.embedded.truckscale.dto.products;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.product.Products;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO extends CommonDTO {

	private String name;
	private String code;
	
	private List<GoodDTO> goods = new ArrayList<GoodDTO>();
	private List<Long> goodsId = new ArrayList<Long>();
	
	private String goodNames;
	
	public ProductDTO(Products p) {
		if(CommonValidators.isValidObject(p)) {
			this.name = p.getName();
			this.code = p.getCode();
			
			if(CommonValidators.validList(p.getGoods())) {
				this.goods = p.getGoods().stream().map(GoodDTO::new).collect(Collectors.toList());
				this.goodsId = p.getGoods().stream().map(g -> g.getId()).collect(Collectors.toList());
				this.goodNames = p.getGoods().stream().map(g->g.getName()).collect(Collectors.joining(", "));
			}
			
			setField(p);
		}
	}
	
}
