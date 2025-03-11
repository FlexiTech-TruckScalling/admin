package org.flexitech.projects.embedded.truckscale.services.reports;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public interface ReportService {
	void generateReportPdf(List<?> dataList, ByteArrayOutputStream baos, String reportPath, String fileName,
			HashMap<String, Object> parameter);

	void generateReportExcel(List<?> dataList, ByteArrayOutputStream baos,
			String reportPath, String fileName, HashMap<String, Object> parameter);
}
