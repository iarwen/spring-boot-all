package com.small.bdp.framework.dao;

import java.util.List;
import java.util.Set;

import com.small.bdp.core.context.IContext;
import com.small.bdp.core.query.CompareType;
import com.small.bdp.core.query.QueryFilterInfo;
import com.small.bdp.core.query.SelectorInfo;
import com.small.bdp.core.query.SortItem;
import com.small.bdp.framework.domain.AbstractCoreBaseInfo;

/**
 * 
 * 数据访问对象接口的基对象，用来限定标准的访问接口，每个子DAO对象都应该实现。
 * 
 * @author xiaojiao_li
 *
 */
public interface ICoreBaseDao<T extends AbstractCoreBaseInfo> {

	public String getDaoTableName();

	// 新增类
	public void addnew(IContext ctx, T object);

	// 新增全部的类
	public void batchPersist(IContext ctx, List<T> list);

	// 更新类
	public void update(IContext ctx, String fid, T object);

	/**
	 * 
	 * @param fid
	 * @param object
	 * @param isAllowNoData
	 *            是否允许匹配不到数据的更新，默认不允许，如果按fid更新不到记录，则抛出异常。
	 */
	public void update(IContext ctx, String fid, T object, boolean isAllowNoData);

	// 删除类
	public void deleteAll(IContext ctx);

	public void delete(IContext ctx, T object);

	public void delete(IContext ctx, String fid);

	public void delete(IContext ctx, T object, boolean isAllowNoData);

	public void delete(IContext ctx, String fid, boolean isAllowNoData);

	public void delete(IContext ctx, QueryFilterInfo ev);

	// 保存,自动判断新增还是更新
	public void save(IContext ctx, T object);

	// 根据Id查询类
	public T findById(IContext ctx, String fid);

	public T findById(IContext ctx, String fid, SelectorInfo sic);

	public T findById(IContext ctx, String fid, boolean isAllowNoData);

	public T findById(IContext ctx, String fid, SelectorInfo sic, boolean isAllowNoData);

	public boolean exists(IContext ctx, String fid);

	public boolean exists(IContext ctx, QueryFilterInfo ev);

	public List<T> findByIds(IContext ctx, Set<String> ids);

	public List<T> findByIds(IContext ctx, Set<String> ids, SelectorInfo sic);

	public List<T> findByIds(IContext ctx, Set<String> ids, List<SortItem> sorts);

	public List<T> findByIds(IContext ctx, Set<String> ids, List<SortItem> sorts, SelectorInfo sic);

	// 按字段查询类
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue);

	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts);

	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, SelectorInfo sic);

	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts, SelectorInfo sic);

	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type);

	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts);

	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type, SelectorInfo sic);

	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts, SelectorInfo sic);

	// 按字段查询类，分页
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize);

	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize, List<SortItem> sorts);

	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize, SelectorInfo sic);

	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize, List<SortItem> sorts,
			SelectorInfo sic);

	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize);

	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize,
			List<SortItem> sorts);

	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize,
			SelectorInfo sic);

	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize,
			List<SortItem> sorts, SelectorInfo sic);

	// 多字段查询方法
	public List<T> findByFields(IContext ctx, QueryFilterInfo ev);

	public List<T> findByFields(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts);

	public List<T> findByFields(IContext ctx, QueryFilterInfo ev, SelectorInfo sic);

	public List<T> findByFields(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts, SelectorInfo sic);

	// 多字段查询方法，分页
	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize);

	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize, List<SortItem> sorts);

	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize, SelectorInfo sic);

	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize, List<SortItem> sorts, SelectorInfo sic);

	// 按字段查询，返回单个结果类
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue);

	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, SelectorInfo sic);

	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts);

	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts, SelectorInfo sic);

	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type);

	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type, SelectorInfo sic);

	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts);

	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts, SelectorInfo sic);

	// 多字段查询，返单单个结果类
	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev);

	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev, SelectorInfo sic);

	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts);

	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts, SelectorInfo sic);

	// 查询全部数据的逻辑
	public List<T> findAll(IContext ctx);

	public List<T> findAll(IContext ctx, SelectorInfo sic);

	public List<T> findAll(IContext ctx, List<SortItem> sorts);

	public List<T> findAll(IContext ctx, List<SortItem> sorts, SelectorInfo sic);

	// 分页
	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize);

	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize, SelectorInfo sic);

	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize, List<SortItem> sorts);

	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize, List<SortItem> sorts, SelectorInfo sic);

}
