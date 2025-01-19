package org.flexitech.projects.embedded.truckscale.dto.response.products;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.products.ProductDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoodProductListResponse {
	private List<ProductDTO> products;
}
