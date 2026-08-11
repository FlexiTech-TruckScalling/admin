package org.flexitech.projects.embedded.truckscale.admin.controllers.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.dto.dashboard.DashboardSummaryDTO;
import org.flexitech.projects.embedded.truckscale.services.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashboardController {
	
	private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private DashboardService dashboardService;

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "Flexitech | Truck Scale Dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        return "dashboard";
    }

    @GetMapping("/api/dashboard/summary")
    @ResponseBody
    public ResponseEntity<?> getDashboardSummary(
            @RequestParam(value = "fromDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(value = "toDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

        if (fromDate == null || toDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            fromDate = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            toDate = cal.getTime();
        }

        try {
            DashboardSummaryDTO response = dashboardService.getDashboardSummary(fromDate, toDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to load dashboard summary", e);
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("error", "Unable to load dashboard data.");
            return ResponseEntity.status(500).body(errorBody);
        }
    }
}
