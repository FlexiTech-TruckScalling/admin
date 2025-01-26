package org.flexitech.projects.embedded.truckscale.dao.transaction;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;

public interface TransactionDAO extends CommonDAO<Transaction, Long>{
	List<Transaction> searchTransactions(TransactionSearchDTO searchDTO, boolean export);
	Integer countTransactions(TransactionSearchDTO searchDTO);
}
