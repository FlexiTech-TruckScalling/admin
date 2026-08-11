package org.flexitech.projects.embedded.truckscale.dto.dashboard;

import org.flexitech.projects.embedded.truckscale.common.CommonConstants;
import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.common.enums.TransactionStatus;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;
import org.flexitech.projects.embedded.truckscale.util.CommonUtil;
import org.flexitech.projects.embedded.truckscale.util.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentTransactionDTO {
    private String ticketNo;
    private String vehicleNumber;
    private String customerName;
    private String transactionDate;
    private String weight;
    private String cargoWeight;
    private Integer status;
    private String statusDesc;
    
    public RecentTransactionDTO(Transaction t) {
    	if(t == null) return;
    	
    	this.ticketNo = t.getTransactionCode();
    	this.vehicleNumber = t.getVehicle() != null ?
    			t.getVehicle().getPrefix() + "/" + t.getVehicle().getNumber() : "-";
    	
    	this.customerName = t.getCustomer() !=null ? t.getCustomer().getName() : "-";
    	
    	this.transactionDate = InOutBounds.IN.getCode().equals(t.getInOutStatus())?
    			DateUtils.dateToString(t.getInTime(), CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT):
    				InOutBounds.OUT.getCode().equals(t.getInOutStatus()) ? 
    						DateUtils.dateToString(t.getOutTime(), CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT):"-";
    	
    	this.weight = t.getWeight() != null ? CommonUtil.formatNumber(t.getWeight()): "0";
    	this.cargoWeight = t.getCargoWeight() != null ? CommonUtil.formatNumber(t.getCargoWeight()) : "0";
    	this.status = t.getStatus();
    	this.statusDesc = TransactionStatus.getDescByCode(status);
    }
    
}