package org.flexitech.projects.embedded.truckscale.dto;

import org.flexitech.projects.embedded.truckscale.common.CommonConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonSearchDTO {
	private Integer pageNo;
	private Integer limit = CommonConstants.ROW_PER_PAGE;
	private Integer totalPage;
	private Integer totalRecords;
	private Integer pageCount;
}
