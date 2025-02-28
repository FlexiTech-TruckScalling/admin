package org.flexitech.projects.embedded.truckscale.admin.controllers.reports;

import org.flexitech.projects.embedded.truckscale.common.SystemSettingConstants;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.common.enums.MathSign;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.services.payment_type.PaymentTypeService;
import org.flexitech.projects.embedded.truckscale.services.products.GoodService;
import org.flexitech.projects.embedded.truckscale.services.products.ProductService;
import org.flexitech.projects.embedded.truckscale.services.setting.SystemSettingService;
import org.flexitech.projects.embedded.truckscale.services.weight_transaction.WeightTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransactionReportController{

	@Autowired
	private WeightTransactionService weightTransactionService;
	
	@Autowired
	private GoodService goodService;
	
	@Autowired
	private PaymentTypeService paymentTypeService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private SystemSettingService systemSettingService;
	
	@GetMapping("/transaction-report")
	public String searchReportPage(Model model) {
		TransactionSearchDTO searchDTO = new TransactionSearchDTO();
		searchDTO.setPageNo(1);
		commonSearchModel(model, searchDTO);
		return "transaction-report";
	}
	
	@PostMapping("/transaction-report")
	public String searchTransactionReport(@ModelAttribute TransactionSearchDTO searchDTO, Model model) {
		commonSearchModel(model, searchDTO);
		return "transaction-report";
	}
	
	@GetMapping("/transaction-detail")
	public String transactionDetail(@RequestParam(required = false) Long id, Model model) {
		model.addAttribute("transactionDTO", this.weightTransactionService.getById(id));
		return "transaction-detail";
	}
	
	private void commonSearchModel(Model model, TransactionSearchDTO searchDTO) {
		model.addAttribute("pageTitle", "ENL | Truck Scale Transaction Search");
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("transactionList", weightTransactionService.searchTransactions(searchDTO));
		
		Integer total = this.weightTransactionService.countTotalTransaction(searchDTO);
		
		int totalPages = (int) Math.ceil((double) total / searchDTO.getLimit());
		
		searchDTO.setPageCount(totalPages);
		searchDTO.setTotalRecords(total);
		
		model.addAttribute("searchDTO", searchDTO);
		model.addAttribute("goodList", goodService.getAllGoods(ActiveStatus.ACTIVE.getCode()));
		model.addAttribute("paymentTypeList", paymentTypeService.getAll(ActiveStatus.ACTIVE.getCode()));
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("productList", this.productService.getAllProducts(ActiveStatus.ACTIVE.getCode()));
		model.addAttribute("mathSignList", MathSign.getAll());
		model.addAttribute("inOutStatusList", InOutBounds.getAll());
	}

}
