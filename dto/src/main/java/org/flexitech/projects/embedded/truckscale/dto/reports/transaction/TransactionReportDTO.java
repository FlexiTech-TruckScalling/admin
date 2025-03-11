package org.flexitech.projects.embedded.truckscale.dto.reports.transaction;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionDTO;
import org.flexitech.projects.embedded.truckscale.util.CommonUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionReportDTO {
	private String transactionCode = "";
	private String customerName = "";
	private String good = "";
	private String product = "";
	private String vehicle = "";
	private String driver = "";
	private String weight = "";
	private String cargoWeight = "";
	private String inOutStatus = "";
	private String transactionStatus = "";
	private String inTime = "";
	private String outTime = "";
	
	private String weightUnit = "KG";

	public TransactionReportDTO(TransactionDTO dto) {
	    if (!CommonValidators.isValidObject(dto))
	        return;
	    
	    // Apply analyseReportContent to all string values
	    this.transactionCode = CommonUtil.analyseReportContent(dto.getTransactionCode());
	    
	    if (CommonValidators.isValidObject(dto.getCustomerDTO())) {
	        this.customerName = CommonUtil.analyseReportContent(dto.getCustomerDTO().getName());
	    }
	    
	    if (CommonValidators.isValidObject(dto.getGoodDTO())) {
	        this.good = CommonUtil.analyseReportContent(dto.getGoodDTO().getName());
	    }
	    
	    if (CommonValidators.isValidObject(dto.getProductDTO())) {
	        this.product = CommonUtil.analyseReportContent(dto.getProductDTO().getName());
	    }
	    
	    if (CommonValidators.isValidObject(dto.getVehicleDTO())) {
	        String prefix = CommonUtil.analyseReportContent(dto.getVehicleDTO().getPrefix());
	        String number = CommonUtil.analyseReportContent(dto.getVehicleDTO().getNumber());
	        this.vehicle = prefix + "/" + number;
	    }
	    
	    this.driver = CommonUtil.analyseReportContent(dto.getDriverName());
	    
	    if (CommonValidators.isValidObject(dto.getWeightUnitDTO())) {
	        this.weightUnit = CommonUtil.analyseReportContent(dto.getWeightUnitDTO().getName());
	    }
	    
	    // Process formatted weight values
	    this.weight = CommonUtil.analyseReportContent(dto.getWeightDesc());
	    
	    this.cargoWeight = CommonUtil.analyseReportContent(dto.getCargoWeightDesc());
	    
	    // Process status descriptions
	    this.inOutStatus = CommonUtil.analyseReportContent(InOutBounds.getDescByCode(dto.getInOutStatus()));
	    this.transactionStatus = CommonUtil.analyseReportContent(dto.getTransactionStatusDesc());
	    
	    // Process time strings
	    this.inTime = CommonUtil.analyseReportContent(dto.getInTimeStr());
	    this.outTime = CommonUtil.analyseReportContent(dto.getOutTimeStr());
	}
}
