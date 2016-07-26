package com.small.bdp.framework.task.impl;

import org.apache.log4j.Logger;

import com.small.bdp.framework.task.ICoreTask;

/**
 * 定时任务基类
 * 
 * @author xiaojiao_li
 *
 */
public abstract class AbstractCoreTask implements ICoreTask {

	protected Logger logger = Logger.getLogger(this.getClass());

}
