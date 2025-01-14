package org.flexitech.projects.embedded.truckscale.common.network.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response {
	private Integer responseCode;
	private String responseMessage;
}
