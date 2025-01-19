package org.flexitech.projects.embedded.truckscale.api.products;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.products.ProductDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.products.GoodProductListResponse;
import org.flexitech.projects.embedded.truckscale.services.products.ProductService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductAPIController {
	
	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	ProductService productService;
	
	@SuppressWarnings("unchecked")
	@GetMapping("/good-products")
	public ResponseEntity<?> getGoodProducts(@RequestParam(required = false) Long goodId){
		Response response = new Response();
		
		try {
			List<ProductDTO> products = this.productService.getProductsByGood(goodId, ActiveStatus.ACTIVE.getCode());
			GoodProductListResponse data = new GoodProductListResponse();
			data.setProducts(products);
			response = new BaseResponse<GoodProductListResponse>();
			response.setResponseCode(HttpStatus.OK.value());
			response.setResponseMessage("Getting good's products list success.");
			((BaseResponse<GoodProductListResponse>) response).setData(data);
		}catch (Exception e) {
			logger.error("Error on getting good's products: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponseMessage(ExceptionUtils.getMessage(e));
		}
		
		return ResponseUtil.send(response);
	}

}
