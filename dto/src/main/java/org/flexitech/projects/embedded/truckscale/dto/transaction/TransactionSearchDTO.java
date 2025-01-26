package org.flexitech.projects.embedded.truckscale.dto.transaction;

import org.flexitech.projects.embedded.truckscale.dto.CommonSearchDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionSearchDTO extends CommonSearchDTO{

	private String customerName;
	private Long goodId;
	private Long productId;
	private String vehiclePrefix;
	private String vehicleNumber;
	private String driverName;
	private Double weight;
	private Integer inOutStatus;
	private Integer overWeightStatus;
	private String sessionCode;
	private Long userId;
	private String createdFromDate;
	private String createdToDate;
}
