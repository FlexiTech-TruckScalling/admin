package org.flexitech.projects.embedded.truckscale.services.products;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.products.GoodDAO;
import org.flexitech.projects.embedded.truckscale.dto.products.GoodDTO;
import org.flexitech.projects.embedded.truckscale.entities.product.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class GoodServiceImpl implements GoodService{
	
	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	GoodDAO goodDAO;
	
	@Override
	public GoodDTO getGoodById(Long id) {
		Goods g = goodDAO.get(id);
		if(CommonValidators.isValidObject(g)) {
			return new GoodDTO(g);
		}
		return null;
	}

	@Override
	public List<GoodDTO> getAllGoods(Integer status) {
		List<Goods> goods = this.goodDAO.getAllGoods(status);
		if(CommonValidators.validList(goods)) {
			return goods.stream().map(GoodDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<GoodDTO>();
	}

	@Override
	public GoodDTO manageGood(GoodDTO goodDTO) {
		if(!CommonValidators.isValidObject(goodDTO)) return null;
		Goods g = null;
		if(CommonValidators.validLong(goodDTO.getId())) {
			g = this.goodDAO.get(goodDTO.getId());
			g.setUpdatedTime(new Date());
		}else {
			g = new Goods();
			g.setCreatedTime(new Date());
		}
		g.setName(goodDTO.getName());
		g.setStatus(goodDTO.getStatus());
		g.setCode(goodDTO.getCode());
		
		this.goodDAO.saveOrUpdate(g);
		
		return new GoodDTO(g);
	}

	@Override
	public boolean deleteGood(Long id) {
		try {
			Goods g = this.goodDAO.get(id);
			if(CommonValidators.isValidObject(g)) {
				this.goodDAO.delete(g);
				return true;
			}
			return false;
		}catch (Exception e) {
			logger.error("Error on deleting Good: {}", ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

}
