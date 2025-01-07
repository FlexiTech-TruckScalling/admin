package org.flexitech.projects.embedded.truckscale.services.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.user.UserRoleDAO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserRoleDTO;
import org.flexitech.projects.embedded.truckscale.entities.user.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	UserRoleDAO userRoleDAO;
	
	@Override
	public UserRoleDTO getRoleById(Long id) {
		UserRoles role = this.userRoleDAO.get(id);
		if(CommonValidators.isValidObject(role)) {
			return new UserRoleDTO(role);
		}
		logger.error("Role resources not found!");
		return null;
	}

	@Override
	public UserRoleDTO manageRole(UserRoleDTO userRoleDTO) {
		if(!CommonValidators.isValidObject(userRoleDTO)) return null;
		UserRoles role = new UserRoles();
		if(CommonValidators.validLong(userRoleDTO.getId())) {
			role = this.userRoleDAO.get(userRoleDTO.getId());
			if(!CommonValidators.isValidObject(role)) {
				logger.error("Try to update role but resource unavilable!");
				return null;
			}
			role.setUpdatedTime(new Date());
		}else {
			role.setCreatedTime(new Date());
		}
		
		role.setCode(userRoleDTO.getCode());
		role.setName(userRoleDTO.getName());
		role.setDescription(userRoleDTO.getDescription());
		role.setStatus(userRoleDTO.getStatus());
		
		this.userRoleDAO.saveOrUpdate(role);
		
		return new UserRoleDTO(role);
	}

	@Override
	public List<UserRoleDTO> getAllRoles() {
		List<UserRoles> roles = this.userRoleDAO.getAll();
		if(CommonValidators.validList(roles)) {
			return roles.stream().map(UserRoleDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<UserRoleDTO>();
	}

}
