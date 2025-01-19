package org.flexitech.projects.embedded.truckscale.dao.products;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.product.Products;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Products> getAllProductsByGoodId(Long goodId, Integer status) {
		SQLQuery query = getCurrentSession().createSQLQuery(prepareSQLForProductsByGoodId(goodId, status));
		query.addEntity(daoType);
		query.setParameter("goodId", goodId);
		if(CommonValidators.validInteger(status)) {
			query.setParameter("status", status);
		}
		return query.list();
	}
	
	private String prepareSQLForProductsByGoodId(Long goodId, Integer status) {
		StringBuilder b = new StringBuilder();
		b.append("SELECT p.* FROM products p ")
		.append("JOIN products_goods pg ON p.id = pg.product_id ")
		.append("WHERE pg.goods_id = :goodId ");
		if(CommonValidators.validInteger(status)) {
			b.append("AND p.Status = :status");
		}
		return b.toString();
	}

}
