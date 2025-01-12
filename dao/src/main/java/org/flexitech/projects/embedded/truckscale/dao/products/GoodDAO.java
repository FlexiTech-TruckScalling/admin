package org.flexitech.projects.embedded.truckscale.dao.products;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.product.Goods;

public interface GoodDAO extends CommonDAO<Goods, Long>{
	List<Goods> getAllGoods(Integer status);
}
