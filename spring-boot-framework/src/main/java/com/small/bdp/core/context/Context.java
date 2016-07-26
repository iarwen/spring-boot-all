package com.small.bdp.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用程序上下文
 * 
 * @author xiaojiao_li
 *
 */
public class Context implements IContext {

	private Map<String, Object> parmas = new HashMap<String, Object>();

	private Context() {
	}

	@Override
	public void putValue(String key, Object value) {
		parmas.put(key, value);
	}

	@Override
	public Object getValue(String key) {
		return parmas.get(key);
	}

	/**
	 * 创建默认的Context
	 * 
	 * @return
	 */
	public static IContext createDefaultContext() {
		return new Context();
	}

}
