package org.flexitech.projects.embedded.truckscale.dto.menu;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.menu.Menu;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuDTO extends CommonDTO{
	private String code;
	private String name;
	private String url;
	private Integer parentStatus;
	private String parentMenuCode;
	private String description;
	private Integer order;
	private String icon;
	public MenuDTO(Menu menu) {
		if(CommonValidators.isValidObject(menu)) {
			this.code = menu.getCode();
			this.name = menu.getName();
			this.url = menu.getUrl();
			this.parentMenuCode = menu.getUrl();
			this.parentStatus = menu.getParentStatus();
			this.description = menu.getDescription();
			this.order = menu.getOrder();
			this.icon = menu.getIcon();
			setField(menu);
		}
	}
}
