package org.flexitech.projects.embedded.truckscale.dao.transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.MathSign;
import org.flexitech.projects.embedded.truckscale.common.enums.TransactionStatus;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.dto.shift.CurrentShiftSummaryDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;
import org.flexitech.projects.embedded.truckscale.util.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDAOImpl extends CommonDAOImpl<Transaction, Long> implements TransactionDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> searchTransactions(TransactionSearchDTO searchDTO, boolean export) {
		SQLQuery query = getCurrentSession().createSQLQuery(prepareSQLForSearch(searchDTO, false, export));
		query.addEntity(daoType);

		setQueryParameters(query, searchDTO);

		if (!export && searchDTO.getPageNo() != null && searchDTO.getLimit() != null) {
			int offset = (searchDTO.getPageNo() - 1) * searchDTO.getLimit();
			query.setFirstResult(offset).setMaxResults(searchDTO.getLimit());
		}

		return query.list();
	}

	@Override
	public Integer countTransactions(TransactionSearchDTO searchDTO) {
		SQLQuery query = getCurrentSession().createSQLQuery(prepareSQLForSearch(searchDTO, true, false));
		setQueryParameters(query, searchDTO);
		query.setMaxResults(1);
		return ((Number) query.uniqueResult()).intValue();
	}

	private String prepareSQLForSearch(TransactionSearchDTO searchDTO, boolean countOnly, boolean export) {
	    StringBuilder builder = new StringBuilder();
	    List<String> joins = new ArrayList<>();
	    List<String> conditions = new ArrayList<>();

	    // SELECT clause
	    if (countOnly) {
	        builder.append("SELECT COUNT(DISTINCT t.id) ");
	    } else {
	        builder.append("SELECT t.* ");
	    }

	    // FROM clause
	    builder.append("FROM transactions t ");
	    joins.add("LEFT JOIN customers c ON t.customer_id = c.id");

	    if (CommonValidators.validString(searchDTO.getVehiclePrefix()) || CommonValidators.validString(searchDTO.getVehicleNumber())) {
	        joins.add("LEFT JOIN customer_vehicles cv ON t.vehicle_id = cv.id");
	    }

	    // Append joins
	    if (!joins.isEmpty()) {
	        builder.append(String.join(" ", joins)).append(" ");
	    }

	    conditions.add("t.transaction_status != " + TransactionStatus.CANCEL.getCode() + " ");

	    if(CommonValidators.validString(searchDTO.getTransctionCode())) {
	    	conditions.add("t.transaction_code = :transactionCode");
	    }
	    
	    // WHERE clause
	    if (CommonValidators.validString(searchDTO.getCustomerName())) {
	        conditions.add("c.name LIKE :customerName");
	    }
	    if (CommonValidators.validLong(searchDTO.getGoodId())) {
	        conditions.add("t.good_id = :goodId");
	    }
	    if (CommonValidators.validLong(searchDTO.getProductId())) {
	        conditions.add("t.product_id = :productId");
	    }
	    if (CommonValidators.validString(searchDTO.getVehiclePrefix())) {
	        conditions.add("cv.prefix LIKE :vehiclePrefix");
	    }
	    if (CommonValidators.validString(searchDTO.getVehicleNumber())) {
	        conditions.add("cv.number LIKE :vehicleNumber");
	    }
	    if (CommonValidators.validString(searchDTO.getDriverName())) {
	        conditions.add("t.driver_name LIKE :driverName");
	    }
	    if (CommonValidators.validDouble(searchDTO.getWeight())) {
	        conditions.add("t.weight = :weight");
	    }
	    if (CommonValidators.validDouble(searchDTO.getFromWeight())
	            && CommonValidators.validInteger(searchDTO.getMathSign())) {
	        conditions.add("t.weight " + MathSign.getDescByCode(searchDTO.getMathSign()) + " :fromWeight ");
	    }
	    if (CommonValidators.validInteger(searchDTO.getInOutStatus())) {
	        conditions.add("t.in_out_status = :inOutStatus");
	    }
	    if (CommonValidators.validInteger(searchDTO.getOverWeightStatus())) {
	        if (searchDTO.getOverWeightStatus() == 1) {
	            conditions.add("t.over_weight > 0");
	        } else {
	            conditions.add("t.over_weight <= 0");
	        }
	    }
	    if (CommonValidators.validString(searchDTO.getSessionCode())) {
	        conditions.add("t.session_code = :sessionCode");
	    }
	    if (CommonValidators.validLong(searchDTO.getUserId())) {
	        conditions.add("t.user_id = :userId");
	    }
	    if (CommonValidators.validString(searchDTO.getCreatedFromDate())) {
	        conditions.add("t.created_time >= :createdFromDate");
	    }
	    if (CommonValidators.validString(searchDTO.getCreatedToDate())) {
	        conditions.add("t.created_time <= :createdToDate");
	    }
	    
	    if(CommonValidators.validLong(searchDTO.getPaymentTypeId())) {
	    	conditions.add("t.payment_type_id = :paymentTypeId");
	    }

	    if (!conditions.isEmpty()) {
	        builder.append("WHERE ").append(String.join(" AND ", conditions)).append(" ");
	    }

	    if (!countOnly && !export) {
	        builder.append("ORDER BY t.in_time DESC ");
	    }

	    return builder.toString();
	}

	private void setQueryParameters(SQLQuery query, TransactionSearchDTO searchDTO) {
		if (CommonValidators.validString(searchDTO.getCustomerName())) {
			query.setParameter("customerName", "%" + searchDTO.getCustomerName() + "%");
		}
		if (CommonValidators.validLong(searchDTO.getGoodId())) {
			query.setParameter("goodId", searchDTO.getGoodId());
		}
		if (CommonValidators.validLong(searchDTO.getProductId())) {
			query.setParameter("productId", searchDTO.getProductId());
		}
		if (CommonValidators.validString(searchDTO.getVehiclePrefix())) {
			query.setParameter("vehiclePrefix", searchDTO.getVehiclePrefix() + "%");
		}
		if (CommonValidators.validString(searchDTO.getVehicleNumber())) {
			query.setParameter("vehicleNumber", "%" + searchDTO.getVehicleNumber() + "%");
		}
		if (CommonValidators.validString(searchDTO.getDriverName())) {
			query.setParameter("driverName", "%" + searchDTO.getDriverName() + "%");
		}
		if (CommonValidators.validDouble(searchDTO.getWeight())) {
			query.setParameter("weight", searchDTO.getWeight());
		}
		if (CommonValidators.validInteger(searchDTO.getInOutStatus())) {
			query.setParameter("inOutStatus", searchDTO.getInOutStatus());
		}
		if (CommonValidators.validString(searchDTO.getSessionCode())) {
			query.setParameter("sessionCode", searchDTO.getSessionCode());
		}
		if (CommonValidators.validLong(searchDTO.getUserId() )) {
			query.setParameter("userId", searchDTO.getUserId());
		}
		if (CommonValidators.validString(searchDTO.getCreatedFromDate())) {
			Date date = DateUtils.stringToDate(searchDTO.getCreatedFromDate() + " " + CommonDateFormats.HOUR_START, CommonDateFormats.STD_YYYY_MM_DD_24);

			query.setParameter("createdFromDate", date);
		}
		if(CommonValidators.validDouble(searchDTO.getFromWeight())
				&& CommonValidators.validInteger(searchDTO.getMathSign())) {
			query.setParameter("fromWeight", searchDTO.getFromWeight());
		}
		if (CommonValidators.validString(searchDTO.getCreatedToDate())) {
			Date date = DateUtils.stringToDate(searchDTO.getCreatedToDate() + " " + CommonDateFormats.HOUR_END, CommonDateFormats.STD_YYYY_MM_DD_24);
			query.setParameter("createdToDate", date);
		}
		
		
		
		if(CommonValidators.validString(searchDTO.getTransctionCode())) {
			query.setParameter("transactionCode", searchDTO.getTransctionCode());
		}
		if(CommonValidators.validLong(searchDTO.getPaymentTypeId())) {
			query.setParameter("paymentTypeId", searchDTO.getPaymentTypeId());
		}
	}

	@Override
	public CurrentShiftSummaryDTO getCurrentShiftSummary(Long userId, String sessionCode) {
		SQLQuery query = getCurrentSession().createSQLQuery(prepareSQLForShiftSummary())
				.addScalar("totalTrn",StandardBasicTypes.INTEGER)
				.addScalar("totalAmount", StandardBasicTypes.BIG_DECIMAL);
		
		query.setParameter("userId", userId);
		query.setParameter("sessionCode", sessionCode);
		
		query.setResultTransformer(Transformers.aliasToBean(CurrentShiftSummaryDTO.class));
		query.setMaxResults(1);
		return (CurrentShiftSummaryDTO) query.uniqueResult();
	}
	
	private String prepareSQLForShiftSummary() {
		StringBuilder b = new StringBuilder();
		
		b.append("SELECT COUNT(DISTINCT id) as totalTrn, ")
		.append("SUM(COALESCE(cost, 0)) as totalAmount ")
		.append("FROM transactions ")
		.append("WHERE user_id = :userId ")
		.append("AND session_code = :sessionCode ");
		
		return b.toString();
	}

	@Override
	public boolean isCodeAlreadyUsed(String code) {
		if(!CommonValidators.validString(code)) return true;
		Criteria c = getCurrentSession().createCriteria(daoType);
		c.add(Restrictions.eq("transactionCode", code));
		c.setMaxResults(1);
		return c.uniqueResult() != null;
	}

}
