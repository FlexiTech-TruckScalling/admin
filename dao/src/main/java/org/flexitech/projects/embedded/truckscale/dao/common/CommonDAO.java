package org.flexitech.projects.embedded.truckscale.dao.common;

import java.io.Serializable;
import java.util.List;

import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public interface CommonDAO<T extends BaseEntity, ID extends Serializable> {
	Session getCurrentSession() throws HibernateException;
	
	List<T> getAll() throws HibernateException;

	public T get(ID id) throws HibernateException;

	Long saveWithId(T entity) throws HibernateException;

	void save(T entity) throws HibernateException;

	void saveOrUpdate(T entity) throws HibernateException;

	void update(T entity) throws HibernateException;

	void merge(T entity) throws HibernateException;

	void deleteById(long uniqueId) throws HibernateException;

	void delete(T entity) throws HibernateException;
}
