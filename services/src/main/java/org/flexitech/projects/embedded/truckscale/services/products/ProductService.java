package org.flexitech.projects.embedded.truckscale.services.products;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.products.ProductDTO;

public interface ProductService {
	ProductDTO getProductById(Long id);
	ProductDTO manageProduct(ProductDTO dto);
	List<ProductDTO> getAllProducts(Integer status);
	boolean deleteProduct(Long id);
}
