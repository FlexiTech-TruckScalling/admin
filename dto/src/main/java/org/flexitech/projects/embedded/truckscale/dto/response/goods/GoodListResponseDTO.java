package org.flexitech.projects.embedded.truckscale.dto.response.goods;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.products.GoodDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodListResponseDTO {
	private List<GoodDTO> goods;
}
