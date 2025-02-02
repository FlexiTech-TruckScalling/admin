package org.flexitech.projects.embedded.truckscale.services.counter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.counter.CounterDAO;
import org.flexitech.projects.embedded.truckscale.dao.counter.CounterSettingCategoryDAO;
import org.flexitech.projects.embedded.truckscale.dao.counter.CounterSettingDAO;
import org.flexitech.projects.embedded.truckscale.dao.counter.MasterCounterSettingDAO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterSettingDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.CounterSettingCategoryDTO;
import org.flexitech.projects.embedded.truckscale.dto.setting.MasterCounterSettingDTO;
import org.flexitech.projects.embedded.truckscale.entities.counters.CounterSetting;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.flexitech.projects.embedded.truckscale.entities.setting.CounterSettingCategory;
import org.flexitech.projects.embedded.truckscale.entities.setting.MasterCounterSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CounterSettingServiceImpl implements CounterSettingService {

	@Autowired
	MasterCounterSettingDAO masterCounterSettingDAO;

	@Autowired
	CounterSettingDAO counterSettingDAO;

	@Autowired
	CounterSettingCategoryDAO counterSettingCategoryDAO;

	@Autowired
	CounterDAO counterDAO;

	private final Logger logger = LogManager.getLogger(getClass());

	@Override
	public List<MasterCounterSettingDTO> getMasterCounterSettings() {
		List<MasterCounterSetting> masterSettings = masterCounterSettingDAO
				.getAllByStatus(ActiveStatus.ACTIVE.getCode());
		if (CommonValidators.validList(masterSettings)) {
			return masterSettings.stream().map(MasterCounterSettingDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<MasterCounterSettingDTO>();
	}

	@Override
	public CounterDTO getCounterSettingByCounterId(Long counterId) {

		CounterDTO counterDTO = null;

		if (!CommonValidators.isValidObject(counterId))
			counterDTO = new CounterDTO();
		else {
			Counters counter = this.counterDAO.get(counterId);

			counterDTO = new CounterDTO(counter);
		}

		List<CounterSettingCategory> categories = this.counterSettingCategoryDAO.getAllByStatus(null);

		List<CounterSettingCategoryDTO> categoryDtos = new ArrayList<CounterSettingCategoryDTO>();

		for (CounterSettingCategory cat : categories) {
			CounterSettingCategoryDTO catDto = new CounterSettingCategoryDTO(cat);

			List<CounterSettingDTO> result = new ArrayList<CounterSettingDTO>();
			List<MasterCounterSetting> masterSettings = this.masterCounterSettingDAO.getAllByCategory(cat.getId(),
					ActiveStatus.ACTIVE.getCode());
			for (MasterCounterSetting masterSetting : masterSettings) {
				CounterSettingDTO dto = new CounterSettingDTO();
				CounterSetting setting = this.counterSettingDAO.getCounterSettingByCounterAndMasterSettingId(counterId,
						masterSetting.getId());
				dto.setMasterCounterSettingDTO(new MasterCounterSettingDTO(masterSetting));
				if (!CommonValidators.isValidObject(setting)) {
					dto.setValue(null);
				} else {
					dto = new CounterSettingDTO(setting);
					dto.setValue(setting.getValue());
				}
				result.add(dto);
			}
			catDto.setCounterSettingDTOs(result);
			categoryDtos.add(catDto);
		}

		counterDTO.setSettingCategoryDTOs(categoryDtos);
		return counterDTO;
	}

	@Override
	public void manageCounterSetting(CounterDTO counterDTO) {
		if (!CommonValidators.validLong(counterDTO.getId()))
			return;
		Counters counter = this.counterDAO.get(counterDTO.getId());

		if (CommonValidators.validList(counterDTO.getSettingCategoryDTOs())) {
			for (CounterSettingCategoryDTO category : counterDTO.getSettingCategoryDTOs()) {

				if (CommonValidators.validList(category.getCounterSettingDTOs())) {
					for (CounterSettingDTO settingDTO : category.getCounterSettingDTOs()) {
						CounterSetting setting = new CounterSetting();
						if (CommonValidators.validLong(settingDTO.getId())) {
							setting = counterSettingDAO.get(settingDTO.getId());
							setting.setUpdatedTime(new Date());
						} else {
							setting.setCreatedTime(new Date());
						}
						MasterCounterSetting master = null;
						if (CommonValidators.validLong(settingDTO.getMasterCounterSettingDTO().getId())) {
							master = masterCounterSettingDAO.get(settingDTO.getMasterCounterSettingDTO().getId());
							setting.setMasterCounterSetting(master);
							setting.setCounter(counter);
							setting.setValue(settingDTO.getValue());

							this.counterSettingDAO.saveOrUpdate(setting);
						} else {
							logger.warn("Skipped becasue setting does not contain master counter from frontend!");
							continue;
						}
					}
				}

			}
		}

	}

	@Override
	public CounterDTO getCounterSettingWithMasterSettingByCounterId(Long counterId) {
		CounterDTO counterDTO = null;

		if (!CommonValidators.isValidObject(counterId))
			counterDTO = new CounterDTO();
		else {
			Counters counter = this.counterDAO.get(counterId);

			counterDTO = new CounterDTO(counter);
		}

		List<CounterSettingDTO> result = new ArrayList<CounterSettingDTO>();
		List<MasterCounterSetting> masterSettings = this.masterCounterSettingDAO
				.getAllByStatus(ActiveStatus.ACTIVE.getCode());
		
		for (MasterCounterSetting masterSetting : masterSettings) {
			CounterSettingDTO dto = new CounterSettingDTO();
			CounterSetting setting = this.counterSettingDAO.getCounterSettingByCounterAndMasterSettingId(counterId,
					masterSetting.getId());
			dto.setMasterCounterSettingDTO(new MasterCounterSettingDTO(masterSetting));
			if (!CommonValidators.isValidObject(setting)) {
				dto.setValue(null);
			} else {
				dto = new CounterSettingDTO(setting);
				dto.setValue(setting.getValue());
			}
			result.add(dto);
		}

		counterDTO.setCounterSettingDTOs(result);
		return counterDTO;
	}

}
