package com.small.bdp.framework.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.small.bdp.core.context.IContext;
import com.small.bdp.core.query.CompareType;
import com.small.bdp.core.query.FilterItemInfo;
import com.small.bdp.core.query.QueryFilterInfo;
import com.small.bdp.core.query.QueryUtils;
import com.small.bdp.core.query.SelectorInfo;
import com.small.bdp.core.query.SortItem;
import com.small.bdp.framework.dao.ICoreBaseDao;
import com.small.bdp.framework.domain.AbstractCoreBaseInfo;
import com.small.bdp.framework.exception.BaseException;
import com.small.bdp.framework.exception.DataException;
import com.small.bdp.framework.exception.DataObjectNotFoundException;
import com.small.bdp.framework.exception.PKEmptyException;
import com.small.bdp.util.StringUtils;

/**
 * 
 * 数据访问对象的通用父对象，实现的基本常用的通用接口。
 * 
 * 需要添加@Repository注解,系统可自动识别加载
 * 
 * @author xiaojiao_li
 *
 */
public abstract class AbstractCoreBaseDao<T extends AbstractCoreBaseInfo> implements ICoreBaseDao<T> {

	// 日志对象，子类可以直接使用输出
	protected Logger logger = Logger.getLogger(this.getClass());

	// 配置的jdbcTemplate实现实例
	@Autowired
	private JdbcTemplate jdbcTemplate = null;

	// 配置实现的EntityManager实例
	@PersistenceContext
	private EntityManager em = null;

	/**
	 * 获取EntityManager模板
	 * 
	 * @return
	 */
	public synchronized EntityManager getEm() {
		return em;
	}

	// 定义实体类的Class
	private Class<T> entityClass;

	/***
	 * 获取当前实体对象的类定义
	 * 
	 * @return
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * 获取当前实体的类名
	 * 
	 * @return
	 */
	public String getEntityName() {
		return entityClass.getSimpleName();
	}

	@Override
	public String getDaoTableName() {

		String tablename = null;

		Table[] tt = getEntityClass().getAnnotationsByType(Table.class);
		if (tt != null && tt.length > 0) {
			tablename = tt[0].name();
		}
		if (tablename == null) {
			throw new BaseException("The info object get tablename error!");
		}
		return tablename;

	}

	// 构造
	@SuppressWarnings("unchecked")
	public AbstractCoreBaseDao() {
		super();
		// 获取泛型类型
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	}

