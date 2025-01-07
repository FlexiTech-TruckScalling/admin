package org.flexitech.projects.embedded.truckscale.dao.menu;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.menu.Menu;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class MenuDAOImpl extends CommonDAOImpl<Menu, Long> implements MenuDAO {

	@Override
	public Menu getByCode(String code) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("code", code));
		c.setMaxResults(1);
		return (Menu) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> getAllByStatus(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		c.addOrder(Order.asc("id"));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> getParentMenu() {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("parentStatus", ActiveStatus.ACTIVE.getCode()));
		c.addOrder(Order.asc("id"));
		return c.list();
	}

}
