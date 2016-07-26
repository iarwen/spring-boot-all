package com.small.bdp.core.xls;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Import {
	/**
	 * xls中的名称
	 * @return
	 */
	String displayName() default "";
	
	/**
	 * set方法名称
	 * @return
	 */
	String name() default "";
	
	/**
	 * 替换
	 */
	String[] replace() default {};
	
	/**
	 * 顺序，在excel中的字段先后顺序  只要按整形从小到大排列即可
	 */
	int order() default 0;
	
	/**导入数据的精度，针对数值型*/
	int scale() default 0;
}
