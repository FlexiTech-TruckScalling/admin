package org.flexitech.projects.embedded.truckscale.dto.menu;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuAccessListDTO {
	private List<String> accessTreeList = new ArrayList<String>();

	private Long roleId;
}
