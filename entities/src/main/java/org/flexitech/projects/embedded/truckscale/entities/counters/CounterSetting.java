package org.flexitech.projects.embedded.truckscale.entities.counters;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.flexitech.projects.embedded.truckscale.entities.setting.MasterCounterSetting;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.COUNTER_SETTING_TABLE)
public class CounterSetting extends BaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = -2606044636255212424L;

	@ManyToOne
	@JoinColumn(name = "counter_id", nullable = false)
	private Counters counter;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "master_counter_id", nullable = false)
	private MasterCounterSetting masterCounterSetting;
	
	private String value;
	
	private String remark;
}
