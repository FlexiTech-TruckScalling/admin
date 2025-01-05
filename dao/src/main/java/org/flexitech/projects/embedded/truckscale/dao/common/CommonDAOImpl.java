package org.flexitech.projects.embedded.truckscale.dao.common;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.transaction.Transactional;

import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
public class CommonDAOImpl<T extends BaseEntity, ID extends Serializable> implements CommonDAO<T, ID> {

	@Autowired
	private SessionFactory sessionFactory;
	
	protected Class<T> daoType;
	
	@SuppressWarnings("unchecked")
	public CommonDAOImpl()
	{
		daoType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
	}

	@Override
	public Session getCurrentSession() 
	{
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public T get(ID id)
	{
		return (T)getCurrentSession().get(daoType, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {
        return getCurrentSession().createCriteria(daoType).list();
    }

	@Override
	public Long saveWithId(T entity) 
	{
		return  (Long)getCurrentSession().save(entity);
	}
	
	@Override
	public void save(T entity) 
	{
		getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(T entity) 
	{
		getCurrentSession().saveOrUpdate(entity);
	}

	@Override
	public void update(T entity) 
	{
		getCurrentSession().update(entity);	
	}
	
	@Override
	public void merge(T entity)
	{
		getCurrentSession().merge(entity);
	}

	@Override
	public void deleteById(long uniqueId) 
	{		
		getCurrentSession().delete(uniqueId);
	}

	@Override
	public void delete(T entity) 
	{
		getCurrentSession().delete(entity);		
	}

}
