package org.flexitech.projects.embedded.truckscale.dao.menu;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.menu.MenuRoleAccess;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRoleAccessDAOImpl extends CommonDAOImpl<MenuRoleAccess, Long> implements MenuRoleAccessDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<MenuRoleAccess> getParentMenusByRoleId(Long roleId) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.createAlias("role", "r");
		c.createAlias("menu", "m");
		c.add(Restrictions.eq("r.id", roleId));
		c.add(Restrictions.eq("m.parentStatus", ActiveStatus.ACTIVE.getCode()));
		c.addOrder(Order.asc("m.order"));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MenuRoleAccess> getChildMenuByParentMenuIdAndRoleId(String parentMenuCode, Long roleId) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.createAlias("role", "r");
		c.createAlias("menu", "m");
		c.add(Restrictions.eq("r.id", roleId));
		c.add(Restrictions.eq("m.parentMenuCode", parentMenuCode));
		c.add(Restrictions.ne("m.parentStatus", ActiveStatus.ACTIVE.getCode()));
		c.addOrder(Order.asc("m.order"));
		return c.list();
	}

}
