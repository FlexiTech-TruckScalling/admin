package org.flexitech.projects.embedded.truckscale.services.weight_transaction;

import org.flexitech.projects.embedded.truckscale.dto.request.transactions.WeightTransactionRequest;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionPreloadDataResponse;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionResponse;

public interface WeightTransactionService {
	WeightTransactionPreloadDataResponse getWeightTransactionPreloadData(Long userId);
	WeightTransactionResponse manageWeightTransaction(WeightTransactionRequest request) throws Exception;
}
