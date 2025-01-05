package org.flexitech.projects.embedded.truckscale.entities.shift;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.USER_SHIFT_TABLE)
public class UserShift extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1038122743857044139L;

	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "end_time")
	private Date endTime;

	@Column(name = "shift_status")
	private Integer shiftStatus; // 1 = Opened, 2 = Closed

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private Users user;

}
