package org.flexitech.projects.embedded.truckscale.dto.transaction;

import java.math.BigDecimal;
import java.util.Date;

import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.TransactionStatus;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerTypeDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerVehicleDTO;
import org.flexitech.projects.embedded.truckscale.dto.payment_type.PaymentTypeDTO;
import org.flexitech.projects.embedded.truckscale.dto.products.GoodDTO;
import org.flexitech.projects.embedded.truckscale.dto.products.ProductDTO;
import org.flexitech.projects.embedded.truckscale.dto.reports.transaction.TransactionReportSummaryDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.WeightUnitDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;
import org.flexitech.projects.embedded.truckscale.util.CommonUtil;
import org.flexitech.projects.embedded.truckscale.util.DateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO extends CommonDTO {

	private CustomerDTO customerDTO;
	private CustomerTypeDTO customerTypeDTO;
	private GoodDTO goodDTO;
	private ProductDTO productDTO;
	private String containerNumber;
	private Integer containerSize;
	private CustomerVehicleDTO vehicleDTO;
	private Integer registerVehicleStatus;
	private String driverName;
	private Integer quantity;
	private WeightUnitDTO quantityUnitDTO;
	private WeightUnitDTO weightUnitDTO;
	private BigDecimal cost;
	private Integer allowedWeight;
	private Integer overWeight;
	private Integer cargoStatus;
	private Integer weight;
	private String weightDesc;
	private Integer cargoWeight;
	private String cargoWeightDesc;
	private Integer inOutStatus;
	private String vehiclePhotoOne;
	private String vehiclePhotoTwo;
	private Date inTime;
	private String inTimeStr;
	private Date outTime;
	private String outTimeStr;
	private Integer transactionStatus;
	private String transactionStatusDesc;
	private String sessionCode;
	private UserDTO userDTO;
	private String transactionCode;
	private Integer transactionType;
	
	private PaymentTypeDTO paymentTypeDTO;
	
	@JsonIgnore
	private TransactionReportSummaryDTO summary; 

	public TransactionDTO(Transaction t) {
		if (!CommonValidators.isValidObject(t))
			return;

		// Map fields from Transaction entity to TransactionDTO
		this.customerDTO = new CustomerDTO(t.getCustomer());
		this.customerTypeDTO = new CustomerTypeDTO(t.getCustomerType());
		this.goodDTO = new GoodDTO(t.getGoods());
		this.productDTO = t.getProduct() != null ? new ProductDTO(t.getProduct()) : null;
		this.containerNumber = t.getContainerNumber();
		this.containerSize = t.getContainerSize();
		this.vehicleDTO = t.getVehicle() != null ? new CustomerVehicleDTO(t.getVehicle()) : null;
		this.registerVehicleStatus = t.getRegisterVehicleStatus();
		this.driverName = t.getDriverName();
		this.quantity = CommonUtil.convertNumberToInteger(t.getQty());
		this.quantityUnitDTO = t.getQuantityUnit() != null ? new WeightUnitDTO(t.getQuantityUnit()) : null;
		this.weightUnitDTO = t.getWeightUnit() != null ? new WeightUnitDTO(t.getWeightUnit()) : null;
		this.cost = t.getCost();
		this.allowedWeight = CommonUtil.convertNumberToInteger(t.getAllowedWeight());
		this.overWeight = CommonUtil.convertNumberToInteger(t.getOverWeight());
		this.cargoStatus = t.getCargoStatus();
		this.weight = CommonUtil.convertNumberToInteger(t.getWeight());
		if(CommonValidators.isValidObject(weight) && CommonValidators.isValidObject(t.getWeightUnit())) {
			this.weightDesc = CommonUtil.getWeightDesc(t.getWeight(), t.getWeightUnit().getPerKgValue(), t.getWeightUnit().getName());
		}
		this.cargoWeight = CommonUtil.convertNumberToInteger(t.getCargoWeight());
		if(CommonValidators.isValidObject(cargoWeight) && CommonValidators.isValidObject(t.getWeightUnit())) {
			this.cargoWeightDesc = CommonUtil.getWeightDesc(t.getCargoWeight(), t.getWeightUnit().getPerKgValue(), t.getWeightUnit().getName());
		}
		this.inOutStatus = t.getInOutStatus();
		this.vehiclePhotoOne = t.getVehiclePhotoOne();
		this.vehiclePhotoTwo = t.getVehiclePhotoTwo();
		this.inTime = t.getInTime();
		this.outTime = t.getOutTime();
		if (CommonValidators.isValidObject(inTime)) {
			this.inTimeStr = DateUtils.dateToString(inTime, CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT);
		}
		if (CommonValidators.isValidObject(outTime)) {
			this.outTimeStr = DateUtils.dateToString(outTime, CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT);
		}
		this.transactionStatus = t.getTransactionStatus();
		this.transactionStatusDesc = CommonValidators.validInteger(transactionStatus) ? TransactionStatus.getDescByCode(transactionStatus): "Unknown";
		this.sessionCode = t.getSessionCode();
		this.userDTO = t.getUser() != null ? new UserDTO(t.getUser()) : null;
		this.transactionCode = t.getTransactionCode();
		this.transactionType = t.getTransactionType();
		if(CommonValidators.isValidObject(t.getPaymentType())) {
			this.paymentTypeDTO = new PaymentTypeDTO(t.getPaymentType());
		}
		setField(t);
	}
}