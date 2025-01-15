package org.flexitech.projects.embedded.truckscale.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String convertToJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}
}
