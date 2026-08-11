package org.flexitech.projects.embedded.truckscale.services.dashboard;

import java.util.Date;

import org.flexitech.projects.embedded.truckscale.dto.dashboard.DashboardSummaryDTO;

public interface DashboardService {
	DashboardSummaryDTO getDashboardSummary(Date fromDate, Date toDate);
}
