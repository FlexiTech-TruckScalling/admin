package org.flexitech.projects.embedded.truckscale.api.wight_transaction;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.network.response.BaseResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.ErrorResponse;
import org.flexitech.projects.embedded.truckscale.common.network.response.Response;
import org.flexitech.projects.embedded.truckscale.dto.request.transactions.WeightTransactionRequest;
import org.flexitech.projects.embedded.truckscale.dto.response.CommonPagingDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionPreloadDataResponse;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionResponse;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.services.weight_transaction.WeightTransactionService;
import org.flexitech.projects.embedded.truckscale.util.network.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weight-transactions")
public class WeightTransactinAPIController {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	WeightTransactionService weightTransactionService;

	@SuppressWarnings("unchecked")
	@GetMapping("/preload-data")
	public ResponseEntity<?> getWeightTransactionPreloadData(@RequestParam(required = false) Long userId) {
		Response response = new Response();
		try {
			if (CommonValidators.validLong(userId)) {
				WeightTransactionPreloadDataResponse data = weightTransactionService
						.getWeightTransactionPreloadData(userId);
				response = new BaseResponse<WeightTransactionPreloadDataResponse>();
				response.setResponseCode(HttpStatus.OK.value());
				response.setResponseMessage("Getting preload data success");
				((BaseResponse<WeightTransactionPreloadDataResponse>) response).setData(data);
			} else {
				response = new ErrorResponse<String>();
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setResponseMessage("Please provide required id!");
			}
		} catch (Exception e) {
			logger.error("Error on getting weight transaction preload data: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponseMessage(ExceptionUtils.getMessage(e));
		}
		return ResponseUtil.send(response);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/manage-transaction")
	public ResponseEntity<?> manageTransaction(@RequestBody WeightTransactionRequest request) {
		Response response = new Response();

		try {
			WeightTransactionResponse res = this.weightTransactionService.manageWeightTransaction(request);
			response = new BaseResponse<WeightTransactionResponse>();
			response.setResponseCode(HttpStatus.OK.value());
			response.setResponseMessage("Success.");
			((BaseResponse<WeightTransactionResponse>) response).setData(res);
		} catch (Exception e) {
			logger.error("Error on manage transaction: {}", ExceptionUtils.getStackTrace(e));
			response = new ErrorResponse<String>();
			if (e instanceof IllegalArgumentException) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
			} else {
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			response.setResponseMessage(ExceptionUtils.getMessage(e));
		}

		return ResponseUtil.send(response);
	}

	@SuppressWarnings("unchecked")
	@PutMapping("/cancel")
	public ResponseEntity<?> cancelTransaction(@RequestParam(required = false) Long id) {
		Response response = new Response();
		if (!CommonValidators.validLong(id)) {
			response.setResponseCode(HttpStatus.BAD_REQUEST.value());
			response.setResponseMessage("Please provide the transaction id!");
		} else {
			try {
				Long trnId = this.weightTransactionService.cancelTransaction(id);
				if (CommonValidators.validLong(trnId)) {
					response = new BaseResponse<Long>();
					response.setResponseCode(HttpStatus.OK.value());
					response.setResponseMessage("Transaction cancel successfully!");
					((BaseResponse<Long>) response).setData(trnId);
				} else {
					response.setResponseCode(HttpStatus.NO_CONTENT.value());
					response.setResponseMessage("Failed to cancel transaction!");
				}
			} catch (Exception e) {
				response = new ErrorResponse<String>();
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				if (e instanceof IllegalArgumentException) {
					response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				}
				response.setResponseMessage(ExceptionUtils.getStackTrace(e));
			}
		}
		return ResponseUtil.send(response);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/search")
	public ResponseEntity<?> searchTransactions(@RequestBody TransactionSearchDTO searchDTO,@RequestParam(required = false) Integer page) {
		Response response = new Response();
		Integer pageNo = CommonValidators.validInteger(page) ? page : 1;
		searchDTO.setPageNo(pageNo);
		try {
			List<TransactionDTO> trans = this.weightTransactionService.searchTransactions(searchDTO);
			response = new BaseResponse<CommonPagingDTO<TransactionDTO>>();
			response.setResponseCode(HttpStatus.OK.value());
			response.setResponseMessage("Search transaction successfully!");
			CommonPagingDTO<TransactionDTO> dto = new CommonPagingDTO<TransactionDTO>();
			
			int totalRecords = this.weightTransactionService.countTotalTransaction(searchDTO);
		    int pageSize = searchDTO.getLimit();
		    int currentPage = pageNo;
		    
		    int totalPage = (int) Math.ceil((double) totalRecords / pageSize);
		    boolean hasNextPage = currentPage < totalPage;

		    String nextPageUrl = hasNextPage ? "weight-transactions/search?page=" + (currentPage + 1) + "&size=" + pageSize : null;

		    dto.setPage(currentPage);
		    dto.setTotalPage(totalPage);
		    dto.setHasNextPage(hasNextPage);
		    dto.setNextPageUrl(nextPageUrl);
		    dto.setTotalRecord(totalRecords);
		    dto.setData(trans);
			((BaseResponse<CommonPagingDTO<TransactionDTO>>) response).setData(dto);
		} catch (Exception e) {
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			if (e instanceof IllegalArgumentException) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
			}
			response.setResponseMessage(ExceptionUtils.getStackTrace(e));
		}
		return ResponseUtil.send(response);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping()
	public ResponseEntity<?> getTransactionData(@RequestParam(required = false) Long id){
		Response response = new Response();
		
		if(!CommonValidators.validLong(id)) {
			response = new ErrorResponse<String>();
			response.setResponseCode(HttpStatus.BAD_REQUEST.value());
			response.setResponseMessage("Please provide a valid transaction!");
		}else {
			try {
				TransactionDTO trn = this.weightTransactionService.getById(id);
				if(CommonValidators.isValidObject(trn)) {
					response = new BaseResponse<TransactionDTO>();
					response.setResponseCode(HttpStatus.OK.value());
					response.setResponseMessage("Getting transaction detail success.");
					((BaseResponse<TransactionDTO>) response).setData(trn);
				}else {
					response = new ErrorResponse<String>();
					response.setResponseCode(HttpStatus.NOT_FOUND.value());
					response.setResponseMessage("No transaction found!");
				}
			}catch (Exception e) {
				logger.error("Error on getting transaction detail: {}", ExceptionUtils.getStackTrace(e));
				response = new ErrorResponse<String>();
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setResponseMessage(ExceptionUtils.getMessage(e));
			}
		}
		
		return ResponseUtil.send(response);
	}

}
