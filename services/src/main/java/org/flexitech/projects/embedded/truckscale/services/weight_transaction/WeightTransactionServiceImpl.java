package org.flexitech.projects.embedded.truckscale.services.weight_transaction;

import javax.transaction.Transactional;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.counter.CounterSettingResponse;
import org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction.WeightTransactionPreloadDataResponse;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.flexitech.projects.embedded.truckscale.services.counter.CounterSettingService;
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
					data.setCounterSetting(settingResponse);
				}
			}
		}

		return data;
	}

}
