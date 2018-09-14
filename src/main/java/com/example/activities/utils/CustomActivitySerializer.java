package com.example.activities.utils;

import com.example.activities.entity.Activity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * TODO Implement this method to allow exporting information using the same
 * import file format.
 * 
 */
public class CustomActivitySerializer extends StdSerializer<Activity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomActivitySerializer() {
		this(null);
	}

	public CustomActivitySerializer(Class<Activity> t) {
		super(t);
	}

	@Override
	public void serialize(Activity car, JsonGenerator jsonGenerator, SerializerProvider serializer) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
