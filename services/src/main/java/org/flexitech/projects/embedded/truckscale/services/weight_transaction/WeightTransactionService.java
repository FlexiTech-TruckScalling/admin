package org.flexitech.projects.embedded.truckscale.services.weight_transaction;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.request.transactions.WeightTransactionRequest;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionPreloadDataResponse;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionResponse;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;

public interface WeightTransactionService {
	WeightTransactionPreloadDataResponse getWeightTransactionPreloadData(Long userId);
	WeightTransactionResponse manageWeightTransaction(WeightTransactionRequest request) throws Exception;
	Long cancelTransaction(Long transactionId) throws Exception;
	List<TransactionDTO> searchTransactions(TransactionSearchDTO searchDTO, boolean export);
	Integer countTotalTransaction(TransactionSearchDTO searchDTO);
	TransactionDTO getById(Long id);
	WeightTransactionResponse syncWeightTransaction(WeightTransactionRequest request) throws Exception;
}
