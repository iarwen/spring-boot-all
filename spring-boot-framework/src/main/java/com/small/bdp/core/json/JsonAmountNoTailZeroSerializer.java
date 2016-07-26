package com.small.bdp.core.json;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.small.bdp.util.NumberUtils;

/**
 * 金额格式化
 * 
 * @author xiaojiao_li
 *
 */
@Component
public class JsonAmountNoTailZeroSerializer extends JsonSerializer<BigDecimal> {

	@Override
	public void serialize(BigDecimal var, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
		if (var == null) {
			var = BigDecimal.ZERO;
		}
		gen.writeString(NumberUtils.removeTailZero(var));
	}

}