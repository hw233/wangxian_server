package com.sqage.stat.commonstat.dao;

import java.util.List;

import com.sqage.stat.commonstat.entity.XiaoFei;

public interface XiaoFeiDao {

	    public XiaoFei getById(Long id);
		
		public List<XiaoFei> getBySql(String sql);
		
		public boolean deleteById(Long id);
		
		public XiaoFei add(XiaoFei xiaoFei);
		
		public boolean update(XiaoFei xiaoFei);
		
}
