package org.flexitech.projects.embedded.truckscale.dao.products;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.product.Products;

public interface ProductDAO extends CommonDAO<Products, Long> {
	List<Products> getAllProducts(Integer status);
}
