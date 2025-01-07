package org.flexitech.projects.embedded.truckscale.dto.menu;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuAccessTreeDTO {

	private Long id;

	private String text;

	private String shotName;

	private String url;

	private boolean checked;

	private boolean hasChildren;

	private List<MenuAccessTreeDTO> children = new ArrayList<MenuAccessTreeDTO>();

	private Long parentId;
	
}
