package com.small.bdp.framework.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.small.bdp.core.context.IContext;
import com.small.bdp.core.query.CompareType;
import com.small.bdp.core.query.QueryFilterInfo;
import com.small.bdp.core.query.SelectorInfo;
import com.small.bdp.core.query.SortItem;
import com.small.bdp.framework.domain.AbstractCoreBaseInfo;
import com.small.bdp.framework.exception.BaseException;
import com.small.bdp.framework.service.ICoreBaseService;

/**
 * 服务层基础服务对象
 * 
 * 提供了常用的增、删、改、查相关的方法
 * 
 * @author xiaojiao_li
 *
 */
public abstract class AbstractCoreBaseService<T extends AbstractCoreBaseInfo> implements ICoreBaseService<T> {

	// 日志对象，子类可以直接使用输出
	protected Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void addnew(IContext ctx, T object) {
		getDaoInstance().addnew(ctx, object);
	}

	@Override
	public void batchPersist(IContext ctx, List<T> list) {
		getDaoInstance().batchPersist(ctx, list);
	}

	@Override
	public void update(IContext ctx, String fid, T object) {
		getDaoInstance().update(ctx, fid, object);
	}

	@Override
	public void update(IContext ctx, String fid, T object, boolean isAllowNoData) {
		getDaoInstance().update(ctx, fid, object, isAllowNoData);
	}

	@Override
	public void deleteAll(IContext ctx) {
		getDaoInstance().deleteAll(ctx);
	}

	@Override
	public void delete(IContext ctx, T object) {
		getDaoInstance().delete(ctx, object);
	}

	@Override
	public void delete(IContext ctx, String fid) {
		getDaoInstance().delete(ctx, fid);
	}

	@Override
	public void delete(IContext ctx, T object, boolean isAllowNoData) {
		getDaoInstance().delete(ctx, object, isAllowNoData);
	}

	@Override
	public void delete(IContext ctx, String fid, boolean isAllowNoData) {
		getDaoInstance().delete(ctx, fid, isAllowNoData);
	}

	@Override
	public void delete(IContext ctx, QueryFilterInfo ev) {
		getDaoInstance().delete(ctx, ev);
	}

	@Override
	public void save(IContext ctx, T object) {
		getDaoInstance().save(ctx, object);
	}

	@Override
	public T findById(IContext ctx, String fid) {
		return getDaoInstance().findById(ctx, fid);
	}

	@Override
	public T findById(IContext ctx, String fid, SelectorInfo sic) {
		return getDaoInstance().findById(ctx, fid, sic);
	}

	@Override
	public T findById(IContext ctx, String fid, boolean isAllowNoData) {
		if (!isAllowNoData) {
			throw new BaseException("At service isAllowNoData must be true!");
		}
		return getDaoInstance().findById(ctx, fid, isAllowNoData);
	}

	@Override
	public T findById(IContext ctx, String fid, SelectorInfo sic, boolean isAllowNoData) {
		return getDaoInstance().findById(ctx, fid, sic, isAllowNoData);
	}

	@Override
	public boolean exists(IContext ctx, String fid) {
		return getDaoInstance().exists(ctx, fid);
	}

	@Override
	public boolean exists(IContext ctx, QueryFilterInfo ev) {
		return getDaoInstance().exists(ctx, ev);
	}

	@Override
	public List<T> findByIds(IContext ctx, Set<String> ids) {
		return getDaoInstance().findByIds(ctx, ids);
	}

	@Override
	public List<T> findByIds(IContext ctx, Set<String> ids, SelectorInfo sic) {
		return getDaoInstance().findByIds(ctx, ids, sic);
	}

	@Override
	public List<T> findByIds(IContext ctx, Set<String> ids, List<SortItem> sorts) {
		return getDaoInstance().findByIds(ctx, ids, sorts);
	}

