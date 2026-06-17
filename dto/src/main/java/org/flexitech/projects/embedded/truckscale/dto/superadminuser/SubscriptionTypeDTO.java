package org.flexitech.projects.embedded.truckscale.dto.superadminuser;

import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.entities.superadmin.SubscriptionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionTypeDTO extends CommonDTO {
	private String subscriptionName;

	private int period;

	public SubscriptionTypeDTO(SubscriptionType s) {
		super();
		this.subscriptionName = s.getSubscriptionName();
		this.period = s.getPeriod();
		
		setField(s);
	}

}
