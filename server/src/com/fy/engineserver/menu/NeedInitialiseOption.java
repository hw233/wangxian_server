package com.fy.engineserver.menu;

/**
 * 需要做特殊初始化的option,在option从文件中load进来的时候会执行init()方法
 * 
 *
 */
public interface NeedInitialiseOption {
	void init() throws Exception;
}
