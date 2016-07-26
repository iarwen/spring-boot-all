package com.small.bdp.core.json;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 金额格式化
 * 
 * @author xiaojiao_li
 *
 */
@Component
public class JsonAmountSerializer extends JsonSerializer<BigDecimal> {

	@Override
	public void serialize(BigDecimal var, JsonGenerator gen,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		if (var == null) {
			var = BigDecimal.ZERO;
		}
		var = var.setScale(2, BigDecimal.ROUND_HALF_UP);
		gen.writeString(var.toString());
	}

}