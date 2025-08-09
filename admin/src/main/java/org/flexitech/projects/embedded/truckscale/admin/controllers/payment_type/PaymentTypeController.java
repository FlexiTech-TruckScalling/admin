package org.flexitech.projects.embedded.truckscale.admin.controllers.payment_type;

import org.flexitech.projects.embedded.truckscale.admin.controllers.BaseController;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.payment_type.PaymentTypeDTO;
import org.flexitech.projects.embedded.truckscale.dto.products.ProductDTO;
import org.flexitech.projects.embedded.truckscale.services.payment_type.PaymentTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaymentTypeController extends BaseController<PaymentTypeDTO, PaymentTypeService>{

	protected PaymentTypeController(PaymentTypeService service) {
		super(service, "Flexitech | Truck Scale Products Management", "payment-type-manage");
	}

	@GetMapping("payment-type-manage")
    public String productsPage(Model model, @RequestParam(required = false) Long id) {
    	return super.managePage(model, id, new PaymentTypeDTO());
    }
    
    @PostMapping("payment-type-manage")
    public String productManage(@ModelAttribute PaymentTypeDTO paymentTypeDTO, Model model, RedirectAttributes attr) {
    	return super.managePost(paymentTypeDTO, model, attr);
    }
	
	@Override
	protected PaymentTypeDTO getById(Long id) {
		return service.getById(id);
	}

	@Override
	protected Long getId(PaymentTypeDTO dto) {
		return dto.getId();
	}

	@Override
	protected PaymentTypeDTO manage(PaymentTypeDTO dto) throws Exception {
		return service.managePaymentType(dto);
	}

	@Override
	protected boolean deleteById(Long id) throws Exception {
		return false;
	}

	@Override
	protected void commonModel(Model model, PaymentTypeDTO dto) throws Exception {
		model.addAttribute("paymentTypeDTO", dto);
        model.addAttribute("statusList", ActiveStatus.getAll());
        model.addAttribute("paymentTypeList", service.getAll(null));
	}

}
