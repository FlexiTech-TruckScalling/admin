package org.flexitech.projects.embedded.truckscale.services.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.flexitech.projects.embedded.truckscale.dao.shift.UserShiftDAO;
import org.flexitech.projects.embedded.truckscale.dao.transaction.TransactionDAO;
import org.flexitech.projects.embedded.truckscale.dto.dashboard.DashboardStatsDTO;
import org.flexitech.projects.embedded.truckscale.dto.dashboard.DashboardSummaryDTO;
import org.flexitech.projects.embedded.truckscale.dto.dashboard.RecentTransactionDTO;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;
import org.flexitech.projects.embedded.truckscale.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private TransactionDAO transactionDAO;

	@Autowired
	private UserShiftDAO userShiftDAO;

	@Override
	public DashboardSummaryDTO getDashboardSummary(Date fromDate, Date toDate) {
		DashboardSummaryDTO dto = new DashboardSummaryDTO();

		Date rangeStart = fromDate != null ? startOfDay(fromDate) : startOfDay(new Date());
		Date rangeEnd = toDate != null ? endOfDay(toDate) : endOfDay(new Date());

		DashboardStatsDTO stats = transactionDAO.getDashboardStats(rangeStart, rangeEnd);
		dto.setTodayTransactionCount(stats.getTransactionCount() == null ? 0 : stats.getTransactionCount().intValue());
		dto.setTodayTotalWeight(stats.getTotalWeight() == null ? "" : CommonUtil.formatNumber(stats.getTotalWeight()));
		dto.setTodayTotalRevenue(stats.getTotalRevenue() == null ? "0" : CommonUtil.formatNumber(stats.getTotalRevenue()));

		dto.setVehiclesInYard(transactionDAO.countVehiclesInYard());
		dto.setActiveShiftsCount(userShiftDAO.countActiveShifts());
		dto.setHourlyTransactionCounts(transactionDAO.getHourlyTransactionCounts(rangeStart, rangeEnd));

		List<Transaction> transactions = transactionDAO.getRecentTransactions(10);

		List<RecentTransactionDTO> recentTransactions = transactions.stream().map(tx -> new RecentTransactionDTO(tx))
				.collect(Collectors.toList());

		dto.setRecentTransactions(recentTransactions);
		dto.setTopCustomers(transactionDAO.getTopCustomersByDateRange(rangeStart, rangeEnd, 5));

		return dto;
	}

	private Date startOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private Date endOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
}