package org.flexitech.projects.embedded.truckscale.dao.transaction;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.dto.dashboard.CustomerStatDTO;
import org.flexitech.projects.embedded.truckscale.dto.dashboard.DashboardStatsDTO;
import org.flexitech.projects.embedded.truckscale.dto.shift.CurrentShiftSummaryDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;

public interface TransactionDAO extends CommonDAO<Transaction, Long>{
	List<Transaction> searchTransactions(TransactionSearchDTO searchDTO, boolean export);
	Integer countTransactions(TransactionSearchDTO searchDTO);
	
	CurrentShiftSummaryDTO getCurrentShiftSummary(Long userId, String sessionCode);
	
	boolean isCodeAlreadyUsed(String code);
	
	DashboardStatsDTO getDashboardStats(Date fromDate, Date toDate);
    Map<Integer, Long> getHourlyTransactionCounts(Date fromDate, Date toDate);
    List<Transaction> getRecentTransactions(int limit);
    List<CustomerStatDTO> getTopCustomersByDateRange(Date fromDate, Date toDate, int limit);
    long countVehiclesInYard();
}
