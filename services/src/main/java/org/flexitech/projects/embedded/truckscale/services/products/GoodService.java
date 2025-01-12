package org.flexitech.projects.embedded.truckscale.services.products;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.products.GoodDTO;

public interface GoodService {
	GoodDTO getGoodById(Long id);
	List<GoodDTO> getAllGoods(Integer status);
	GoodDTO manageGood(GoodDTO goodDTO);
	boolean deleteGood(Long id);
}
