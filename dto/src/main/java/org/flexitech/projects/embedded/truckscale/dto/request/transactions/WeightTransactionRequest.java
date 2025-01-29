package org.flexitech.projects.embedded.truckscale.dto.request.transactions;

import java.util.Date;

import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WeightTransactionRequest {
	
	@JsonProperty("id")
	@JsonInclude(Include.NON_NULL)
	private Long id;
    
    @JsonProperty("customer_id")
    private Long customerId;
    
    @JsonProperty("customer_type_id")
    private long customerTypeId;
    
    @JsonProperty("container_number")
    private String containerNumber = "";
    
    @JsonProperty("container_size")
    private int containerSize;
    
    @JsonProperty("vehicle_prefix")
    private String vehiclePrefix = "";
    
    @JsonProperty("vehicle_number")
    private String vehicleNumber = "";
    
    @JsonProperty("vehicle_weight")
    private Double vehicleWeight;
    
    @JsonProperty("save_new_vehicle_status")
    private int saveNewVehicle;
    
    @JsonProperty("driver_name")
    private String driverName = "";
    
    @JsonProperty("good_id")
    private Long goodId;
    
    @JsonProperty("product_id")
    private Long productId;
    
    @JsonProperty("quantity")
    private Double quantity;
    
    @JsonProperty("quantity_unit_id")
    private Long quantityUnitId;
    
    @JsonProperty("cost")
    private Double cost;
    
    @JsonProperty("allowed_weight")
    private Double allowedWeight;
    
    @JsonProperty("over_weight")
    private Double overWeight;
    
    @JsonProperty("weight")
    private Double weight;
    
    @JsonProperty("weight_unit_id")
    private Long weightUnitId;
    
    @JsonProperty("in_out_status")
    private int inOutStatus;
    
    @JsonProperty("cargo_status")
    private int cargoStatus;
    
    @JsonProperty("session_id")
    private String sessionId = "";
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("in_time")
    @DateTimeFormat(pattern = CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT)
    @JsonFormat(pattern = CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT)
    private Date inTime;
    
    @JsonProperty("out_time")
    @DateTimeFormat(pattern = CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT)
    @JsonFormat(pattern = CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT)
    private Date outTime;
    
    @JsonProperty("transaction_code")
    private String transactionCode;
}

