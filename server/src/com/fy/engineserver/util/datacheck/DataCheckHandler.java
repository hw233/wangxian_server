package com.fy.engineserver.util.datacheck;

import com.fy.engineserver.util.CompoundReturn;

/**
 * 数据校验处理
 * 
 * 
 */
public interface DataCheckHandler {

	/** 管理器的名字 */
	public String getHandlerName();

	/** 涉及到的配置文件名字 */
	public String[] involveConfigfiles();

	/**
	 * 校验数据
	 * 返回boolean==true 表示有错误
	 * 返回Object 是SendHtmlToMail[]
	 */
	public CompoundReturn getCheckResult();
}
