package org.flexitech.projects.embedded.truckscale.dto.setting;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterCounterSettingListDTO{
	private List<MasterCounterSettingDTO> settings = new ArrayList<MasterCounterSettingDTO>();
}
