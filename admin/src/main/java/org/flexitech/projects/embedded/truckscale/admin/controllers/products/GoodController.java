package org.flexitech.projects.embedded.truckscale.admin.controllers.products;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.dto.products.GoodDTO;
import org.flexitech.projects.embedded.truckscale.services.products.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GoodController {
	
	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	GoodService goodService;
	
	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return "ENL | Truck Scale Goods Management";
	}
	
	@GetMapping("goods-manage")
	public String goodsManagePage(Model model, @RequestParam(required = false) Long id) {
		GoodDTO goodDTO = new GoodDTO();
		if(CommonValidators.validLong(id)) {
			goodDTO = this.goodService.getGoodById(id);
		}
		commonModel(model, goodDTO);
		return "goods-manage";
	}
	
	@PostMapping("goods-manage")
	public String goodsManagePost(@ModelAttribute GoodDTO goodDTO, Model model, RedirectAttributes attribute) {
		try {
			boolean update = CommonValidators.validLong(goodDTO.getId());
			GoodDTO managed = this.goodService.manageGood(goodDTO);
			if(CommonValidators.isValidObject(managed)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, update?"Update success.":"Create success.");
				attribute.addFlashAttribute(CommonConstants.NEW_RECORD, managed.getId());
				return "redirect:goods-manage.fxt";
			}else {
				model.addAttribute(CommonConstants.ERROR_MSG, update?"Update failed!":"Create failed!");
			}
		}catch(Exception e) {
			logger.error("Error on manage goods: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "goods-manage";
	}
	
	@PostMapping("goods-delete")
	public String deleteGood(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attribute) {
		try {
			Long goodId = deleteDTO.getId();
			if(!CommonValidators.validLong(goodId)) {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete! Unknow id.");
			}
			if(this.goodService.deleteGood(goodId)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Successfully deleted!");
			}else {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete!");
			}
		}catch(Exception e) {
			logger.error("Error on delete goods: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "redirect:goods-manage.fxt";
	}
	
	private void commonModel(Model model, GoodDTO dto) {
		model.addAttribute("goodDTO", dto);
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("goodsList", goodService.getAllGoods(null));
	}
	
}
