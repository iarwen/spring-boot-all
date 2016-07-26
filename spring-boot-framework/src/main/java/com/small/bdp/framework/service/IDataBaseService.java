package com.small.bdp.framework.service;

import com.small.bdp.core.context.IContext;
import com.small.bdp.framework.domain.DataBaseInfo;

public interface IDataBaseService<T extends DataBaseInfo> extends ICoreBaseService<T> {

	public void deleteByIds(IContext ctx, String ids);
}
