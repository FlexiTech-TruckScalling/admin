package org.flexitech.projects.embedded.truckscale.entities.transaction;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerTypes;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerVehicles;
import org.flexitech.projects.embedded.truckscale.entities.customers.Customers;
import org.flexitech.projects.embedded.truckscale.entities.product.Goods;
import org.flexitech.projects.embedded.truckscale.entities.product.Products;
import org.flexitech.projects.embedded.truckscale.entities.setting.WeightUnit;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.TRANSACTION_TABLE)
public class Transaction extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4721049850512750699L;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customers customer;
	
	@ManyToOne
	@JoinColumn(name = "customer_type_id")
	private CustomerTypes customerType;
	
	@ManyToOne
	@JoinColumn(name = "good_id", nullable = false)
	private Goods goods;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Products product;
	
	@Column(name = "container_number")
	private String containerNumber;
	
	@Column(name = "container_size")
	private Integer containerSize;
	
	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	private CustomerVehicles vehicle;
	
	@Column(name = "register_vehicle_status")
	private Integer registerVehicleStatus;
	
	@Column(name = "driver_name")
	private String driverName;
	
	private BigDecimal qty;
	
	@ManyToOne
	@JoinColumn(name = "quantity_unit")
	private WeightUnit quantityUnit;
	
	@ManyToOne
	@JoinColumn(name = "weight_unit_id")
	private WeightUnit weightUnit;
	
	private BigDecimal cost;
	
	@Column(name = "allow_weight")
	private Double allowedWeight;
	
	@Column(name = "over_weight")
	private Double overWeight;
	
	@Column(name = "cargo_status")
	private Integer cargoStatus;
	
	private Double weight;
	
	@Column(name = "in_out_status")
	private Integer inOutStatus;
	
	@Column(name = "vehicle_photo_one")
	private String vehiclePhotoOne;
	
	@Column(name = "vehicle_photo_two")
	private String vehiclePhotoTwo;

	@Column(name = "in_time")
	private Date inTime;
	
	@Column(name = "out_time")
	private Date outTime;
	
	@Column(name = "transaction_status")
	private Integer transactionStatus;
	
	@Column(name = "session_code")
	private String sessionCode;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	
	@Column(name = "transaction_code")
	private String transactionCode;
	
}
