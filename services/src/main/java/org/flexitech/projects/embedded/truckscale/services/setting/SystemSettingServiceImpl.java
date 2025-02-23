package org.flexitech.projects.embedded.truckscale.services.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.setting.SystemSettingDAO;
import org.flexitech.projects.embedded.truckscale.dto.setting.SystemSettingDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.SystemSettingListDTO;
import org.flexitech.projects.embedded.truckscale.entities.setting.SystemSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingServiceImpl implements SystemSettingService {

	@Autowired
	private SystemSettingDAO systemSettingDAO;

	@Override
	public SystemSettingListDTO getAllSystemSettings(Integer status) {
		List<SystemSetting> settings = this.systemSettingDAO.getAllByStatus(status);
		if (CommonValidators.validList(settings)) {
			return new SystemSettingListDTO(settings.stream().map(SystemSettingDTO::new).toList());
		}
		return new SystemSettingListDTO(Collections.emptyList());
	}

	@Override
	public SystemSettingListDTO manageSystemSettings(SystemSettingListDTO settingListDTO) {
	    List<SystemSettingDTO> changesList = settingListDTO.getSettings();
	    List<SystemSetting> existingList = this.systemSettingDAO.getAllByStatus(null);

	    Map<String, SystemSetting> existingMap = existingList.stream()
	        .collect(Collectors.toMap(SystemSetting::getCode, Function.identity()));

	    List<SystemSetting> toUpdate = new ArrayList<>();

	    for (SystemSettingDTO dto : changesList) {
	        SystemSetting existingSetting = existingMap.get(dto.getCode());
	        
	        if (existingSetting != null) {
	            boolean isValueChanged = !Objects.equals(existingSetting.getValue(), dto.getValue());
	            boolean isStatusChanged = !Objects.equals(existingSetting.getStatus(), dto.getStatus());
	            boolean isSequenceChanged = !Objects.equals(existingSetting.getSequence(), dto.getSequence());

	            if (isValueChanged || isStatusChanged || isSequenceChanged) {
	                // Apply the changes
	                existingSetting.setValue(dto.getValue());
	                existingSetting.setStatus(dto.getStatus());
	                existingSetting.setSequence(dto.getSequence());

	                toUpdate.add(existingSetting);
	            }
	        }
	    }

	    if (!toUpdate.isEmpty()) {
	        for(SystemSetting s: toUpdate) {
	        	this.systemSettingDAO.update(s);
	        }
	    }

	    return settingListDTO;
	}

	@Override
	public SystemSettingDTO getSettingByCode(String code) {
		SystemSetting setting = this.systemSettingDAO.getSettingByCode(code);
		if(setting != null)
			return new SystemSettingDTO(setting);
		return null;
	}



}
