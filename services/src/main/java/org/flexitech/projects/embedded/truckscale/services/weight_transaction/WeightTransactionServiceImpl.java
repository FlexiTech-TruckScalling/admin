package org.flexitech.projects.embedded.truckscale.services.weight_transaction;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.CargoStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.common.enums.TransactionStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.TransactionType;
import org.flexitech.projects.embedded.truckscale.dao.company.CompanyDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerTypeDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerVehicleDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.DriverDAO;
import org.flexitech.projects.embedded.truckscale.dao.payment_type.PaymentTypeDAO;
import org.flexitech.projects.embedded.truckscale.dao.products.GoodDAO;
import org.flexitech.projects.embedded.truckscale.dao.products.ProductDAO;
import org.flexitech.projects.embedded.truckscale.dao.setting.WeightUnitDAO;
import org.flexitech.projects.embedded.truckscale.dao.transaction.TransactionDAO;
import org.flexitech.projects.embedded.truckscale.dao.unit.QuantityUnitDAO;
import org.flexitech.projects.embedded.truckscale.dao.user.UserDAO;
import org.flexitech.projects.embedded.truckscale.dto.company.CompanyDTO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.transactions.WeightTransactionRequest;
import org.flexitech.projects.embedded.truckscale.dto.response.counter.CounterSettingResponse;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionPreloadDataResponse;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionResponse;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.company.Company;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerTypes;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerVehicles;
import org.flexitech.projects.embedded.truckscale.entities.customers.Customers;
import org.flexitech.projects.embedded.truckscale.entities.customers.Drivers;
import org.flexitech.projects.embedded.truckscale.entities.payment_type.PaymentType;
import org.flexitech.projects.embedded.truckscale.entities.product.Goods;
import org.flexitech.projects.embedded.truckscale.entities.product.Products;
import org.flexitech.projects.embedded.truckscale.entities.setting.WeightUnit;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;
import org.flexitech.projects.embedded.truckscale.entities.unit.QuantityUnit;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.flexitech.projects.embedded.truckscale.services.counter.CounterSettingService;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerTypeService;
import org.flexitech.projects.embedded.truckscale.services.payment_type.PaymentTypeService;
import org.flexitech.projects.embedded.truckscale.services.products.GoodService;
import org.flexitech.projects.embedded.truckscale.services.setting.SystemSettingService;
import org.flexitech.projects.embedded.truckscale.services.setting.WeightUnitService;
import org.flexitech.projects.embedded.truckscale.services.shift.UserShiftService;
import org.flexitech.projects.embedded.truckscale.services.unit.QuantityUnitService;
import org.flexitech.projects.embedded.truckscale.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WeightTransactionServiceImpl implements WeightTransactionService {

	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	UserService userService;

	@Autowired
	GoodService goodService;

	@Autowired
	WeightUnitService weightUnitService;

	@Autowired
	CounterSettingService counterSettingService;

	@Autowired
	UserShiftService shiftService;

	@Autowired
	CustomerTypeService customerTypeService;

	@Autowired
	TransactionDAO transactionDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	CustomerTypeDAO customerTypeDAO;

	@Autowired
	CustomerVehicleDAO customerVehicleDAO;

	@Autowired
	DriverDAO driverDAO;

	@Autowired
	GoodDAO goodDAO;

	@Autowired
	ProductDAO productDAO;

	@Autowired
	WeightUnitDAO weightUnitDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	PaymentTypeDAO paymentTypeDAO;

	@Autowired
	SystemSettingService systemSettingService;
	
	@Autowired
	PaymentTypeService paymentTypeService;
	
	@Autowired
	CompanyDAO companyDAO;
	
	@Autowired
	QuantityUnitService quantityUnitService;
	
	@Autowired
	QuantityUnitDAO quantityUnitDAO;

	@Override
	public WeightTransactionPreloadDataResponse getWeightTransactionPreloadData(Long userId) {
		WeightTransactionPreloadDataResponse data = new WeightTransactionPreloadDataResponse();

		data.setSystemSettings(this.systemSettingService.getAllSystemSettings(ActiveStatus.ACTIVE.getCode()));

		data.setGoods(this.goodService.getAllGoods(ActiveStatus.ACTIVE.getCode()));

		data.setPaymentTypes(this.paymentTypeService.getAll(ActiveStatus.ACTIVE.getCode()));
		
		List<Company> companies = this.companyDAO.getAll();
		data.setCompany(new CompanyDTO(companies.get(0)));
		
		UserDTO user = this.userService.getUserById(userId);

		UserShiftDTO activeShift = this.shiftService.getCurrentActiveShift(userId);

		if (CommonValidators.isValidObject(activeShift)) {
			data.setUserShift(activeShift);
		}

		if (CommonValidators.isValidObject(user)) {
			if (CommonValidators.validLong(user.getCounterId())) {
				CounterDTO counter = (this.counterSettingService
						.getCounterSettingWithMasterSettingByCounterId(user.getCounterId()));
				if (CommonValidators.isValidObject(counter)) {
					CounterSettingResponse settingResponse = new CounterSettingResponse();
					settingResponse.setCounter(counter);
					settingResponse.setUnits(this.weightUnitService.getAllWeightUnit(ActiveStatus.ACTIVE.getCode()));
					settingResponse.setBounds(InOutBounds.getAll(false));
					settingResponse.setCustomerTypes(
							this.customerTypeService.getAllCustomerTypes(ActiveStatus.ACTIVE.getCode()));
					settingResponse.setQuantityUnits(this.quantityUnitService.getQuantityUnitsByStatus(ActiveStatus.ACTIVE));
					data.setCounterSetting(settingResponse);
				}
			}
		}

		return data;
	}

	@Override
	public WeightTransactionResponse manageWeightTransaction(WeightTransactionRequest request) throws Exception {
		if (!CommonValidators.isValidObject(request)) {
			throw new IllegalArgumentException("Request cannot be empty!");
		}

		Transaction transaction = null;

		if (CommonValidators.validLong(request.getId())) {
			try {
				transaction = this.transactionDAO.get(request.getId());
				if (CommonValidators.isValidObject(transaction)) {
					transaction.setUpdatedTime(new Date());
				} else {
					logger.warn("Transaction is not exit! continue with new transaction.");
					transaction = new Transaction();
					transaction.setCreatedTime(new Date());
				}
			} catch (Exception e) {
				logger.error("Failed to get existing transaction: {}", ExceptionUtils.getStackTrace(e));
				transaction = new Transaction();
				transaction.setCreatedTime(new Date());
			}
		} else {
			transaction = new Transaction();
			transaction.setCreatedTime(new Date());

			// transaction code
			/*
			 * int attempts = 0; final int maxAttempts = 5; boolean success = false;
			 * 
			 * while (attempts < maxAttempts && !success) { String code =
			 * TokenUtil.generateTransactionCode(); if
			 * (!this.transactionDAO.isCodeAlreadyUsed(code)) {
			 * transaction.setTransactionCode(code); success = true; } attempts++; }
			 * 
			 * if (!success) throw new IllegalStateException(
			 * "Failed to generate a unique transaction code after " + maxAttempts +
			 * " attempts.");
			 */
		}

		if (CommonValidators.validLong(request.getCustomerId())) {
			Customers c = this.customerDAO.get(request.getCustomerId());
			transaction.setCustomer(c);
		}

		if (CommonValidators.validLong(request.getCustomerTypeId())) {
			CustomerTypes t = this.customerTypeDAO.get(request.getCustomerTypeId());
			transaction.setCustomerType(t);
		}

		transaction.setTransactionCode(request.getTransactionCode());
		transaction.setContainerNumber(request.getContainerNumber());
		transaction.setContainerSize(request.getContainerSize());

		transaction.setRegisterVehicleStatus(request.getSaveNewVehicle());

		CustomerVehicles vehicle = this.customerVehicleDAO.getVehicleByPrefixAndNumber(request.getVehiclePrefix(),
				request.getVehicleNumber());
		if (vehicle == null) {
			if (CommonValidators.validInteger(request.getSaveNewVehicle())) {
				CustomerVehicles v = new CustomerVehicles();
				v.setPrefix(request.getVehiclePrefix());
				v.setNumber(request.getVehicleNumber());
				v.setWeight(new BigDecimal(0));
				Customers c = this.customerDAO.get(request.getCustomerId());
				v.setCustomer(c);
				v.setStatus(ActiveStatus.ACTIVE.getCode());
				v.setCreatedTime(new Date());
				Drivers d = this.driverDAO.getDriverByName(request.getDriverName());

				if (CommonValidators.isValidObject(d)) {
					v.setDriver(d);
				} else {
					Drivers driver = new Drivers();
					driver.setName(request.getDriverName());
					driver.setStatus(ActiveStatus.ACTIVE.getCode());
					driver.setCreatedTime(new Date());
					this.driverDAO.save(driver);
					v.setDriver(driver);
				}
				this.customerVehicleDAO.save(v);
				transaction.setVehicle(v);
			}
		} else {
			transaction.setVehicle(vehicle);
		}

		transaction.setDriverName(request.getDriverName());

		if (CommonValidators.validLong(request.getGoodId())) {
			Goods g = this.goodDAO.get(request.getGoodId());
			transaction.setGoods(g);
		}

		if (CommonValidators.validLong(request.getProductId())) {
			Products p = this.productDAO.get(request.getProductId());
			transaction.setProduct(p);
		}

		transaction.setQty(new BigDecimal(request.getQuantity()));

		if (CommonValidators.validLong(request.getQuantityUnitId())) {
			QuantityUnit unit = this.quantityUnitDAO.get(request.getQuantityUnitId());
			transaction.setQuantityUnit(unit);
		}

		if (TransactionType.INOUT.getCode().equals(request.getTransactionType()))
			transaction.setCost(new BigDecimal(request.getCost()));

		transaction.setAllowedWeight(request.getAllowedWeight());

		transaction.setOverWeight(request.getOverWeight());

		if (TransactionType.INOUT.getCode().equals(request.getTransactionType()))
			if (request.getCargoStatus() == CargoStatus.WITH_CARGO.getCode())
				transaction.setCargoWeight(request.getCargoWeight());
			else if (request.getCargoStatus() == CargoStatus.WITHOUT_CARGO.getCode())
				transaction.setWeight(request.getWeight());

		if (CommonValidators.validLong(request.getWeightUnitId())) {
			WeightUnit unit = this.weightUnitDAO.get(request.getWeightUnitId());
			transaction.setWeightUnit(unit);
		}

		transaction.setInOutStatus(request.getInOutStatus());

		transaction.setCargoStatus(request.getCargoStatus());

		if (CommonValidators.isValidObject(request.getInTime())) {
			transaction.setInTime(request.getInTime());
		}

		if (CommonValidators.isValidObject(request.getOutTime())) {
			transaction.setOutTime(request.getOutTime());
		}

		transaction.setSessionCode(request.getSessionId());

		if (CommonValidators.validLong(request.getUserId())) {
			Users user = this.userDAO.get(request.getUserId());
			transaction.setUser(user);
		}

		transaction.setVehiclePhotoOne(request.getVehiclePhotoOne());
		transaction.setVehiclePhotoTwo(request.getVehiclePhotoTwo());

		transaction.setStatus(ActiveStatus.ACTIVE.getCode());
		transaction.setTransactionType(request.getTransactionType());

		if (CommonValidators.validLong(request.getPaymentTypeId())) {
			PaymentType type = this.paymentTypeDAO.get(request.getPaymentTypeId());
			if (CommonValidators.isValidObject(type)) {
				transaction.setPaymentType(type);
			}
		}

		this.transactionDAO.saveOrUpdate(transaction);

		checkForCompletedTransction(transaction);

		return new WeightTransactionResponse(new TransactionDTO(transaction));
	}

	private void checkForCompletedTransction(Transaction transaction) {
		if (!CommonValidators.isValidObject(transaction) && !CommonValidators.validLong(transaction.getId()))
			return;
		if (transaction.getInTime() != null && transaction.getOutTime() != null) {
			// completed
			transaction.setTransactionStatus(TransactionStatus.COMPLETED.getCode());
		} else {
			transaction.setTransactionStatus(TransactionStatus.INCOMPLETED.getCode());
		}
		this.transactionDAO.update(transaction);
	}

	@Override
	public Long cancelTransaction(Long transactionId) throws Exception {
		if (!CommonValidators.validLong(transactionId)) {
			throw new IllegalAccessException("Please provide the transaction to cancel!");
		}
		Transaction transaction = this.transactionDAO.get(transactionId);
		if (!CommonValidators.isValidObject(transaction)) {
			throw new IllegalAccessException("No transaction found.");
		}

		transaction.setTransactionStatus(TransactionStatus.CANCEL.getCode());
		this.transactionDAO.update(transaction);
		return transaction.getId();
	}

	@Override
	public List<TransactionDTO> searchTransactions(TransactionSearchDTO searchDTO, boolean export) {
		List<Transaction> transactions = this.transactionDAO.searchTransactions(searchDTO, export);
		if (CommonValidators.validList(transactions)) {
			return transactions.stream().map(TransactionDTO::new).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public Integer countTotalTransaction(TransactionSearchDTO searchDTO) {
		return this.transactionDAO.countTransactions(searchDTO);
	}

	@Override
	public TransactionDTO getById(Long id) {
		Transaction trn = this.transactionDAO.get(id);
		if (CommonValidators.isValidObject(trn))
			return new TransactionDTO(trn);
		return null;
	}

	@Override
	public WeightTransactionResponse syncWeightTransaction(WeightTransactionRequest request) throws Exception {
		if (!CommonValidators.isValidObject(request)) {
			throw new IllegalArgumentException("Request cannot be empty!");
		}

		Transaction transaction = null;

		if (CommonValidators.validLong(request.getId())) {
			try {
				transaction = this.transactionDAO.get(request.getId());
				if (CommonValidators.isValidObject(transaction)) {
					transaction.setUpdatedTime(new Date());
				} else {
					logger.warn("Transaction is not exit! continue with new transaction.");
					transaction = new Transaction();
					transaction.setCreatedTime(new Date());
				}
			} catch (Exception e) {
				logger.error("Failed to get existing transaction: {}", ExceptionUtils.getStackTrace(e));
				transaction = new Transaction();
				transaction.setCreatedTime(new Date());
			}
		} else {
			transaction = new Transaction();
			transaction.setCreatedTime(new Date());

			// transaction code
			/*
			 * int attempts = 0; final int maxAttempts = 5; boolean success = false;
			 * 
			 * while (attempts < maxAttempts && !success) { String code =
			 * TokenUtil.generateTransactionCode(); if
			 * (!this.transactionDAO.isCodeAlreadyUsed(code)) {
			 * transaction.setTransactionCode(code); success = true; } attempts++; }
			 * 
			 * if (!success) throw new IllegalStateException(
			 * "Failed to generate a unique transaction code after " + maxAttempts +
			 * " attempts.");
			 */
		}

		if (CommonValidators.validLong(request.getCustomerId())) {
			Customers c = this.customerDAO.get(request.getCustomerId());
			transaction.setCustomer(c);
		}

		if (CommonValidators.validLong(request.getCustomerTypeId())) {
			CustomerTypes t = this.customerTypeDAO.get(request.getCustomerTypeId());
			transaction.setCustomerType(t);
		}

		transaction.setTransactionCode(request.getTransactionCode());
		transaction.setContainerNumber(request.getContainerNumber());
		transaction.setContainerSize(request.getContainerSize());

		transaction.setRegisterVehicleStatus(request.getSaveNewVehicle());

		CustomerVehicles vehicle = this.customerVehicleDAO.getVehicleByPrefixAndNumber(request.getVehiclePrefix(),
				request.getVehicleNumber());
		if (vehicle == null) {
			if (CommonValidators.validInteger(request.getSaveNewVehicle())) {
				CustomerVehicles v = new CustomerVehicles();
				v.setPrefix(request.getVehiclePrefix());
				v.setNumber(request.getVehicleNumber());
				v.setWeight(new BigDecimal(0));
				Customers c = this.customerDAO.get(request.getCustomerId());
				v.setCustomer(c);
				v.setStatus(ActiveStatus.ACTIVE.getCode());
				v.setCreatedTime(new Date());
				Drivers d = this.driverDAO.getDriverByName(request.getDriverName());

				if (CommonValidators.isValidObject(d)) {
					v.setDriver(d);
				} else {
					Drivers driver = new Drivers();
					driver.setName(request.getDriverName());
					driver.setStatus(ActiveStatus.ACTIVE.getCode());
					driver.setCreatedTime(new Date());
					this.driverDAO.save(driver);
					v.setDriver(driver);
				}
				this.customerVehicleDAO.save(v);
				transaction.setVehicle(v);
			}
		} else {
			transaction.setVehicle(vehicle);
		}

		transaction.setDriverName(request.getDriverName());

		if (CommonValidators.validLong(request.getGoodId())) {
			Goods g = this.goodDAO.get(request.getGoodId());
			transaction.setGoods(g);
		}

		if (CommonValidators.validLong(request.getProductId())) {
			Products p = this.productDAO.get(request.getProductId());
			transaction.setProduct(p);
		}

		transaction.setQty(new BigDecimal(request.getQuantity()));

		if (CommonValidators.validLong(request.getQuantityUnitId())) {
			QuantityUnit unit = this.quantityUnitDAO.get(request.getQuantityUnitId());
			transaction.setQuantityUnit(unit);
		}

		if (TransactionType.INOUT.getCode().equals(request.getTransactionType()))
			transaction.setCost(new BigDecimal(request.getCost()));

		transaction.setAllowedWeight(request.getAllowedWeight());

		transaction.setOverWeight(request.getOverWeight());
		transaction.setCargoWeight(request.getCargoWeight());
		transaction.setWeight(request.getWeight());
		
		/*
		 * if (TransactionType.INOUT.getCode().equals(request.getTransactionType())) if
		 * (request.getCargoStatus() == CargoStatus.WITH_CARGO.getCode()) else if
		 * (request.getCargoStatus() == CargoStatus.WITHOUT_CARGO.getCode())
		 */

		if (CommonValidators.validLong(request.getWeightUnitId())) {
			WeightUnit unit = this.weightUnitDAO.get(request.getWeightUnitId());
			transaction.setWeightUnit(unit);
		}

		transaction.setInOutStatus(request.getInOutStatus());

		transaction.setCargoStatus(request.getCargoStatus());

		if (CommonValidators.isValidObject(request.getInTime())) {
			transaction.setInTime(request.getInTime());
		}

		if (CommonValidators.isValidObject(request.getOutTime())) {
			transaction.setOutTime(request.getOutTime());
		}

		transaction.setSessionCode(request.getSessionId());

		if (CommonValidators.validLong(request.getUserId())) {
			Users user = this.userDAO.get(request.getUserId());
			transaction.setUser(user);
		}

		transaction.setVehiclePhotoOne(request.getVehiclePhotoOne());
		transaction.setVehiclePhotoTwo(request.getVehiclePhotoTwo());

		transaction.setStatus(ActiveStatus.ACTIVE.getCode());
		transaction.setTransactionType(request.getTransactionType());
		transaction.setTransactionStatus(request.getTransactionSatus());

		if (CommonValidators.validLong(request.getPaymentTypeId())) {
			PaymentType type = this.paymentTypeDAO.get(request.getPaymentTypeId());
			if (CommonValidators.isValidObject(type)) {
				transaction.setPaymentType(type);
			}
		}

		this.transactionDAO.saveOrUpdate(transaction);

		/* checkForCompletedTransction(transaction); */

		return new WeightTransactionResponse(new TransactionDTO(transaction));
	}

}
