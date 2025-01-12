package org.flexitech.projects.embedded.truckscale.dao.products;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.product.Goods;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class GoodDAOImpl extends CommonDAOImpl<Goods, Long> implements GoodDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Goods> getAllGoods(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		
		return c.list();
	}

}
