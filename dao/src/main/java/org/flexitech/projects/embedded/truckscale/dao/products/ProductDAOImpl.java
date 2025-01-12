package org.flexitech.projects.embedded.truckscale.dao.products;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.product.Products;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDAOImpl extends CommonDAOImpl<Products, Long> implements ProductDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Products> getAllProducts(Integer status) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		if(CommonValidators.validInteger(status)) {
			c.add(Restrictions.eq("status", status));
		}
		return c.list();
	}

}
