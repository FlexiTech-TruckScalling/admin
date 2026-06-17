package org.flexitech.projects.embedded.truckscale.dto.user;

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
public class SbscriptionTypeDTO extends CommonDTO {
	private String subscriptionName;

	private int period;

	public SbscriptionTypeDTO(SubscriptionType sbscriptionType) {
		this.subscriptionName = sbscriptionType.getSubscriptionName();
		this.period = sbscriptionType.getPeriod();

		// default fields
		setField(sbscriptionType);
	}

}
