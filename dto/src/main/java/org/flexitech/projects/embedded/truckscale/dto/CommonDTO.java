package org.flexitech.projects.embedded.truckscale.dto;

import java.util.Date;

import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.flexitech.projects.embedded.truckscale.util.DateUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommonDTO {
	private Long id;
	
	private Integer status;
	
	private String statusDesc;
	
	private Date createdTime;
	
	private String createdTimeDesc;
	
	private Date updatedTime;
	
	private String updatedTimeDesc;
	
	protected void setField(BaseEntity e) {
		this.id = e.getId();
		this.status = e.getStatus();
		this.statusDesc = ActiveStatus.getDescByCode(status);
		this.createdTime = e.getCreatedTime();
		if(CommonValidators.isValidObject(e.getCreatedTime())) {
			this.createdTimeDesc = DateUtils.dateToString(createdTime, CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT);
		}
		this.updatedTime = e.getUpdatedTime();
		if(CommonValidators.isValidObject(e.getUpdatedTime())) {
			this.updatedTimeDesc = DateUtils.dateToString(updatedTime, CommonDateFormats.STANDARD_12_HOUR_DATE_MINUTE_FORMAT);
		}
	}
}
