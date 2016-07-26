package com.small.bdp.core.xls;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Export {
	/**
	 * 导出表头中文名
	 */
	String title() default "";
	
	/**
	 * 集合是否同行显示
	 * type: 0-普通列 1-实体对象列  2-集合列 3-动态列名 4-动态列对应值
	 */
	FieldType type() default FieldType.ZERO;
	
	/**
	 * 针对type 为 2处理字段
	 */
	String name() default "";
	
	/**
	 * 是否需要替换内容
	 */
	String[] replace() default {};
	
	Fetch fetch() default Fetch.ONETOMANY;
	/**
	 * 顺序，在excel中的字段先后顺序  只要按整形从小到大排列即可
	 */
	int order() default 0;
}