	// =======================开始业务逻辑实现=======================//

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addnew(IContext ctx, T object) {

		if (object == null) {
			throw new DataException("The entity is :" + getEntityName() + ",the param object is null!");
		}

		if (StringUtils.isEmptyString(object.getFid(), true)) {
			throw new PKEmptyException("The entity is :" + getEntityName() + ",at the addnew time, fid is empty!");
		}

		// 校验一下，fid是否有重复。

		// 保存对象
		getEm().persist(object);

		// 清空缓存
		// clearHibernateSession();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void batchPersist(IContext ctx, List<T> list) {
		if (list == null || list.isEmpty()) {
			throw new DataException("The entity is :" + getEntityName() + ",the param list is empty!");
		}
		int count = 0;
		for (T object : list) {
			if (StringUtils.isEmptyString(object.getFid(), true)) {
				object.setFid(UUID.randomUUID().toString());
			}
			em.persist(object);
			// 刷入库 防止OOM
			if (count % 500 == 0) {
				em.flush();
				em.clear();
			}
			count++;
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(IContext ctx, String fid, T object) {
		// 更新，默认如果没有update到对象，直接报错。
		update(ctx, fid, object, false);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(IContext ctx, String fid, T object, boolean isAllowNoData) {

		if (StringUtils.isEmptyString(fid, true)) {
			throw new PKEmptyException("The entity is :" + getEntityName() + ",update's params fid is empty!");
		}

		if (object == null) {
			throw new DataException("The entity is :" + getEntityName() + ",the param object is null!");
		}

		// 如果无数据更新设置为不允许，则会抛出错误。

		try {
			T findObj = findById(ctx, fid);
			try {
				BeanUtils.copyProperties(findObj, object);
				object = findObj;
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new DataException("copy bean values to hibernate bean error!", e);
			}

			object = findObj;
		} catch (DataObjectNotFoundException ex) {
			if (!isAllowNoData) {
				throw ex;
			}
		}

		getEm().merge(object);

		// 清空缓存
		// clearHibernateSession();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAll(IContext ctx) {

		List<T> allList = findAll(ctx);
		for (T t : allList) {
			delete(ctx, t);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(IContext ctx, T object) {
		// 删除，如果匹配不到对象，报错。
		delete(ctx, object, false);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(IContext ctx, String fid) {
		// 删除，如果匹配不到对象，报错。
		delete(ctx, fid, false);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(IContext ctx, T object, boolean isAllowNoData) {
		if (object == null) {
			throw new DataException("The entity is :" + getEntityName() + ",the param object is null!");
		}

		String fid = object.getFid();

		if (StringUtils.isEmptyString(fid, true)) {
			throw new PKEmptyException("The entity is :" + getEntityName() + ",delete's params fid is empty!");
		}

		// 如果无数据删除设置为不允许，则会抛出错误。

		try {
			T findObj = findById(ctx, fid);
			object = findObj;
		} catch (DataObjectNotFoundException ex) {
			if (!isAllowNoData) {
				throw ex;
			}
		}

		getEm().remove(object);

		// 清空缓存
		// clearHibernateSession();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(IContext ctx, QueryFilterInfo ev) {

		List<T> findlist = findByFields(ctx, ev);

		for (T t : findlist) {
			delete(ctx, t);
		}

		// 清空缓存
		// clearHibernateSession();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(IContext ctx, String fid, boolean isAllowNoData) {

		T entity = null;
		try {
			entity = entityClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new DataException(e);
		}

		if (entity == null) {
			throw new DataException("Init entity error,entity type is :" + getEntityName());
		}
		entity.setFid(fid);

		// 包装成对象后，删除
		delete(ctx, entity, isAllowNoData);

		// 清空缓存
		// clearHibernateSession();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(IContext ctx, T object) {
		String fid = object.getFid();
		if (StringUtils.isEmptyString(fid) || !exists(ctx, fid)) {
			// 生成主键
			if (StringUtils.isEmptyString(fid)) {
				object.setFid(UUID.randomUUID().toString());
			}
			// AddNew the object.
			addnew(ctx, object);
		} else {
			// Update the object.
			update(ctx, fid, object);
		}

		// 清空缓存
		// clearHibernateSession();
	}

	@Override
	public T findById(IContext ctx, String fid) {
		return findById(ctx, fid, null);
	}

	@Override
	public T findById(IContext ctx, String fid, SelectorInfo sic) {
		return findById(ctx, fid, sic, false);
	}

	@Override
	public T findById(IContext ctx, String fid, boolean isAllowNoData) {
		return findById(ctx, fid, null, isAllowNoData);
	}

	@Override
	public T findById(IContext ctx, String fid, SelectorInfo sic, boolean isAllowNoData) {

		if (StringUtils.isEmptyString(fid, true)) {
			throw new PKEmptyException("The entity is :" + getEntityName() + ",findById's params fid is empty!");
		}
		String selectSql = QueryUtils.buildSelectorSql(sic);
		TypedQuery<T> query = getEm().createQuery(selectSql + "from " + getEntityName() + " where fid=:fid ", entityClass);
		query.setParameter("fid", fid);
		T object = query.getSingleResult();

		if (object == null) {
			if (isAllowNoData) {
				return null;
			} else {
				throw new DataObjectNotFoundException("The entity is :" + getEntityName() + ",findById : [" + fid + "] not found data!");
			}
		}
		return object;
	}

	@Override
	public boolean exists(IContext ctx, String fid) {
		T findinfo = findById(ctx, fid, true);
		return findinfo != null;
	}

	@Override
	public boolean exists(IContext ctx, QueryFilterInfo ev) {
		T findinfo = findByFieldsSingle(ctx, ev);
		return findinfo != null;
	}

	@Override
	public List<T> findByIds(IContext ctx, Set<String> ids) {
		return findByIds(ctx, ids, null, null);
	}

	@Override
	public List<T> findByIds(IContext ctx, Set<String> ids, List<SortItem> sorts) {
		return findByIds(ctx, ids, sorts, null);
	}

	@Override
	public List<T> findByIds(IContext ctx, Set<String> ids, SelectorInfo sic) {
		return findByIds(ctx, ids, null, sic);
	}

	@Override
	public List<T> findByIds(IContext ctx, Set<String> ids, List<SortItem> sorts, SelectorInfo sic) {
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<T>();
		}
		QueryFilterInfo ev = new QueryFilterInfo();
		ev.addFilterItemInfo("fid", ids, CompareType.In);
		List<T> list = findByFields(ctx, ev);

		return list;
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue) {
		return findByField(ctx, fieldName, fieldValue, null, null, null);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts) {
		return findByField(ctx, fieldName, fieldValue, null, sorts, null);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, SelectorInfo sic) {
		return findByField(ctx, fieldName, fieldValue, null, null, sic);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts, SelectorInfo sic) {
		return findByField(ctx, fieldName, fieldValue, null, sorts, sic);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type) {
		return findByField(ctx, fieldName, fieldValue, type, null, null);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts) {
		return findByField(ctx, fieldName, fieldValue, type, sorts, null);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type, SelectorInfo sic) {
		return findByField(ctx, fieldName, fieldValue, type, null, sic);
	}

	@Override
	public List<T> findByField(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts, SelectorInfo sic) {
		QueryFilterInfo ev = new QueryFilterInfo();
		if (type != null) {
			ev.addFilterItemInfo(fieldName, fieldValue, type);
		} else {
			ev.addFilterItemInfo(fieldName, fieldValue);
		}

		return findByFields(ctx, ev, sorts, sic);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue) {
		return findByFieldSingle(ctx, fieldName, fieldValue, null, null, null);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, SelectorInfo sic) {
		return findByFieldSingle(ctx, fieldName, fieldValue, null, null, sic);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts) {
		return findByFieldSingle(ctx, fieldName, fieldValue, null, sorts, null);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, List<SortItem> sorts, SelectorInfo sic) {
		return findByFieldSingle(ctx, fieldName, fieldValue, null, sorts, sic);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type) {
		return findByFieldSingle(ctx, fieldName, fieldValue, type, null, null);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type, SelectorInfo sic) {
		return findByFieldSingle(ctx, fieldName, fieldValue, type, null, sic);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts) {
		return findByFieldSingle(ctx, fieldName, fieldValue, type, sorts, null);
	}

	@Override
	public T findByFieldSingle(IContext ctx, String fieldName, Object fieldValue, CompareType type, List<SortItem> sorts, SelectorInfo sic) {
		// 用分页的方法获取第一条数据
		List<T> findList = findByFieldWithPaging(ctx, fieldName, fieldValue, type, 1, 1, sorts, sic);
		if (findList == null || findList.isEmpty()) {
			return null;
		}
		return findList.get(0);
	}

	@Override
	public List<T> findAll(IContext ctx) {
		return findAll(ctx, null, null);
	}

	@Override
	public List<T> findAll(IContext ctx, SelectorInfo sic) {
		return findAll(ctx, null, sic);
	}

	@Override
	public List<T> findAll(IContext ctx, List<SortItem> sorts) {
		return findAll(ctx, sorts, null);
	}

	@Override
	public List<T> findAll(IContext ctx, List<SortItem> sorts, SelectorInfo sic) {

		String selectSql = QueryUtils.buildSelectorSql(sic);
		String sortSql = QueryUtils.buildOrderBySql(sorts);
		TypedQuery<T> query = getEm().createQuery(selectSql + "from " + getEntityName() + "" + sortSql + " ", entityClass);

		// 对Hibernate返回的结果集进行处理，让全部变为JavaBean类型集合
		List<T> dataList = query.getResultList();

		return dataList;
	}

	// =============多字段查询逻辑========================//
	@Override
	public List<T> findByFields(IContext ctx, QueryFilterInfo ev) {
		return findByFields(ctx, ev, null, null);
	}

	@Override
	public List<T> findByFields(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts) {
		return findByFields(ctx, ev, sorts, null);
	}

	@Override
	public List<T> findByFields(IContext ctx, QueryFilterInfo ev, SelectorInfo sic) {
		return findByFields(ctx, ev, null, sic);
	}

	@Override
	public List<T> findByFields(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts, SelectorInfo sic) {
		if (ev == null || ev.getFilterItems().isEmpty()) {
			throw new BaseException("QueryFilterInfo is empty!");
		}

		String selectSql = QueryUtils.buildSelectorSql(sic);
		String sortSql = QueryUtils.buildOrderBySql(sorts);

		String filterString = ev.getMarkString();
		String[] farray = new String[ev.getFilterItems().size()];
		Object[] varray = new Object[ev.getFilterItems().size()];
		// 替换markString占位符号
		int realcount = 0;
		for (int i = 0; i < ev.getFilterItems().size(); i++) {
			FilterItemInfo ff = ev.getFilterItems().get(i);
			if (ff.getType() == CompareType.IsNull || ff.getType() == CompareType.IsNotNull) {
				filterString = filterString.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator() + " ");
				continue;
			} else if (ff.getType().isReplaceInSql()) {
				if (!ff.getType().isNeedBrackets()) {
					// 判断如果value的字段如果是字符，还要加引号才对
					filterString = filterString.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator() + ("'" + ff.getValue() + "'") + " ");
				} else {
					filterString = filterString.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator() + (" (" + ff.getValue() + ")") + " ");
				}

				continue;
			} else {
				if (!ff.getType().isNeedBrackets()) {
					filterString = filterString
							.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator() + (" :" + ff.getField() + "_" + i) + " ");
				} else {
					filterString = filterString.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator()
							+ (" (:" + ff.getField() + "_" + i + ")") + " ");
				}
			}
			realcount++;
		}
		if (realcount > 0) {
			farray = new String[realcount];
			varray = new Object[realcount];
			int index = 0;
			for (int i = 0; i < ev.getFilterItems().size(); i++) {
				FilterItemInfo ff = ev.getFilterItems().get(i);
				if (ff.getType().isReplaceInSql() || ff.getType() == CompareType.IsNotNull || ff.getType() == CompareType.IsNull) {
					continue;
				}

				farray[index] = ff.getField() + "_" + i;
				varray[index] = ff.getValue();
				// 处理like模式
				if (ff.getType() == CompareType.Like) {
					if (varray[index] instanceof String) {
						if (((String) varray[index]).startsWith("'")) {
							varray[index] = "'" + varray[index] + "'";
						}
					}
				}
				index++;
			}
		}

		List<T> findList = null;
		TypedQuery<T> query = getEm().createQuery(selectSql + "from " + getEntityName() + " where " + filterString + " " + sortSql + " ", entityClass);

		if (realcount > 0) {
			for (int index = 0; index < farray.length; index++) {
				String f = farray[index];
				Object v = varray[index];
				query.setParameter(f, v);
			}
		}

		findList = query.getResultList();

		return findList;
	}

	//
	@Override
	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev) {
		return findByFieldsSingle(ctx, ev, null, null);
	}

