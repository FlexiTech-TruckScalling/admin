package org.flexitech.projects.embedded.truckscale.services.reports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Service
public class ReportServiceImpl implements ReportService{
	private static final Logger logger = LogManager.getLogger(ReportServiceImpl.class);

	/**
	 * Generate PDF report.
	 */
	@Override
	public void generateReportPdf(List<?> dataList, ByteArrayOutputStream baos, String reportPath, String fileName,
			HashMap<String, Object> parameters) {
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(dataList);

		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(reportPath));
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, datasource);
			if (jasperReport.getDefaultStyle() == null) {
	            JRStyle defaultStyle = new JRBaseStyle();
	            defaultStyle.setFontName("Zawgyi-One");
	            defaultStyle.setPdfFontName("Zawgyi-One");
	        }
			JRPdfExporter exporterPdf = new JRPdfExporter();
			exporterPdf.setExporterInput(new SimpleExporterInput(print));
			exporterPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

			SimplePdfReportConfiguration config = new SimplePdfReportConfiguration();
			config.setSizePageToContent(true);
			config.setForceLineBreakPolicy(false);

			exporterPdf.setConfiguration(config);
			exporterPdf.exportReport();
		} catch (JRException e) {
			logger.error("Exception while exporting PDF report: " + ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Generate Excel report.
	 */
	@Override
	public void generateReportExcel(List<?> dataList, ByteArrayOutputStream baos, String reportPath, String fileName,
			HashMap<String, Object> parameters) {
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(dataList);

		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(reportPath));
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, datasource);

			JRXlsxExporter exporterXLS = new JRXlsxExporter();
			exporterXLS.setExporterInput(new SimpleExporterInput(print));
			exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

			SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
			config.setOnePagePerSheet(false);
			config.setDetectCellType(true);
			config.setCollapseRowSpan(false);
			config.setRemoveEmptySpaceBetweenRows(false);
			config.setRemoveEmptySpaceBetweenColumns(false);
			config.setWhitePageBackground(false);
			config.setWrapText(true);
			
			exporterXLS.setConfiguration(config);
			exporterXLS.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
			logger.error("Exception while exporting Excel report: " + ExceptionUtils.getStackTrace(e));
		}
	}

}
