package com.sentieo.utils;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class JSONUtils {

	private ObjectMapper objectMapper;

	public JSONUtils() {
		objectMapper = new ObjectMapper();
		objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY).setVisibility(JsonMethod.SETTER, Visibility.NONE)
				.setVisibility(JsonMethod.GETTER, Visibility.NONE).setVisibility(JsonMethod.IS_GETTER, Visibility.NONE)
				.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false)
				.configure(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS, false);
	}

	public String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException("Json conversion failure - Received object is not in valid format - " + object,
					e);
		}
	}

}
