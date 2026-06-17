package org.flexitech.projects.embedded.truckscale.entities.superadmin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.SUBSCRIPTION_TYPE)
public class SubscriptionType extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "subscription_name")
	private String subscriptionName;

	private int period;


}