	@Override
	public List<T> findByIds(IContext ctx, Set<String> ids, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findByIds(ctx, ids, sorts, sic);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue) {
		return getDaoInstance().findByField(ctx, fieldName, fieldValue);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts) {
		return getDaoInstance().findByField(ctx, fieldName, fieldValue, sorts);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, SelectorInfo sic) {
		return getDaoInstance().findByField(ctx, fieldName, fieldValue, sic);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findByField(ctx, fieldName, fieldValue, sorts, sic);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type) {
		return getDaoInstance().findByField(ctx, fieldName, fieldValue, type);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts) {
		return getDaoInstance().findByField(ctx, fieldName, fieldValue, type, sorts);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type, SelectorInfo sic) {
		return getDaoInstance().findByField(ctx, fieldName, fieldValue, type, sic);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findByField(ctx, fieldName, fieldValue, type, sorts, sic);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize) {
		return getDaoInstance().findByFieldWithPaging(ctx, fieldName, fieldValue, currentPageIndex, pageSize);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize, List<SortItem> sorts) {
		return getDaoInstance().findByFieldWithPaging(ctx, fieldName, fieldValue, currentPageIndex, pageSize, sorts);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize, SelectorInfo sic) {
		return getDaoInstance().findByFieldWithPaging(ctx, fieldName, fieldValue, currentPageIndex, pageSize, sic);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize, List<SortItem> sorts,
			SelectorInfo sic) {
		return getDaoInstance().findByFieldWithPaging(ctx, fieldName, fieldValue, currentPageIndex, pageSize, sorts, sic);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize) {
		return getDaoInstance().findByFieldWithPaging(ctx, fieldName, fieldValue, type, currentPageIndex, pageSize);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize,
			List<SortItem> sorts) {
		return getDaoInstance().findByFieldWithPaging(ctx, fieldName, fieldValue, type, currentPageIndex, pageSize, sorts);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize,
			SelectorInfo sic) {
		return getDaoInstance().findByFieldWithPaging(ctx, fieldName, fieldValue, currentPageIndex, pageSize, sic);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize,
			List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findByFieldWithPaging(ctx, fieldName, fieldValue, type, currentPageIndex, pageSize, sorts, sic);
	}

	@Override
	public List<T> findByFields(IContext ctx, QueryFilterInfo ev) {
		return getDaoInstance().findByFields(ctx, ev);
	}

	@Override
	public List<T> findByFields(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts) {
		return getDaoInstance().findByFields(ctx, ev, sorts);
	}

	@Override
	public List<T> findByFields(IContext ctx, QueryFilterInfo ev, SelectorInfo sic) {
		return getDaoInstance().findByFields(ctx, ev, sic);
	}

	@Override
	public List<T> findByFields(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findByFields(ctx, ev, sorts, sic);
	}

	@Override
	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize) {
		return getDaoInstance().findByFieldsWithPaging(ctx, ev, currentPageIndex, pageSize);
	}

	@Override
	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize, List<SortItem> sorts) {
		return getDaoInstance().findByFieldsWithPaging(ctx, ev, currentPageIndex, pageSize, sorts);
	}

	@Override
	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize, SelectorInfo sic) {
		return getDaoInstance().findByFieldsWithPaging(ctx, ev, currentPageIndex, pageSize, sic);
	}

	@Override
	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findByFieldsWithPaging(ctx, ev, currentPageIndex, pageSize, sorts, sic);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue) {
		return getDaoInstance().findByFieldSingle(ctx, fieldName, fieldValue);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, SelectorInfo sic) {
		return getDaoInstance().findByFieldSingle(ctx, fieldName, fieldValue, sic);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts) {
		return getDaoInstance().findByFieldSingle(ctx, fieldName, fieldValue, sorts);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findByFieldSingle(ctx, fieldName, fieldValue, sorts, sic);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type) {
		return getDaoInstance().findByFieldSingle(ctx, fieldName, fieldValue, type);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type, SelectorInfo sic) {
		return getDaoInstance().findByFieldSingle(ctx, fieldName, fieldValue, type, sic);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts) {
		return getDaoInstance().findByFieldSingle(ctx, fieldName, fieldValue, type, sorts);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findByFieldSingle(ctx, fieldName, fieldValue, type, sorts, sic);
	}

	@Override
	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev) {
		return getDaoInstance().findByFieldsSingle(ctx, ev);
	}

	@Override
	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev, SelectorInfo sic) {
		return getDaoInstance().findByFieldsSingle(ctx, ev, sic);
	}

	@Override
	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts) {
		return getDaoInstance().findByFieldsSingle(ctx, ev, sorts);
	}

	@Override
	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findByFieldsSingle(ctx, ev, sorts, sic);
	}

	@Override
	public List<T> findAll(IContext ctx) {
		return getDaoInstance().findAll(ctx);
	}

	@Override
	public List<T> findAll(IContext ctx, SelectorInfo sic) {
		return getDaoInstance().findAll(ctx, sic);
	}

	@Override
	public List<T> findAll(IContext ctx, List<SortItem> sorts) {
		return getDaoInstance().findAll(ctx, sorts);
	}

	@Override
	public List<T> findAll(IContext ctx, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findAll(ctx, sorts, sic);
	}

	@Override
	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize) {
		return getDaoInstance().findAllWithPaging(ctx, currentPageIndex, pageSize);
	}

	@Override
	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize, SelectorInfo sic) {
		return getDaoInstance().findAllWithPaging(ctx, currentPageIndex, pageSize, sic);
	}

	@Override
	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize, List<SortItem> sorts) {
		return getDaoInstance().findAllWithPaging(ctx, currentPageIndex, pageSize, sorts);
	}

	@Override
	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize, List<SortItem> sorts, SelectorInfo sic) {
		return getDaoInstance().findAllWithPaging(ctx, currentPageIndex, pageSize, sorts, sic);
	}

}
