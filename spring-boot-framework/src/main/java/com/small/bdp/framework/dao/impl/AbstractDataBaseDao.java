package com.small.bdp.framework.dao.impl;

import com.small.bdp.framework.dao.IDatabaseDao;
import com.small.bdp.framework.domain.DataBaseInfo;

/**
 * 基础资料DAO实现
 * 
 * 子类继承后需要添加@Repository注解,系统可自动识别加载
 * 
 * @author xiaojiao_li
 *
 * @param <T>
 */
public abstract class AbstractDataBaseDao<T extends DataBaseInfo> extends AbstractCoreBaseDao<T> implements IDatabaseDao<T> {

}
