package org.flexitech.projects.embedded.truckscale.dto.transaction;

import java.math.BigDecimal;
import java.util.Date;

import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerTypeDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerVehicleDTO;
import org.flexitech.projects.embedded.truckscale.dto.products.GoodDTO;
import org.flexitech.projects.embedded.truckscale.dto.products.ProductDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.WeightUnitDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;
import org.flexitech.projects.embedded.truckscale.util.DateUtils;

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
	private BigDecimal quantity;
	private WeightUnitDTO quantityUnitDTO;
	private WeightUnitDTO weightUnitDTO;
	private BigDecimal cost;
	private Double allowedWeight;
	private Double overWeight;
	private Integer cargoStatus;
	private Double weight;
	private Integer inOutStatus;
	private String vehiclePhotoOne;
	private String vehiclePhotoTwo;
	private Date inTime;
	private String inTimeStr;
	private Date outTime;
	private String outTimeStr;
	private Integer transactionStatus;
	private String sessionCode;
	private UserDTO userDTO;
	private String transactionCode;

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
		this.quantity = t.getQty();
		this.quantityUnitDTO = t.getQuantityUnit() != null ? new WeightUnitDTO(t.getQuantityUnit()) : null;
		this.weightUnitDTO = t.getWeightUnit() != null ? new WeightUnitDTO(t.getWeightUnit()) : null;
		this.cost = t.getCost();
		this.allowedWeight = t.getAllowedWeight();
		this.overWeight = t.getOverWeight();
		this.cargoStatus = t.getCargoStatus();
		this.weight = t.getWeight();
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
		this.sessionCode = t.getSessionCode();
		this.userDTO = t.getUser() != null ? new UserDTO(t.getUser()) : null;
		this.transactionCode = t.getTransactionCode();
		setField(t);
	}
}