package org.flexitech.projects.embedded.truckscale.entities.counters;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;
import org.flexitech.projects.embedded.truckscale.entities.user.Users;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.COUNTER_TABLE)
public class Counters extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7266066125626111273L;
	
	private String name;

	@Column(name = "counter_ip")
	private String counterIp;
	
	@OneToMany(mappedBy = "counter", cascade = CascadeType.ALL)
	private List<CounterSetting> settings;
	
	@OneToMany(mappedBy = "counter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Users> users;
	
}
