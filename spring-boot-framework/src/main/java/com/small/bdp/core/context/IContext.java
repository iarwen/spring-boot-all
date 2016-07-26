package com.small.bdp.core.context;

/**
 * 程序上下文 ，用来扩展传递参数的逻辑
 * 
 * @author xiaojiao_li
 *
 */
public interface IContext {

	/**
	 * 放置参数进去
	 * 
	 * @param key
	 * @param value
	 */
	public void putValue(String key, Object value);

	/**
	 * 获取参数出来
	 * 
	 * @param key
	 * @return
	 */
	public Object getValue(String key);

}
