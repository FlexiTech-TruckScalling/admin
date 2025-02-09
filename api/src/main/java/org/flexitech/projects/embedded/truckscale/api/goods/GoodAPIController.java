package org.flexitech.projects.embedded.truckscale.api.goods;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.products.GoodDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.goods.GoodListResponseDTO;
import org.flexitech.projects.embedded.truckscale.services.products.GoodService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodAPIController {

	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private GoodService goodService;
	
	@SuppressWarnings("unchecked")
	@GetMapping
	public ResponseEntity<?> getAllGoods(){
		Response response = new Response();
		
		try {
			List<GoodDTO> goods = this.goodService.getAllGoods(ActiveStatus.ACTIVE.getCode());
			response = new BaseResponse<GoodListResponseDTO>();
			response.setResponseCode(HttpStatus.OK.value());
			response.setResponseMessage("Getting all goods success!");
			((BaseResponse<GoodListResponseDTO>) response).setData(new GoodListResponseDTO(goods));
		}catch (Exception e) {
			logger.error("Error on getting goods: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponseMessage(ExceptionUtils.getMessage(e));
		}
		
		return ResponseUtil.send(response);
	}
	
}
