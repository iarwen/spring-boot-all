package com.small.bdp.core.json;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.small.bdp.util.DateUtils;

@Component
public class JsonSimpleDateSerializer2 extends JsonSerializer<Date> {

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
		gen.writeString(DateUtils.formatDate(date, "yyyy/MM/dd"));
	}

}