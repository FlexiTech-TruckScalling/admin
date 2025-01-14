package org.flexitech.projects.embedded.truckscale.common.network.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BaseResponse<T> extends Response{
	@JsonInclude(Include.NON_NULL)
	private T data;
}
