package org.flexitech.projects.embedded.truckscale.dao.shift;

import java.util.Date;
import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.ShiftStatus;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.dto.user_shift.UserShiftSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShift;
import org.flexitech.projects.embedded.truckscale.util.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UserShiftDAOImpl extends CommonDAOImpl<UserShift, Long> implements UserShiftDAO {

	@Override
	public UserShift getCurrentActiveShift(Long userId) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.createAlias("user", "u");
		c.add(Restrictions.eq("u.id", userId));
		c.add(Restrictions.eq("shiftStatus", ShiftStatus.OPEN.getCode()));
		c.add(Restrictions.isNull("endTime"));
		c.addOrder(Order.desc("startTime"));
		c.setMaxResults(1);
		return (UserShift) c.uniqueResult();
	}

	@Override
	public UserShift getUserShitByCode(String code) {
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("code", code));
		c.setMaxResults(1);
		return (UserShift) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserShift> searchUserShift(UserShiftSearchDTO searchDTO, boolean export) {
		SQLQuery query = getCurrentSession().createSQLQuery(prepareSearchQuery(searchDTO, false, export));
		query.addEntity(daoType);
		this.appendParamentersForSearch(query, searchDTO, export);
		return query.list();
	}
	
	

	private String prepareSearchQuery(UserShiftSearchDTO searchDTO, boolean countOnly, boolean export) {
		StringBuilder b = new StringBuilder();

		if (countOnly) {
			b.append(" SELECT COUNT(DISTINCT c.id) ");
		} else {
			b.append(" SELECT DISTINCT c.* ");
		}

		b.append("From user_shifts c ").append("LEFT OUTER JOIN users u ON u.id = c.user_id ");

		b.append("WHERE 1=1 ");

		if (CommonValidators.validString(searchDTO.getUsername())) {
			b.append("AND u.name LIKE :name ");
		}

		if (CommonValidators.validString(searchDTO.getStartTime())) {
			b.append("AND c.end_time >= :startTime ");
		}

		if (CommonValidators.validString(searchDTO.getEndTime())) {
			b.append("AND c.end_time <= :endTime ");
		}

		if (!countOnly) {
			b.append("ORDER BY created_time desc ");
		}

		return b.toString();
	}
	
	private void appendParamentersForSearch(SQLQuery query,UserShiftSearchDTO searchDTO, boolean export) {
		if (CommonValidators.validString(searchDTO.getUsername())) {
			query.setParameter("name", "%" + searchDTO.getUsername() + "%");
		}

		if (CommonValidators.validString(searchDTO.getStartTime())) {
			Date date = DateUtils.stringToDate(searchDTO.getStartTime() + " " + CommonDateFormats.HOUR_START, CommonDateFormats.STD_YYYY_MM_DD_24);
			System.out.println(date);
			query.setParameter("startTime", date);
		}

		if (CommonValidators.validString(searchDTO.getEndTime())) {
			Date date = DateUtils.stringToDate(searchDTO.getEndTime() + " " + CommonDateFormats.HOUR_END, CommonDateFormats.STD_YYYY_MM_DD_24);
			System.out.println(date);
			query.setParameter("endTime", date);
		}
		if(!export) {
			if (searchDTO.getPageNo() != null && searchDTO.getLimit() != null) {
				int offset = (searchDTO.getPageNo() - 1) * searchDTO.getLimit();
				query.setFirstResult(offset).setMaxResults(searchDTO.getLimit());
			}
		}
	}

	@Override
	public Integer countUserShift(UserShiftSearchDTO searchDTO) {
		SQLQuery query = getCurrentSession().createSQLQuery(prepareSearchQuery(searchDTO, true, true));
		appendParamentersForSearch(query, searchDTO, true);
		query.setMaxResults(1);
		return ((Number) query.uniqueResult()).intValue();
	}

}
