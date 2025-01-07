package org.flexitech.projects.embedded.truckscale.dao.user;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.user.UserRoles;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleDAOImpl extends CommonDAOImpl<UserRoles, Long> implements UserRoleDAO{

}
