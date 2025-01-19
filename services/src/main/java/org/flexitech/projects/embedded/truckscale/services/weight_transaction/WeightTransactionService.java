package org.flexitech.projects.embedded.truckscale.services.weight_transaction;

import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionPreloadDataResponse;

public interface WeightTransactionService {
	WeightTransactionPreloadDataResponse getWeightTransactionPreloadData(Long userId);
}
