package com.small.bdp.framework.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.small.bdp.core.context.IContext;
import com.small.bdp.core.query.SortItem;
import com.small.bdp.framework.domain.DataBaseInfo;
import com.small.bdp.framework.service.IDataBaseService;

public abstract class AbstractDataBaseService<T extends DataBaseInfo> extends AbstractCoreBaseService<T> implements IDataBaseService<T> {

	@Override
	public List<T> findAll(IContext ctx) {
		List<SortItem> sorts = new ArrayList<SortItem>();
		sorts.add(new SortItem("number"));
		return super.findAll(ctx, sorts);
	}

	@Override
	public void deleteByIds(IContext ctx, String ids) {
		String[] idArray = ids.split(",");
		for (String fid : idArray) {
			delete(ctx, fid);
		}
	}

}
