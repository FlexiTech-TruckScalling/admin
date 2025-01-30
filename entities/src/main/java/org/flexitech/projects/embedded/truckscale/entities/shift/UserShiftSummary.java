package org.flexitech.projects.embedded.truckscale.entities.shift;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.USER_SHIFT_SUMMARY_TABLE)
public class UserShiftSummary extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7008882084877143898L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private Users user;
	
	@ManyToOne
	@JoinColumn(name = "counter_id", nullable = false)
	private Counters counter;
	
	@ManyToOne
	@JoinColumn(name = "shift_id", nullable = false)
	private UserShift userShift;
	
	@Column(name = "total_transaction")
	private Integer totalTransaction;
	
	@Column(name = "total_in_transaction")
	private Integer totalInTransaction;
	
	@Column(name = "total_out_transaction")
	private Integer totalOutTransaction;
	
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	@ManyToOne
	@JoinColumn(name = "end_by_user_id", nullable = false)
	private Users endByUser;
	
}
