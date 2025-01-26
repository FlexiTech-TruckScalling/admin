package org.flexitech.projects.embedded.truckscale.dao.transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.flexitech.projects.embedded.truckscale.common.CommonDateFormats;
import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;
import org.flexitech.projects.embedded.truckscale.util.DateUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDAOImpl extends CommonDAOImpl<Transaction, Long> implements TransactionDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> searchTransactions(TransactionSearchDTO searchDTO, boolean export) {
		SQLQuery query = getCurrentSession().createSQLQuery(prepareSQLForSearch(searchDTO, false, export));
		query.addEntity(daoType);

		setQueryParameters(query, searchDTO);

		if (searchDTO.getPageNo() != null && searchDTO.getLimit() != null) {
			int offset = (searchDTO.getPageNo() - 1) * searchDTO.getLimit();
			query.setFirstResult(offset).setMaxResults(searchDTO.getLimit());
		}

		return query.list();
	}

	@Override
	public Integer countTransactions(TransactionSearchDTO searchDTO) {
		// TODO Auto-generated method stub
		return null;
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

		if (searchDTO.getVehiclePrefix() != null || searchDTO.getVehicleNumber() != null) {
			joins.add("LEFT JOIN customer_vehicles cv ON t.vehicle_id = cv.id");
		}

		// Append joins
		if (!joins.isEmpty()) {
			builder.append(String.join(" ", joins)).append(" ");
		}

		// WHERE clause
		if (searchDTO.getCustomerName() != null && !searchDTO.getCustomerName().isEmpty()) {
			conditions.add("c.name LIKE :customerName");
		}
		if (searchDTO.getGoodId() != null) {
			conditions.add("t.good_id = :goodId");
		}
		if (searchDTO.getProductId() != null) {
			conditions.add("t.product_id = :productId");
		}
		if (searchDTO.getVehiclePrefix() != null && !searchDTO.getVehiclePrefix().isEmpty()) {
			conditions.add("cv.prefix LIKE :vehiclePrefix");
		}
		if (searchDTO.getVehicleNumber() != null && !searchDTO.getVehicleNumber().isEmpty()) {
			conditions.add("cv.number LIKE :vehicleNumber");
		}
		if (searchDTO.getDriverName() != null && !searchDTO.getDriverName().isEmpty()) {
			conditions.add("t.driver_name LIKE :driverName");
		}
		if (searchDTO.getWeight() != null) {
			conditions.add("t.weight = :weight");
		}
		if (searchDTO.getInOutStatus() != null) {
			conditions.add("t.in_out_status = :inOutStatus");
		}
		if (searchDTO.getOverWeightStatus() != null) {
			if (searchDTO.getOverWeightStatus() == 1) {
				conditions.add("t.over_weight > 0");
			} else {
				conditions.add("t.over_weight <= 0");
			}
		}
		if (searchDTO.getSessionCode() != null && !searchDTO.getSessionCode().isEmpty()) {
			conditions.add("t.session_code = :sessionCode");
		}
		if (searchDTO.getUserId() != null) {
			conditions.add("t.user_id = :userId");
		}
		if (searchDTO.getCreatedFromDate() != null) {
			conditions.add("t.created_time >= :createdFromDate");
		}
		if (searchDTO.getCreatedToDate() != null) {
			conditions.add("t.created_time <= :createdToDate");
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
		if (searchDTO.getCustomerName() != null && !searchDTO.getCustomerName().isEmpty()) {
			query.setParameter("customerName", "%" + searchDTO.getCustomerName() + "%");
		}
		if (searchDTO.getGoodId() != null) {
			query.setParameter("goodId", searchDTO.getGoodId());
		}
		if (searchDTO.getProductId() != null) {
			query.setParameter("productId", searchDTO.getProductId());
		}
		if (searchDTO.getVehiclePrefix() != null && !searchDTO.getVehiclePrefix().isEmpty()) {
			query.setParameter("vehiclePrefix", searchDTO.getVehiclePrefix() + "%");
		}
		if (searchDTO.getVehicleNumber() != null && !searchDTO.getVehicleNumber().isEmpty()) {
			query.setParameter("vehicleNumber", "%" + searchDTO.getVehicleNumber() + "%");
		}
		if (searchDTO.getDriverName() != null && !searchDTO.getDriverName().isEmpty()) {
			query.setParameter("driverName", "%" + searchDTO.getDriverName() + "%");
		}
		if (searchDTO.getWeight() != null) {
			query.setParameter("weight", searchDTO.getWeight());
		}
		if (searchDTO.getInOutStatus() != null) {
			query.setParameter("inOutStatus", searchDTO.getInOutStatus());
		}
		if (searchDTO.getSessionCode() != null && !searchDTO.getSessionCode().isEmpty()) {
			query.setParameter("sessionCode", searchDTO.getSessionCode());
		}
		if (searchDTO.getUserId() != null) {
			query.setParameter("userId", searchDTO.getUserId());
		}
		if (searchDTO.getCreatedFromDate() != null) {
			Date date = DateUtils.stringToDate(searchDTO.getCreatedFromDate(),
					CommonDateFormats.STANDARD_24_HOUR_DATE_FORMAT);
			query.setParameter("createdFromDate", date);
		}
		if (searchDTO.getCreatedToDate() != null) {
			Date date = DateUtils.stringToDate(searchDTO.getCreatedToDate(),
					CommonDateFormats.STANDARD_24_HOUR_DATE_FORMAT);
			query.setParameter("createdToDate", date);
		}
	}

}
