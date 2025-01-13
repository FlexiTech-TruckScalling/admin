package org.flexitech.projects.embedded.truckscale.admin.controllers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public abstract class BaseController<DTO extends CommonDTO, Service> {

	protected final Logger logger = LogManager.getLogger(getClass());
	protected final Service service;
	private final String pageTitle;
	private final String manageUrl;

	protected BaseController(Service service, String pageTitle, String manageUrl) {
		this.service = service;
		this.pageTitle = pageTitle;
		this.manageUrl = manageUrl;
	}

	@ModelAttribute("pageTitle")
	public String pageTitle() {
		return this.pageTitle;
	}

	public String managePage(Model model, Long id, DTO dto) {
		try {
			if (CommonValidators.validLong(id)) {
				dto = getById(id);
			}
			commonModel(model, dto);
		} catch (Exception e) {
			logger.error("Error on getting customer vehicle page: {}", ExceptionUtils.getStackTrace(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return this.manageUrl;
	}

	public String managePost(DTO dto, Model model, RedirectAttributes attribute) {
		try {
			boolean update = CommonValidators.validLong(getId(dto));
			DTO managed = manage(dto);
			if (CommonValidators.isValidObject(managed)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG,
						update ? "Update success." : "Create success.");
				attribute.addFlashAttribute(CommonConstants.NEW_RECORD, getId(managed));
				return "redirect:" + this.manageUrl + ".fxt";
			} else {
				model.addAttribute(CommonConstants.ERROR_MSG, update ? "Update failed!" : "Create failed!");
			}
		} catch (Exception e) {
			logger.error("Error on manage entity: {}", ExceptionUtils.getStackTrace(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return this.manageUrl;
	}

	public String deleteEntity(DeleteDTO deleteDTO, Model model, RedirectAttributes attribute) {
		try {
			Long id = deleteDTO.getId();
			if (!CommonValidators.validLong(id)) {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete! Unknown ID.");
			}
			if (deleteById(id)) {
				attribute.addFlashAttribute(CommonConstants.SUCCESS_MSG, "Successfully deleted!");
			} else {
				attribute.addFlashAttribute(CommonConstants.ERROR_MSG, "Failed to delete!");
			}
		} catch (Exception e) {
			logger.error("Error on delete entity: {}", ExceptionUtils.getMessage(e));
			model.addAttribute(CommonConstants.ERROR_MSG, ExceptionUtils.getMessage(e));
		}
		return "redirect:" + this.manageUrl + ".fxt";
	}

	protected abstract DTO getById(Long id);

	protected abstract Long getId(DTO dto);

	protected abstract DTO manage(DTO dto) throws Exception;

	protected abstract boolean deleteById(Long id) throws Exception;

	protected abstract void commonModel(Model model, DTO dto) throws Exception;
}