	@Override
	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev, SelectorInfo sic) {
		return findByFieldsSingle(ctx, ev, null, sic);
	}

	@Override
	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts) {
		return findByFieldsSingle(ctx, ev, sorts, null);
	}

	@Override
	public T findByFieldsSingle(IContext ctx, QueryFilterInfo ev, List<SortItem> sorts, SelectorInfo sic) {
		// 用分页的方法获取第一条数据
		List<T> findList = findByFieldsWithPaging(ctx, ev, 1, 1, sorts, sic);
		if (findList == null || findList.isEmpty()) {
			return null;
		}

		return findList.get(0);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize) {
		return findByFieldWithPaging(ctx, fieldName, fieldValue, null, currentPageIndex, pageSize, null, null);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize, List<SortItem> sorts) {
		return findByFieldWithPaging(ctx, fieldName, fieldValue, null, currentPageIndex, pageSize, sorts, null);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize, SelectorInfo sic) {
		return findByFieldWithPaging(ctx, fieldName, fieldValue, null, currentPageIndex, pageSize, null, sic);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, int currentPageIndex, int pageSize, List<SortItem> sorts,
			SelectorInfo sic) {
		return findByFieldWithPaging(ctx, fieldName, fieldValue, null, currentPageIndex, pageSize, sorts, sic);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize) {
		return findByFieldWithPaging(ctx, fieldName, fieldValue, type, currentPageIndex, pageSize, null, null);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize,
			List<SortItem> sorts) {
		return findByFieldWithPaging(ctx, fieldName, fieldValue, type, currentPageIndex, pageSize, sorts, null);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize,
			SelectorInfo sic) {
		return findByFieldWithPaging(ctx, fieldName, fieldValue, type, currentPageIndex, pageSize, null, sic);
	}

	@Override
	public List<T> findByFieldWithPaging(IContext ctx, String fieldName, Object fieldValue, CompareType type, int currentPageIndex, int pageSize,
			List<SortItem> sorts, SelectorInfo sic) {
		QueryFilterInfo ev = new QueryFilterInfo();
		if (type != null) {
			ev.addFilterItemInfo(fieldName, fieldValue, type);
		} else {
			ev.addFilterItemInfo(fieldName, fieldValue);
		}
		return findByFieldsWithPaging(ctx, ev, currentPageIndex, pageSize, sorts, sic);
	}

	@Override
	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize) {
		return findByFieldsWithPaging(ctx, ev, currentPageIndex, pageSize, null, null);
	}

	@Override
	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize, List<SortItem> sorts) {
		return findByFieldsWithPaging(ctx, ev, currentPageIndex, pageSize, sorts, null);
	}

	@Override
	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize, SelectorInfo sic) {
		return findByFieldsWithPaging(ctx, ev, currentPageIndex, pageSize, null, sic);
	}

	@Override
	public List<T> findByFieldsWithPaging(IContext ctx, QueryFilterInfo ev, int currentPageIndex, int pageSize, List<SortItem> sorts, SelectorInfo sic) {
		if (ev == null || ev.getFilterItems().isEmpty()) {
			throw new BaseException("QueryFilterInfo is empty!");
		}

		String selectSql = QueryUtils.buildSelectorSql(sic);
		String sortSql = QueryUtils.buildOrderBySql(sorts);

		String filterString = ev.getMarkString();
		String[] farray = new String[ev.getFilterItems().size()];
		Object[] varray = new Object[ev.getFilterItems().size()];
		// 替换markString占位符号
		int realcount = 0;
		for (int i = 0; i < ev.getFilterItems().size(); i++) {
			FilterItemInfo ff = ev.getFilterItems().get(i);
			if (ff.getType() == CompareType.IsNull || ff.getType() == CompareType.IsNotNull) {
				filterString = filterString.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator() + " ");
				continue;
			} else if (ff.getType().isReplaceInSql()) {
				if (!ff.getType().isNeedBrackets()) {
					// 判断如果value的字段如果是字符，还要加引号才对
					filterString = filterString.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator() + ("'" + ff.getValue() + "'") + " ");
				} else {
					filterString = filterString.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator() + (" (" + ff.getValue() + ")") + " ");
				}

				continue;
			} else {
				if (!ff.getType().isNeedBrackets()) {
					filterString = filterString
							.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator() + (" :" + ff.getField() + "_" + i) + " ");
				} else {
					filterString = filterString.replaceFirst("#" + i, ff.getField() + " " + ff.getType().getOperator()
							+ (" (:" + ff.getField() + "_" + i + ")") + " ");
				}
			}
			realcount++;
		}
		if (realcount > 0) {
			farray = new String[realcount];
			varray = new Object[realcount];
			int index = 0;
			for (int i = 0; i < ev.getFilterItems().size(); i++) {
				FilterItemInfo ff = ev.getFilterItems().get(i);
				if (ff.getType().isReplaceInSql() || ff.getType() == CompareType.IsNull || ff.getType() == CompareType.IsNotNull) {
					continue;
				}

				farray[index] = ff.getField() + "_" + i;
				varray[index] = ff.getValue();

				// 处理like模式
				if (ff.getType() == CompareType.Like) {
					if (varray[index] instanceof String) {
						if (((String) varray[index]).startsWith("'")) {
							varray[index] = "'" + varray[index] + "'";
						}
					}
				}
				index++;
			}
		}

		String hql = selectSql + "from " + getEntityName() + " where " + filterString + " " + sortSql + " ";
		TypedQuery<T> query = getEm().createQuery(hql, entityClass);
		if (realcount > 0) {
			for (int index = 0; index < farray.length; index++) {
				String f = farray[index];
				Object v = varray[index];
				query.setParameter(f, v);
			}
		}
		query.setFirstResult((currentPageIndex - 1) * pageSize);
		query.setMaxResults(pageSize);

		List<T> findList = query.getResultList();

		return findList;
	}

	@Override
	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize) {
		return findAllWithPaging(ctx, currentPageIndex, pageSize, null, null);
	}

	@Override
	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize, SelectorInfo sic) {
		return findAllWithPaging(ctx, currentPageIndex, pageSize, null, sic);
	}

	@Override
	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize, List<SortItem> sorts) {
		return findAllWithPaging(ctx, currentPageIndex, pageSize, sorts, null);
	}

	@Override
	public List<T> findAllWithPaging(IContext ctx, int currentPageIndex, int pageSize, List<SortItem> sorts, SelectorInfo sic) {

		String selectSql = QueryUtils.buildSelectorSql(sic);
		String sortSql = QueryUtils.buildOrderBySql(sorts);

		final String hql = selectSql + "from " + getEntityName() + "" + sortSql + " ";

		TypedQuery<T> query = getEm().createQuery(hql, entityClass);

		query.setFirstResult((currentPageIndex - 1) * pageSize);
		query.setMaxResults(pageSize);

		List<T> findList = query.getResultList();
		return findList;
	}

}
