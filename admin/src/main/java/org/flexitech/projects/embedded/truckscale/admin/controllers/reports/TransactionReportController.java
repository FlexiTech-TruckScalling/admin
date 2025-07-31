package org.flexitech.projects.embedded.truckscale.admin.controllers.reports;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.ReportConstants;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.common.enums.MathSign;
import org.flexitech.projects.embedded.truckscale.common.enums.TransactionStatus;
import org.flexitech.projects.embedded.truckscale.dto.reports.transaction.TransactionReportDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.services.payment_type.PaymentTypeService;
import org.flexitech.projects.embedded.truckscale.services.products.GoodService;
import org.flexitech.projects.embedded.truckscale.services.products.ProductService;
import org.flexitech.projects.embedded.truckscale.services.reports.ReportService;
import org.flexitech.projects.embedded.truckscale.services.weight_transaction.WeightTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransactionReportController {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private WeightTransactionService weightTransactionService;

	@Autowired
	private GoodService goodService;

	@Autowired
	private PaymentTypeService paymentTypeService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ReportService reportService;

	@GetMapping("/transaction-report")
	public String searchReportPage(Model model) {
		TransactionSearchDTO searchDTO = new TransactionSearchDTO();
		searchDTO.setPageNo(1);
		commonSearchModel(model, searchDTO);
		return "transaction-report";
	}

	@PostMapping(value = "/transaction-report")
	public String searchTransactionReport(@ModelAttribute TransactionSearchDTO searchDTO, Model model) {
		commonSearchModel(model, searchDTO);
		return "transaction-report";
	}

	@PostMapping(value = "/transaction-report", params = { "ExportExcel" })
	public void excelExportTransactionReport(@ModelAttribute TransactionSearchDTO searchDTO, HttpServletRequest request,
			HttpServletResponse response) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			String reportPath = request.getServletContext().getRealPath("/") + ReportConstants.REPORT_PATH
					+ ReportConstants.TRANSCTION_REPORT;
			List<TransactionDTO> list = this.weightTransactionService.searchTransactions(searchDTO, true);

			if (!CommonValidators.validList(list))
				return;

			List<TransactionReportDTO> dataList = list.stream().map(TransactionReportDTO::new)
					.collect(Collectors.toList());

			String fileName = System.currentTimeMillis() + "_transaction_report.xlsx";
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			reportService.generateReportExcel(dataList, baos, reportPath, fileName, parameter);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			response.setContentLength(baos.size());

			baos.writeTo(response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("Error on excel export transaction report: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	@PostMapping(value = "/transaction-report", params = { "ExportPDF" })
	public void pdfExportTransactionReport(@ModelAttribute TransactionSearchDTO searchDTO, HttpServletRequest request,
			HttpServletResponse response) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			String reportPath = request.getServletContext().getRealPath("/") + ReportConstants.REPORT_PATH
					+ ReportConstants.TRANSCTION_REPORT;
			List<TransactionDTO> list = this.weightTransactionService.searchTransactions(searchDTO, true);

			if (!CommonValidators.validList(list))
				return;

			List<TransactionReportDTO> dataList = list.stream().map(TransactionReportDTO::new)
					.collect(Collectors.toList());

			String fileName = System.currentTimeMillis() + "_transaction_report.pdf";
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			reportService.generateReportPdf(dataList, baos, reportPath, fileName, parameter);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			response.setContentLength(baos.size());

			baos.writeTo(response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("Error on excel export transaction report: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	@GetMapping("/transaction-detail")
	public String transactionDetail(@RequestParam(required = false) Long id, Model model) {
		model.addAttribute("transactionDTO", this.weightTransactionService.getById(id));
		model.addAttribute("pageTitle", "ENL | Truck Scale Transaction Detail");
		return "transaction-detail";
	}

	private void commonSearchModel(Model model, TransactionSearchDTO searchDTO) {
		searchDTO.setExcludeCancel(false);
		model.addAttribute("pageTitle", "ENL | Truck Scale Transaction Search");
		model.addAttribute("statusList", ActiveStatus.getAll());
		model.addAttribute("transactionStatusList", TransactionStatus.getAll());
		model.addAttribute("transactionList", weightTransactionService.searchTransactions(searchDTO, false));

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
		model.addAttribute("inOutStatusList", InOutBounds.getAll(false));
	}

}
