package com.small.bdp.framework.domain;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import com.small.bdp.framework.exception.BaseException;

/**
 * 数据库对应实体对象的父类 需要对子对象标记注解@Entity以及@Table(name="数据库表名")。
 * 
 * 配置注解后，系统将自动实现相应的ORM操作
 * 
 * @author wentao_chang
 *
 */
@MappedSuperclass
public abstract class AbstractCoreBaseInfo implements Serializable {

	private static final long serialVersionUID = -3384366834290114644L;

	@Id
	private String fid;

	public String getFid() {
		if (fid != null) {
			fid = fid.trim();
		}
		return fid;
	}

	public void setFid(String fid) {
		if (fid != null) {
			fid = fid.trim();
		}
		this.fid = fid;
	}

	public String getTableName() {
		String tablename = null;

		Table[] tt = this.getClass().getAnnotationsByType(Table.class);
		if (tt != null && tt.length > 0) {
			tablename = tt[0].name();
		}
		if (tablename == null) {
			throw new BaseException("The info object get tablename error!");
		}
		return tablename;
	}

}
