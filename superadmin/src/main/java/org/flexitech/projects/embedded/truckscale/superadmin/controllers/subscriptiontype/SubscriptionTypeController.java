package org.flexitech.projects.embedded.truckscale.superadmin.controllers.subscriptiontype;

import org.flexitech.projects.embedded.truckscale.admin.controllers.BaseController;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.products.ProductDTO;
import org.flexitech.projects.embedded.truckscale.dto.superadminuser.SubscriptionTypeDTO;
import org.flexitech.projects.embedded.truckscale.services.superadmin.subscriptiontype.SubscriptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubscriptionTypeController extends BaseController<SubscriptionTypeDTO, SubscriptionTypeService> {
	@Autowired
	SubscriptionTypeService subTypeService;
	
	public SubscriptionTypeController(SubscriptionTypeService service, String pageTitle, String manageUrl) {
		super(service, pageTitle, manageUrl);
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("subtype-manage")
    public String subTypePage(Model model, @RequestParam(required = false) Long id) {
    	return super.managePage(model, id, new SubscriptionTypeDTO());
    }
    
    @PostMapping("subtype-manage")
    public String subTypeManage(@ModelAttribute SubscriptionTypeDTO subTypeDTO, Model model, RedirectAttributes attr) {
    	return super.managePost(subTypeDTO, model, attr);
    }

	@Override
	protected SubscriptionTypeDTO getById(Long id) {
		return subTypeService.getSubscriptionTypeById(id);
	}

	@Override
	protected Long getId(SubscriptionTypeDTO dto) {
		return dto.getId();
	}

	@Override
	protected SubscriptionTypeDTO manage(SubscriptionTypeDTO dto) throws Exception {
		return subTypeService.manageSubscriptionType(dto);
	}

	@Override
	protected boolean deleteById(Long id) throws Exception {
		return subTypeService.deleteSubscriptionType(id);
	}

	@Override
	protected void commonModel(Model model, SubscriptionTypeDTO dto) throws Exception {
		model.addAttribute("subTypeDTO", dto);
        model.addAttribute("statusList", ActiveStatus.getAll());
        model.addAttribute("subTypeList", subTypeService.getAllSubscriptionType(null));
		
	}
}
