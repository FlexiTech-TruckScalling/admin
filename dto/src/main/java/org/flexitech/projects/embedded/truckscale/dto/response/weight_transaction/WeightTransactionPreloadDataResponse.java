package org.flexitech.projects.embedded.truckscale.dto.response.weight_transaction;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.payment_type.PaymentTypeDTO;
import org.flexitech.projects.embedded.truckscale.dto.products.GoodDTO;
import org.flexitech.projects.embedded.truckscale.dto.response.counter.CounterSettingResponse;
import org.flexitech.projects.embedded.truckscale.dto.setting.SystemSettingListDTO;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WeightTransactionPreloadDataResponse {
	private List<GoodDTO> goods;
	private CounterSettingResponse counterSetting;
	private UserShiftDTO userShift;
	private SystemSettingListDTO systemSettings;
	private List<PaymentTypeDTO> paymentTypes;
}
