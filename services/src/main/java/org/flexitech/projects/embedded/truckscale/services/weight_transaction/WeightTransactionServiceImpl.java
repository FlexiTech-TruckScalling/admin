package org.flexitech.projects.embedded.truckscale.services.weight_transaction;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerTypeDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerVehicleDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.DriverDAO;
import org.flexitech.projects.embedded.truckscale.dao.products.GoodDAO;
import org.flexitech.projects.embedded.truckscale.dao.products.ProductDAO;
import org.flexitech.projects.embedded.truckscale.dao.setting.WeightUnitDAO;
import org.flexitech.projects.embedded.truckscale.dao.transaction.TransactionDAO;
import org.flexitech.projects.embedded.truckscale.dao.user.UserDAO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.request.transactions.WeightTransactionRequest;
import org.flexitech.projects.embedded.truckscale.dto.response.counter.CounterSettingResponse;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionPreloadDataResponse;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionResponse;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerTypes;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerVehicles;
import org.flexitech.projects.embedded.truckscale.entities.customers.Customers;
import org.flexitech.projects.embedded.truckscale.entities.customers.Drivers;
import org.flexitech.projects.embedded.truckscale.entities.product.Goods;
import org.flexitech.projects.embedded.truckscale.entities.product.Products;
import org.flexitech.projects.embedded.truckscale.entities.setting.WeightUnit;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;
import org.flexitech.projects.embedded.truckscale.services.counter.CounterSettingService;
import org.flexitech.projects.embedded.truckscale.services.customers.CustomerTypeService;
import org.flexitech.projects.embedded.truckscale.services.products.GoodService;
import org.flexitech.projects.embedded.truckscale.services.setting.WeightUnitService;
import org.flexitech.projects.embedded.truckscale.services.shift.UserShiftService;
import org.flexitech.projects.embedded.truckscale.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WeightTransactionServiceImpl implements WeightTransactionService {

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
	
	@Override
	public WeightTransactionPreloadDataResponse getWeightTransactionPreloadData(Long userId) {
		WeightTransactionPreloadDataResponse data = new WeightTransactionPreloadDataResponse();

		data.setGoods(this.goodService.getAllGoods(ActiveStatus.ACTIVE.getCode()));

		UserDTO user = this.userService.getUserById(userId);

		UserShiftDTO activeShift = this.shiftService.getCurrentActiveShift(userId);

		if (CommonValidators.isValidObject(activeShift)) {
			data.setUserShift(activeShift);
		}

		if (CommonValidators.isValidObject(user)) {
			if (CommonValidators.validLong(user.getCounterId())) {
				CounterDTO counter = (this.counterSettingService.getCounterSettingByCounterId(user.getCounterId()));
				if (CommonValidators.isValidObject(counter)) {
					CounterSettingResponse settingResponse = new CounterSettingResponse();
					settingResponse.setCounter(counter);
					settingResponse.setUnits(this.weightUnitService.getAllWeightUnit(ActiveStatus.ACTIVE.getCode()));
					settingResponse.setBounds(InOutBounds.getAll());
					settingResponse.setCustomerTypes(this.customerTypeService.getAllCustomerTypes(ActiveStatus.ACTIVE.getCode()));
					data.setCounterSetting(settingResponse);
				}
			}
		}

		return data;
	}

	@Override
	public WeightTransactionResponse manageWeightTransaction(WeightTransactionRequest request)  throws Exception{
		if(!CommonValidators.isValidObject(request)) {
			throw new IllegalArgumentException("Request cannot be empty!");
		}
		
		Transaction transaction = null;
		
		if(CommonValidators.validLong(request.getId())) {
			transaction = this.transactionDAO.get(request.getId());
			transaction.setUpdatedTime(new Date());
		}else {
			transaction = new Transaction();
			transaction.setCreatedTime(new Date());
		}
		
		if(CommonValidators.validLong(request.getCustomerId())) {
			Customers c = this.customerDAO.get(request.getCustomerId());
			transaction.setCustomer(c);
		}
		
		if(CommonValidators.validLong(request.getCustomerTypeId())) {
			CustomerTypes t = this.customerTypeDAO.get(request.getCustomerTypeId());
			transaction.setCustomerType(t);
		}
		
		transaction.setContainerNumber(request.getContainerNumber());
		transaction.setContainerSize(request.getContainerSize());
		
		transaction.setRegisterVehicleStatus(request.getSaveNewVehicle());
		
		CustomerVehicles vehicle = this.customerVehicleDAO.getVehicleByPrefixAndNumber(request.getVehiclePrefix(), request.getVehicleNumber());
		if(vehicle == null) {
			if(CommonValidators.validInteger(request.getSaveNewVehicle())) {
				CustomerVehicles v = new CustomerVehicles();
				v.setPrefix(request.getVehiclePrefix());
				v.setNumber(request.getVehicleNumber());
				v.setWeight(new BigDecimal(0));
				Customers c = this.customerDAO.get(request.getCustomerId());
				v.setCustomer(c);
				v.setStatus(ActiveStatus.ACTIVE.getCode());
				v.setCreatedTime(new Date());
				Drivers d = this.driverDAO.getDriverByName(request.getDriverName());
				
				if(CommonValidators.isValidObject(d)) {
					v.setDriver(d);
				}else {
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
		}else {
			transaction.setVehicle(vehicle);
		}
		
		transaction.setDriverName(request.getDriverName());
		
		if(CommonValidators.validLong(request.getGoodId())) {
			Goods g = this.goodDAO.get(request.getGoodId());
			transaction.setGoods(g);
		}
		
		if(CommonValidators.validLong(request.getProductId())) {
			Products p = this.productDAO.get(request.getProductId());
			transaction.setProduct(p);
		}
		
		transaction.setQty(new BigDecimal(request.getQuantity()));
		
		if(CommonValidators.validLong(request.getQuantityUnitId())) {
			WeightUnit unit = this.weightUnitDAO.get(request.getQuantityUnitId());
			transaction.setQuantityUnit(unit);
		}
		
		transaction.setCost(new BigDecimal(request.getCost()));
		
		transaction.setAllowedWeight(request.getAllowedWeight());
		
		transaction.setOverWeight(request.getOverWeight());
		
		transaction.setWeight(request.getWeight());
		
		if(CommonValidators.validLong(request.getWeightUnitId())) {
			WeightUnit unit = this.weightUnitDAO.get(request.getWeightUnitId());
			transaction.setWeightUnit(unit);
		}
		
		transaction.setInOutStatus(request.getInOutStatus());
		
		transaction.setCargoStatus(request.getCargoStatus());
		
		if(CommonValidators.isValidObject(request.getInTime())) {
			transaction.setInTime(request.getInTime());
		}
		
		if(CommonValidators.isValidObject(request.getOutTime())) {
			transaction.setOutTime(request.getOutTime());
		}
		
		transaction.setSessionCode(request.getSessionId());
		
		if(CommonValidators.validLong(request.getUserId())) {
			Users user = this.userDAO.get(request.getUserId());
			transaction.setUser(user);
		}
		
		transaction.setStatus(ActiveStatus.ACTIVE.getCode());
		
		this.transactionDAO.saveOrUpdate(transaction);
		
		return new WeightTransactionResponse(new TransactionDTO(transaction));
	}

}
