package org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction;

import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeightTransactionResponse {
	private TransactionDTO transaction;
}
