package org.flexitech.projects.embedded.truckscale.services.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.counter.CounterSettingCategoryDAO;
import org.flexitech.projects.embedded.truckscale.dao.counter.MasterCounterSettingDAO;
import org.flexitech.projects.embedded.truckscale.dto.setting.CounterSettingCategoryDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.MasterCounterSettingDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.MasterCounterSettingListDTO;
import org.flexitech.projects.embedded.truckscale.entities.setting.CounterSettingCategory;
import org.flexitech.projects.embedded.truckscale.entities.setting.MasterCounterSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;

@Service
@Transactional
public class MasterCounterSettingServiceImpl implements MasterCounterSettingService {

	@Autowired
	private MasterCounterSettingDAO masterCounterSettingDAO;

	@Autowired
	private CounterSettingCategoryDAO categoryDAO;

	@Override
	public MasterCounterSettingListDTO getSettingList(Integer status) {
		MasterCounterSettingListDTO dto = new MasterCounterSettingListDTO();
		List<MasterCounterSetting> settings = this.masterCounterSettingDAO.getAllByStatus(status);
		if (CommonValidators.validList(settings)) {
			List<MasterCounterSettingDTO> dtos = settings.stream().map(MasterCounterSettingDTO::new)
					.collect(Collectors.toList());
			dto.setSettings(dtos);
		}
		return dto;
	}

	@Override
	public MasterCounterSettingListDTO updateSetting(MasterCounterSettingListDTO dto) throws Exception {
		// Validate the input DTO
		if (!CommonValidators.isValidObject(dto)) {
			throw new Exception("Reference object is null!");
		}
		if (!CommonValidators.validList(dto.getSettings())) {
			throw new Exception("Setting list is empty!");
		}

		List<MasterCounterSetting> settings = this.masterCounterSettingDAO.getAllByStatus(null);
		List<MasterCounterSettingDTO> dtos = dto.getSettings();

		List<MasterCounterSetting> updatedSettings = new ArrayList<MasterCounterSetting>();

		for (MasterCounterSettingDTO update : dtos) {
			if (CommonValidators.validLong(update.getId())) {
				settings.stream().filter(setting -> setting.getId().equals(update.getId())).findFirst()
						.ifPresent(setting -> {
							if (!Objects.equal(update.getSequence(), setting.getSequence())
									|| !Objects.equal(update.getStatus(), setting.getStatus())
									|| !CommonValidators.isValidObject(setting.getCategory())
									|| (CommonValidators.isValidObject(setting.getCategory())) 
											&& !Objects.equal(update.getCategoryId(), setting.getCategory().getId())) {

								setting.setSequence(update.getSequence());
								setting.setStatus(update.getStatus());
								setting.setUpdatedTime(new Date());
								
								CounterSettingCategory cat = this.categoryDAO.get(update.getCategoryId());
								setting.setCategory(cat);

								updatedSettings.add(setting);
							}
						});
			}
		}

		for (MasterCounterSetting updated : updatedSettings) {
			this.masterCounterSettingDAO.update(updated);
		}

		return new MasterCounterSettingListDTO(
				settings.stream().map(MasterCounterSettingDTO::new).collect(Collectors.toList()));
	}

	@Override
	public List<CounterSettingCategoryDTO> getAllCategory(Integer status) {
		List<CounterSettingCategory> categories = this.categoryDAO.getAllByStatus(status);
		if (CommonValidators.validList(categories)) {
			return categories.stream().map(CounterSettingCategoryDTO::new).toList();
		}
		return Collections.emptyList();
	}

}
