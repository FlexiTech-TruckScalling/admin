package org.flexitech.projects.embedded.truckscale.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonPagingDTO<T> {
	private Integer page;
	@JsonProperty("has_next_page")
	private boolean hasNextPage;
	@JsonProperty("total_page")
	private Integer totalPage;
	@JsonProperty("total_records")
	private Integer totalRecord;
	@JsonProperty("next_page_url")
	private String nextPageUrl;
	
	private List<T> data = new ArrayList<T>();
}
