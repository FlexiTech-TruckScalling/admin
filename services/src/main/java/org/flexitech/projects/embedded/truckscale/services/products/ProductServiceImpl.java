package org.flexitech.projects.embedded.truckscale.services.products;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.products.GoodDAO;
import org.flexitech.projects.embedded.truckscale.dao.products.ProductDAO;
import org.flexitech.projects.embedded.truckscale.dto.products.ProductDTO;
import org.flexitech.projects.embedded.truckscale.entities.product.Goods;
import org.flexitech.projects.embedded.truckscale.entities.product.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductDAO productDAO;

	@Autowired
	GoodDAO goodDAO;

	private final Logger logger = LogManager.getLogger(getClass());

	@Override
	public ProductDTO getProductById(Long id) {
		Products p = this.productDAO.get(id);
		if (CommonValidators.isValidObject(p)) {
			return new ProductDTO(p);
		}
		return null;
	}

	@Override
	public ProductDTO manageProduct(ProductDTO dto) {
		if (!CommonValidators.isValidObject(dto))
			return null;
		Products p = null;
		if (CommonValidators.validLong(dto.getId())) {
			p = this.productDAO.get(dto.getId());
			p.setUpdatedTime(new Date());
		} else {
			p = new Products();
			p.setCreatedTime(new Date());
		}
		p.setName(dto.getName());
		p.setCode(dto.getCode());
		p.setStatus(dto.getStatus());

		if (CommonValidators.validList(dto.getGoodsId())) {
			Set<Goods> updatedGoods = new HashSet<>();

			// Add new or existing goods that are valid
			for (Long id : dto.getGoodsId()) {
				if (CommonValidators.validLong(id)) {
					Goods good = goodDAO.get(id);
					if (good != null) {
						updatedGoods.add(good);
					} else {
						logger.warn("Good with ID {} not found in the database.", id);
					}
				} else {
					logger.warn("GoodDTO with invalid ID found: {}", id);
				}
			}

			Set<Goods> currentGoods = p.getGoods();

			currentGoods.removeIf(existingGood -> !updatedGoods.contains(existingGood));

			currentGoods.addAll(updatedGoods);

			p.setGoods(currentGoods);
		}

		this.productDAO.saveOrUpdate(p);

		return new ProductDTO(p);
	}

	@Override
	public List<ProductDTO> getAllProducts(Integer status) {
		List<Products> products = this.productDAO.getAllProducts(status);
		if (CommonValidators.validList(products)) {
			return products.stream().map(ProductDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<ProductDTO>();
	}

	@Override
	public boolean deleteProduct(Long id) {
		try {
			Products p = this.productDAO.get(id);
			if (CommonValidators.isValidObject(p)) {
				this.productDAO.delete(p);
				return true;
			}
		} catch (Exception e) {
			logger.error("Error on deleting product: {}", ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

}
