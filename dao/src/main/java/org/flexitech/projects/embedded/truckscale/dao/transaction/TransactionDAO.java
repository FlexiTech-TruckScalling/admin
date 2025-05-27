package org.flexitech.projects.embedded.truckscale.dao.transaction;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.dto.reports.transaction.TransactionReportSummaryDTO;
import org.flexitech.projects.embedded.truckscale.dto.shift.CurrentShiftSummaryDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;

public interface TransactionDAO extends CommonDAO<Transaction, Long>{
	List<Transaction> searchTransactions(TransactionSearchDTO searchDTO, boolean export);
	Integer countTransactions(TransactionSearchDTO searchDTO);
	
	CurrentShiftSummaryDTO getCurrentShiftSummary(Long userId, String sessionCode);
	
	boolean isCodeAlreadyUsed(String code);
	
	TransactionReportSummaryDTO getTransactionSummary(TransactionSearchDTO searchDTO);
}
